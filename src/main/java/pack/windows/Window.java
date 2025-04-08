package pack.windows;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;
import pack.matriale.ImGuiLayer;
import pack.matriale.KeyListener;
import pack.matriale.MouseListener;
import pack.renderer.*;
import pack.scens.LevelEditorScene;
import pack.scens.LevelScene;
import pack.scens.Scene;
import pack.utils.AssetPool;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {


    // Local variables
    private float width, height;
    private final String title;
    private volatile long glfwWindow;
    private boolean runtimePlaying = false;
    private long audioContext;
    private long audioDevice;

    // Local instance
    private volatile static Window window = null;
    private static Scene currentScene;
    LevelEditorScene levelEditorScene = new LevelEditorScene();
    private ImGuiLayer imGuiLayer;
    private Framebuffer framebuffer;
    private PickingTexture pickingTexture;

    public int r, g, b, a;

    // NOTE: Turn this to false if you want to include the editor in the game
    //       true means it will just ship the game without the editor and ImGuiLayer stuff
    public static final boolean RELEASE_BUILD = true;

    private Window() {
        this.width = 1000;
        this.height = 600;
        this.title = "RX-08";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
        //Window.camera = new Camera(new Vector2f((this.width / 2), (this.height / 2)));
    }

    public static void changeScene(int newScene) {

        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                break;

            case 1:
                currentScene = new LevelScene();
                break;

            default:
                assert false : "Unknown scene '" + newScene + "'";
                break;
        }
        currentScene.load();
        currentScene.init();
        currentScene.start();
    }

    public static Window get() {
        if (Window.window == null) {

            synchronized (Window.class){
                if (Window.window == null) {
                    Window.window = new Window();
                }
            }
        }
        return Window.window;
    }

    public void run() {
        System.out.println("Project has ben started successfully: " + Version.getVersion() + "!");
        System.out.println("OpenGL Version: ");

        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            System.err.println("GLFW initialization failed.");
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        //glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        glfwWindow = glfwCreateWindow((int) this.width, (int) this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            System.err.println("Window creation failed: " + width + "x" + height + ", Title: " + title);
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
                                                    Window.setWidth(newWidth);
                                                    Window.setHeight(newHeight);
                                                });

        // Make the OpenGL context current
        System.out.println("Making OpenGL context current...");
        glfwMakeContextCurrent(glfwWindow);
        System.out.println("Creating OpenGL capabilities...");
        GL.createCapabilities();
        System.out.println("OpenGL capabilities created successfully.");


        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // Initialize the audio device
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevice, attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if (!alCapabilities.OpenAL10) {
            assert false : "Audio library not supported.";
        }
        if (!alCapabilities.OpenAL10) {
            System.err.println("Audio library not supported.");
            assert false : "Audio library not supported.";
        }

        try {
            GL.createCapabilities();
        } catch (Exception e) {
            System.err.println("Failed to create OpenGL capabilities.");
            e.printStackTrace();
            throw e;
        }


        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        this.framebuffer = new Framebuffer(3840, 2160);
        this.pickingTexture = new PickingTexture(3840, 2160);
        glViewport(0,0, 3840, 2160);

        this.imGuiLayer = new ImGuiLayer(glfwWindow , pickingTexture);
        this.imGuiLayer.initImGui();
        Window.changeScene(0);
    }

    public void loop() {

        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;

        Shader defaultShader = AssetPool.getShader("assets/shaders/default.glsl");
        Shader pickingShader = AssetPool.getShader("assets/shaders/pickingShader.glsl");
        if (defaultShader == null || pickingShader == null) {
            System.err.println("Failed to load shaders.");
            return;
        }

        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            glfwPollEvents();
            // Render pass 1 . Render to picking texture

            glDisable(GL_BLEND);
            pickingTexture.enableWriting();
            glViewport(0, 0, 3840, 2160);
            glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Renderer.bindShader(pickingShader);
            currentScene.render();

            pickingTexture.disableWriting();
            glEnable(GL_BLEND);

            // Render pass 2 . Render to picking texture

            DebugDraw.beginFrame();


            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);
            this.framebuffer.bind();

            if (dt>=0.0f) {
                DebugDraw.draw();
                Renderer.bindShader(defaultShader);
                currentScene.update(dt);
                currentScene.render();
            }

            this.framebuffer.unbind();
            this.imGuiLayer.updata(dt, currentScene);
            glfwSwapBuffers(glfwWindow);
            MouseListener.endFrame();

            endTime = (float) glfwGetTime();// (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
        currentScene.saveExit();
    }

    public static Scene getScene() {

        return get().currentScene;
    }

    public static float getWidth() {

        return get().width;
    }

    public static float getHeight() {

        return get().height;
    }

    public static void setWidth(int newWidth) {

        get().width = newWidth;
    }

    public static void setHeight(int newHeight) {

        get().height = newHeight;
    }

    public static Framebuffer getFrameBuffer() {
        return get().framebuffer;
    }

    public static float getTargetAspectRatio() {
        return 16.0f / 9.0f;//getWidth() / getHeight();//
    }
}

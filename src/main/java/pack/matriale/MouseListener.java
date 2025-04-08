package pack.matriale;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import pack.windows.Window;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {

    private static MouseListener instance;
    private float lastX, lastY, xPos, yPos;
    private float scrollX, scrollY;
    private boolean[] mouseButtonPress = new boolean[9];
    private boolean isDragging = false;

    private Vector2f gameViewportPos = new Vector2f();
    private Vector2f gameViewportSize = new Vector2f();

    private MouseListener() {
        this.scrollX = 0.0f;
        this.scrollY = 0.0f;
        this.lastX = 0.0f;
        this.lastY = 0.0f;
        this.xPos = 0.0f;
        this.yPos = 0.0f;
    }

    public static MouseListener get() {
        if (instance == null) {
            synchronized (MouseListener.class) {
                if (instance == null) {
                    instance = new MouseListener();
                }
            }
        }
        return instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = (float) xpos;
        get().yPos = (float) ypos;
        get().isDragging = get().mouseButtonPress[0] || get().mouseButtonPress[1] || get().mouseButtonPress[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button >= 0 && button < get().mouseButtonPress.length) {
            if (action == GLFW_PRESS) {
                get().mouseButtonPress[button] = true;
            } else if (action == GLFW_RELEASE) {
                get().mouseButtonPress[button] = false;
                // Update dragging state based on any active button
                get().isDragging = get().mouseButtonPress[0] ||
                        get().mouseButtonPress[1] ||
                        get().mouseButtonPress[2];
            }
        }
    }

    public static void mouseScrollCallback(long window, double xoffset, double yoffset) {
        get().scrollX += xoffset;
        get().scrollY += yoffset;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return get().xPos;
    }

    public static float getY() {
        return get().yPos;
    }

    public static float getDX() {

        return get().xPos - get().lastX;
    }

    public static float getDY() {

        return get().yPos - get().lastY;
    }

    public static float getScrollX() {

        float currentX = getX() - get().gameViewportSize.x;
        currentX = (currentX / get().gameViewportSize.x) * 3840.0f;

        return currentX;
    }

    public static float getScrollY() {

        float currentY = getY() - get().gameViewportPos.y;
        currentY = 2160.0f -((currentY / get().gameViewportSize.y) * 2160.0f);
        return currentY;
    }

    public static boolean mouseButtonDown(int button) {

        return button < get().mouseButtonPress.length && get().mouseButtonPress[button];
    }

    public static void debugPrint() {
        System.out.println("Mouse Position: (" + getX() + ", " + getY() + ")");
        System.out.println("Delta: (" + getDX() + ", " + getDY() + ")");
        System.out.println("Scroll: (" + getScrollX() + ", " + getScrollY() + ")");
        System.out.println("Dragging: " + get().isDragging);
    }

    public static void setGameViewportPos(Vector2f gameViewportPos) {

        get().gameViewportPos.set(gameViewportPos);
    }

    public static void setGameViewportSize(Vector2f gameViewportSize) {

        get().gameViewportSize.set(gameViewportSize);
    }

    public static float getOrthoX() {

        float currentX = getX() - get().gameViewportSize.x;
        currentX = (currentX / get().gameViewportSize.x) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);

        Camera camera = Window.getScene().camera();
        Matrix4f viewProjection =  new Matrix4f();
        camera.getInverseView().mul(camera.getInverseProjection()
                , viewProjection);
        tmp.mul(viewProjection);

        currentX = tmp.x;
        return currentX;
    }

    public static float getOrthoY() {

        float currentY = getY() - get().gameViewportPos.y;
        currentY = -((currentY / get().gameViewportSize.y) * 2.0f - 1.0f);
        Vector4f tmp = new Vector4f(0, currentY, 0, 1);

        Camera camera = Window.getScene().camera();
        Matrix4f viewProjection =  new Matrix4f();
        camera.getInverseView().mul(camera.getInverseProjection()
                , viewProjection);
        tmp.mul(viewProjection);
        currentY = tmp.y;

        return currentY;
    }

    public static boolean isDragging() {

        return get().isDragging;
    }

}

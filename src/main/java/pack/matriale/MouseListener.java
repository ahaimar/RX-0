package pack.matriale;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector4f;
import pack.windows.Window;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

@Getter
@Setter
public class MouseListener {

    private static MouseListener instance;
    private float lastX, lastY, xPos, yPos;
    private float scrollX, scrollY;
    private boolean[] mouseButtonPress = new boolean[9];
    private boolean isDragging = false;

    private MouseListener() {
        this.scrollX = 0.0f;
        this.scrollY = 0.0f;
        this.lastX = 0.0f;
        this.lastY = 0.0f;
        this.xPos = 0.0f;
        this.yPos = 0.0f;
    }

    public static MouseListener getInstance() {
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
        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;
        getInstance().xPos = (float) xpos;
        getInstance().yPos = (float) ypos;
        getInstance().isDragging = getInstance().mouseButtonPress[0] || getInstance().mouseButtonPress[1] || getInstance().mouseButtonPress[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button >= 0 && button < getInstance().mouseButtonPress.length) {
            if (action == GLFW_PRESS) {
                getInstance().mouseButtonPress[button] = true;
            } else if (action == GLFW_RELEASE) {
                getInstance().mouseButtonPress[button] = false;
                // Update dragging state based on any active button
                getInstance().isDragging = getInstance().mouseButtonPress[0] ||
                        getInstance().mouseButtonPress[1] ||
                        getInstance().mouseButtonPress[2];
            }
        }
    }

    public static void mouseScrollCallback(long window, double xoffset, double yoffset) {
        getInstance().scrollX += xoffset;
        getInstance().scrollY += yoffset;
    }

    public static void endFrame() {
        getInstance().scrollX = 0;
        getInstance().scrollY = 0;
        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;
    }

    public static float getX() {
        return getInstance().xPos;
    }

    public static float getY() {
        return getInstance().yPos;
    }

    public static float getDX() {
        return getInstance().xPos - getInstance().lastX;
    }

    public static float getDY() {
        return getInstance().yPos - getInstance().lastY;
    }

    public static float getScrollX() {
        return 0;
    }

    public static float getScrollY() {
        return getInstance().scrollY;
    }

    public static boolean mouseButtonDown(int button) {
        return button < getInstance().mouseButtonPress.length && getInstance().mouseButtonPress[button];
    }

    public static float getOrthoX() {

        float width = Window.getWidth();

        if (width == 0) return 0;

        float currentX = getX();
        currentX = (currentX / width) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);
        tmp.mul(Window.getScene().camera().getInverseProjection())
                .mul(Window.getScene().camera().getInverseView());
        return tmp.x;
    }

    public static float getOrthoY() {

        float height = Window.getHeight();
        if (height == 0) return 0;

        float currentY = height - getY();
        currentY = (currentY / height) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(0, currentY, 0, 1);
        tmp.mul(Window.getScene().camera().getInverseProjection())
                .mul(Window.getScene().camera().getInverseView());
        return tmp.y;
    }

    public static void debugPrint() {
        System.out.println("Mouse Position: (" + getX() + ", " + getY() + ")");
        System.out.println("Delta: (" + getDX() + ", " + getDY() + ")");
        System.out.println("Scroll: (" + getScrollX() + ", " + getScrollY() + ")");
        System.out.println("Dragging: " + getInstance().isDragging);
    }
}





















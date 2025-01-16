package pack.matriale;

import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {

    private volatile static KeyListener instance;
    private final boolean[] keyPressed = new boolean[GLFW.GLFW_KEY_LAST];
    private final boolean[] keyJustPressed = new boolean[350];
    private final boolean[] keyJustReleased = new boolean[350];

    private KeyListener() {}

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            synchronized (KeyListener.class) {
                if (KeyListener.instance == null) {
                    KeyListener.instance = new KeyListener();
                }
            }
        }
        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key < get().keyPressed.length) { // Ensure key index is within bounds
            if (action == GLFW_PRESS) {
                get().keyPressed[key] = true;
                get().keyJustPressed[key] = true; // Key pressed this frame
            } else if (action == GLFW_RELEASE) {
                get().keyPressed[key] = false;
                get().keyJustReleased[key] = true; // Key released this frame
            }
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return keyCode < get().keyPressed.length && get().keyPressed[keyCode];
    }

    public static boolean isKeyJustPressed(int keyCode) {
        return keyCode < get().keyJustPressed.length && get().keyJustPressed[keyCode];
    }

    public static boolean isKeyJustReleased(int keyCode) {
        return keyCode < get().keyJustReleased.length && get().keyJustReleased[keyCode];
    }

    public static void endFrame() {
        // Reset "just pressed" and "just released" states
        for (int i = 0; i < get().keyJustPressed.length; i++) {
            get().keyJustPressed[i] = false;
            get().keyJustReleased[i] = false;
        }
    }
}

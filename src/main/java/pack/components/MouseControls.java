package pack.components;

import pack.matriale.MouseListener;
import pack.objectes.GameObject;
import pack.windows.Window;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {

    private GameObject holdingObject = null;

    /**
     * Pick up a GameObject for placement.
     *
     * @param go The GameObject to pick up.
     */
    public void pickupObject(GameObject go) {
        if (go != null) {
            this.holdingObject = go;
            Window.getScene().addGameObjectToScene(go);
            setHoldingObjectTransparency(0.5f); // Semi-transparent while holding
        } else {
            System.err.println("Cannot pick up a null GameObject.");
        }
    }

    /**
     * Place the currently held GameObject and reset its state.
     */
    public void place() {
        if (this.holdingObject != null) {
            setHoldingObjectTransparency(1.0f); // Restore original opacity
            this.holdingObject = null;
        } else {
            System.err.println("No object is currently held for placement.");
        }
    }

    /**
     * Set the transparency of the currently held object.
     *
     * @param alpha The desired alpha transparency (0.0 to 1.0).
     */
    private void setHoldingObjectTransparency(float alpha) {
        if (this.holdingObject != null) {
            SpriteRenderer renderer = this.holdingObject.getComponent(SpriteRenderer.class);
            if (renderer != null) {
                renderer.getColor().w = alpha; // Modify the alpha directly if possible
            } else {
                System.err.println("SpriteRenderer component is missing on the held object.");
            }
        }
    }

    @Override
    public void update(float dt) {
        if (holdingObject != null && holdingObject.transForm != null) {
            holdingObject.transForm.position.x = MouseListener.getOrthoX() - 16;
            holdingObject.transForm.position.y = MouseListener.getOrthoY() - 16;

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }

}

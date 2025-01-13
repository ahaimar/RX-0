package pack.components;

import org.joml.Vector4f;
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
        }
    }

    /**
     * Place the currently held GameObject and reset its state.
     */
    public void place() {
        if (this.holdingObject != null) {
            setHoldingObjectTransparency(1.0f); // Restore original opacity
            this.holdingObject = null;
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
                renderer.setColor(new Vector4f(1, 1, 1, alpha));
            }
        }
    }

    @Override
    public void update(float dt) {

        if (this.holdingObject != null && this.holdingObject.transForm != null) {
            // Update the object's position to follow the mouse
            this.holdingObject.transForm.position.x = MouseListener.getOrthoX() - 16;
            this.holdingObject.transForm.position.y = MouseListener.getOrthoY() - 16;//this.holdingObject.transForm.scale.y / 2

            // Check if the left mouse button is pressed to place the object
            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }

}

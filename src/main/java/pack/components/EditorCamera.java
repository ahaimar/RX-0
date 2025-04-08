package pack.components;

import org.joml.Vector2f;
import pack.matriale.Camera;
import pack.matriale.MouseListener;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

public class EditorCamera extends Component{

    private float dragDebounce = 0.032f;
    private float scrollSensitivity = 0.1f;

    private Camera levelEditorCamera;
    private Vector2f clickOrigin;

    public EditorCamera(Camera levelEditorCamera){

        this.levelEditorCamera = levelEditorCamera;
        this.clickOrigin = new Vector2f();
    }

    @Override
    public void update(float dt) {

        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE) && dragDebounce > 0){
            this.clickOrigin = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            dragDebounce -= dt;
            return;
        }else if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)){
            Vector2f mPos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            Vector2f delta = new Vector2f(mPos)
                    .sub(clickOrigin);

            levelEditorCamera.position.sub(delta.mul(dt));
            this.clickOrigin.lerp(mPos, dt);
        }

        if (dragDebounce <= 0.0f && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)){
            dragDebounce = 0.1f;
        }
        if (MouseListener.getScrollY() != 0.0f){
            float addValue = (float) Math.pow(Math.abs(MouseListener.getScrollY()) * scrollSensitivity, 1 / levelEditorCamera.getZoom());

            addValue *= Math.signum(MouseListener.getScrollY());
            levelEditorCamera.addZoom(addValue);
        }
    }
}
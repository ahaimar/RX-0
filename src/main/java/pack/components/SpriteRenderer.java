package pack.components;

import imgui.ImGui;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector4f;
import pack.matriale.TransForm;
import pack.renderer.Texture;

@Getter
@Setter
public class SpriteRenderer extends Component {

    private Vector4f color = new Vector4f(1, 1, 1, 1);
    private  Sprite sprite = new Sprite();
    private transient TransForm transForm;
    private transient boolean isDirty = true;

//    public SpriteRenderer(Vector4f color) {
//
//        this.color = color;
//        this.sprite = new Sprite(null);
//        this.isDirty = true;
//    }
//
//    public SpriteRenderer(Sprite sprite) {
//
//        if (sprite == null) {
//            throw new IllegalArgumentException("Sprite cannot be null");
//        }
//        this.sprite = sprite;
//        this.color = new Vector4f(1, 1, 1, 1);
//        this.isDirty = true;
//    }

    @Override
    public void start() {

        this.transForm = gameObject.transForm.copy();
    }

    @Override
    public void update(float deltaTime) {

        if (!this.transForm.equals(this.gameObject.transForm)){
            this.transForm.copy(this.gameObject.transForm);
            isDirty = true;
        }
    }

    @Override
    public void imgui() {

        float[] imColor = {color.z, color.y, color.x, color.w};
        if (ImGui.colorPicker4("color Picker", imColor)){
            this.color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            isDirty = true;
        }
    }

    public Vector4f getColor() {

        return color;
    }

    public Texture getTexture(){

        return sprite.getTexture();
    }

    public Vector2f[] getTexCoord() {

        return sprite.getTexCoords();
    }

    public void setSprite(Sprite sprite) {

        this.sprite = sprite;
        isDirty = true;
    }

    public void getColor(Vector4f color) {

        if (!this.color.equals(color)) {
            this.color.set(color);
            isDirty = true;
        }
    }

    public boolean isDirty() {

        return this.isDirty;
    }

    public void setClean() {

        this.isDirty = false;
    }

}

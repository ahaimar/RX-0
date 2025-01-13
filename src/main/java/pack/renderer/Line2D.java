package pack.renderer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector3f;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Line2D {

    private Vector2f from;
    private Vector2f to;
    private Vector3f color;
    private int lifeTime;

    public int biginframe() {

        this.lifeTime--;
        return this.lifeTime;
    }
}

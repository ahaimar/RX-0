package pack.matriale;

import org.joml.Vector2f;

public class TransForm {

    public Vector2f position;
    public Vector2f scale;

    public TransForm() {

        init(new Vector2f(), new Vector2f());
    }

    public TransForm(Vector2f position) {

        init(position, new Vector2f());
    }

    public TransForm(Vector2f position, Vector2f scale) {

        init(position, scale);
    }

    public void init(Vector2f position, Vector2f scale) {

        if (scale.x < 0 || scale.y < 0) {
            throw new IllegalArgumentException("trans form class : Scale values must be non-negative.");
        }
        this.position = position;
        this.scale = scale;
    }

    public TransForm copy() {

        return new TransForm(new Vector2f(this.position), new Vector2f(this.scale));
    }

    public void copy(TransForm to) {
        if (to == null) {
            throw new IllegalArgumentException("Target TransForm cannot be null.");
        }
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;
        if (!(obj instanceof TransForm)) return false;
        TransForm other = (TransForm) obj;
        return other.position.equals(this.position) && other.scale.equals(this.scale);
    }
}

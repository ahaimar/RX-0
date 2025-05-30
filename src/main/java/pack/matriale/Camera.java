package pack.matriale;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

@Getter
@NoArgsConstructor
public class Camera {

    private Vector2f projectionSize = new Vector2f(32.0f * 40.0f, 32.0f * 21.0f);
    @Setter
    private Matrix4f projectionMatrix, inverseProjection, inverseView;
    @Setter
    private Matrix4f viewMatrix;
    @Setter
    public Vector2f position;

    @Setter
    private float zoom = 1.0f;

    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, projectionSize.x * this.zoom,
                0.0f, projectionSize.y * zoom, 0.0f, 100.0f);
        projectionMatrix.invert(inverseProjection);
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                                cameraFront.add(position.x, position.y, 0.0f),
                                cameraUp);
        inverseView = new Matrix4f(this.viewMatrix).invert();

        return this.viewMatrix;
    }

    public void setProjectionSize(Vector2f newSize) {
        this.projectionSize.set(newSize);
        adjustProjection();
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
        adjustProjection();
    }

    public void addZoom(float zoom) {

        this.zoom += zoom;
    }

}

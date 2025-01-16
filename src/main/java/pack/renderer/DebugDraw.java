package pack.renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import pack.utils.AssetPool;
import pack.utils.JMath;
import pack.windows.Window;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.joml.Vector2f;

public class DebugDraw {

    private static final int MAX_LINES = 500;

    private static final List<Line2D> lines = new ArrayList<>();

    private  static final float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static final Shader shader = AssetPool.getShader("assets/shaders/default2.glsl");

    private static int vboId;
    private static int vaoId;
    private static boolean started = false;

    public static void start() {

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, (long) vertexArray.length * Float.BYTES, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT,false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT,false, 6 * Float.BYTES,
                3  * Float.BYTES);
        glEnableVertexAttribArray(1);

        glLineWidth(2.0f);
    }

    public static void beginFrame() {

        if (!started) {
            start();
            started = true;
        }

        // Use an iterator to safely remove expired lines
        lines.removeIf(line -> line.biginframe() < 0); // this is equivalent to your loop logic but cleaner
    }

    public static void draw() {

        if (lines.isEmpty()) return;

        int index = 0;
        for (Line2D line : lines) {
            for (int i = 0; i < 2; i++) {
                Vector2f position = i == 0 ? line.getFrom():  line.getTo();
                Vector3f color = line.getColor();

                // load positions
                vertexArray[index] = position.x;
                vertexArray[index + 1] = position.y;
                vertexArray[index + 2] = -10.0f;

                // load clolors
                vertexArray[index + 3] = color.x;
                vertexArray[index + 4] = color.y;
                vertexArray[index + 5] = color.z;

                index += 6;

            }
        }
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2));

        // use shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());

        // bind the vao
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // draw the batch
        glDrawArrays(GL_LINES, 0, lines.size() * 6 * 2);

        // Disable location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        // unbind shader
        shader.detach();
    }

    // add line2d methode
    public static void addLine2D(Vector2f from, Vector2f to) {

        //TODO: add constants from common colors
        addLine2D(from, to, new Vector3f(0,1,0), 1);
    }

    public static  void addLine2D(Vector2f from, Vector2f to, Vector3f color ) {

        addLine2D(from, to, color, 1);
    }

    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color, int lifetime) {

        if (lines.size() >= MAX_LINES) return;
        DebugDraw.lines.add(new Line2D(from, to, color, lifetime));
    }

    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation,
                                Vector3f color, int lifetime) {
        Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).div(0.5f));
        Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).mul(0.5f));

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y),
                new Vector2f(min.x, max.y),
                new Vector2f(max.x, max.y),
                new Vector2f(max.x, min.y),
        };
        if (rotation != 0.0f) {
            for (Vector2f vet : vertices) {
                JMath.rotate(vet, rotation, center);
            }
        }
        addLine2D(vertices[0], vertices[1], color, lifetime);
        addLine2D(vertices[0], vertices[3], color, lifetime);
        addLine2D(vertices[1], vertices[2], color, lifetime);
        addLine2D(vertices[2], vertices[3], color, lifetime);
    }

    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation) {
        addBox2D(center, dimensions, rotation,new Vector3f(0, 1, 0), 1);
    }

    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation, Vector3f color) {

        addBox2D(center, dimensions,rotation, color,1);
    }

    public static void addCircle(Vector2f center, float radius,
                                  Vector3f color, int lifetime){

        Vector2f[] points = new Vector2f[30];
        int increment = 360 / points.length;
        int currentAngle = 0;

        for (int i = 0; i < points.length; i++) {

            Vector2f tmp = new Vector2f(0, radius);
            JMath.rotate(tmp, currentAngle, new Vector2f());
            points[i] = new Vector2f(tmp).add(center);

            if (i >0){
                addLine2D(points[i - 1], points[i], color, lifetime);
            }
            currentAngle += increment;
        }
        addLine2D(points[points.length - 1], points[0], color, lifetime);
    }

    public static void addCircle(Vector2f center, float radius){

        addCircle(center, radius, new Vector3f(0,1,0), 1);
    }

    public static void addCircle(Vector2f center, float radius, Vector3f color){

        addCircle(center, radius, color, 1);
    }

    public static void destroy() {
        glDeleteBuffers(vboId);
        glDeleteVertexArrays(vaoId);
    }

}






















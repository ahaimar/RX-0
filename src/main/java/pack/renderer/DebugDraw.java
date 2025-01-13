package pack.renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import pack.utils.AssetPool;
import pack.windows.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

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

    public static  void beginFrame() {

        if (!started) {
            start();
            started = true;
        }
        for (int i = 0; i < lines.size() ; i++) {
            if (lines.get(i).biginframe() < 0) {
                lines.remove(i);
                i--;
            }
        }
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
    public static  void addLine2D(Vector2f from, Vector2f to) {

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

    public static void destroy() {
        glDeleteBuffers(vboId);
        glDeleteVertexArrays(vaoId);
    }

}






















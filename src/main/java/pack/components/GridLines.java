package pack.components;

import org.joml.Vector2f;
import org.joml.Vector3f;
import pack.matriale.Camera;
import pack.renderer.DebugDraw;
import pack.utils.Settings;
import pack.windows.Window;

public class GridLines extends Component {

    @Override
    public void update(float dt) {
        Camera camera = Window.getScene().camera();

        Vector2f cameraPos = camera.position;
        Vector2f projectionSize = camera.getProjectionSize();

        int firstX = ((int) Math.floor(cameraPos.x / Settings.GRID_WIDTH)) * Settings.GRID_HEIGHT;
        int firstY = ((int) Math.floor(cameraPos.y / Settings.GRID_HEIGHT)) * Settings.GRID_HEIGHT;

        // Calculate the number of vertical and horizontal lines that are visible in the viewport
        int numVtLines = (int) (projectionSize.x * camera.getZoom() / Settings.GRID_WIDTH) + 2;  // Extra to cover edge cases
        int numHtLines = (int) (projectionSize.y * camera.getZoom() / Settings.GRID_HEIGHT) + 2;

        int width = (int) (projectionSize.x * camera.getZoom()) + Settings.GRID_WIDTH * 2;
        int height = (int) (projectionSize.y * camera.getZoom()) + Settings.GRID_HEIGHT * 2;

        int maxLine = Math.max(numVtLines, numHtLines);
        Vector3f color = new Vector3f(0.5f, 1.5f, 0.1f);
        for (int i = 0; i < maxLine; i++) {
            int x = firstX + (i * Settings.GRID_WIDTH);
            if (x >= cameraPos.x - width && x <= cameraPos.x + width) {  // Only draw if within the viewport
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color);
            }
        }

        // Draw horizontal lines within the visible range
        for (int i = 0; i < numHtLines; i++) {
            int y = firstY + (i * Settings.GRID_HEIGHT);
            if (y >= cameraPos.y - height && y <= cameraPos.y + height) {  // Only draw if within the viewport
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);
            }
        }
    }
}

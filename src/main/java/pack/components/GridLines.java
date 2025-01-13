package pack.components;

import org.joml.Vector2f;
import org.joml.Vector3f;
import pack.renderer.DebugDraw;
import pack.utils.Settings;
import pack.windows.Window;

public class GridLines extends Component {

    @Override
    public void update(float dt) {
        // Get camera position and projection size
        Vector2f cameraPos = Window.getScene().camera().position;
        Vector2f projectionSize = Window.getScene().camera().getProjectionSize();

        // Calculate the starting grid line positions
        int firstX = ((int) Math.floor(cameraPos.x / Settings.GRID_WIDTH)) * Settings.GRID_WIDTH;
        int firstY = ((int) Math.floor(cameraPos.y / Settings.GRID_HEIGHT)) * Settings.GRID_HEIGHT;

        // Calculate the number of vertical and horizontal lines
        int numVtLines = (int) Math.ceil(projectionSize.x / Settings.GRID_WIDTH) + 2;
        int numHtLines = (int) Math.ceil(projectionSize.y / Settings.GRID_HEIGHT) + 2;

        // Calculate the grid dimensions
        int width = (int) projectionSize.x;
        int height = (int) projectionSize.y;

        // Set grid color
        Vector3f color = new Vector3f(0.2f, 0.2f, 0.2f);

        // Draw vertical and horizontal lines
        for (int i = 0; i < numVtLines; i++) {
            int x = firstX + (i * Settings.GRID_WIDTH);
            DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color);
        }

        for (int i = 0; i < numHtLines; i++) {
            int y = firstY + (i * Settings.GRID_HEIGHT);
            DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);
        }
    }
}

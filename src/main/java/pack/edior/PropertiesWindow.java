package pack.edior;

import imgui.ImGui;
import pack.matriale.MouseListener;
import pack.objectes.GameObject;
import pack.renderer.PickingTexture;
import pack.scens.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {

    private GameObject activegameObject = null;
    private PickingTexture pickingTexture;

    public PropertiesWindow(PickingTexture pickingTexture){

        this.pickingTexture = pickingTexture;
    }

    public void update(float dt, Scene currentScene){

        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {

            int x = (int) MouseListener.getScrollX();
            int y = (int) MouseListener.getScrollY();
            int gameObjectId = pickingTexture.readPixel(x, y);
            activegameObject = currentScene.getGameObject(gameObjectId);
        }
    }

    public void imgui(){

        if (activegameObject != null){
            ImGui.begin("Properties");
            activegameObject.imgui();
            ImGui.end();
        }
    }

}

package pack.scens;

import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import pack.components.*;
import pack.matriale.Camera;
import pack.matriale.Prefabs;
import pack.matriale.TransForm;
import pack.objectes.GameObject;
import pack.renderer.DebugDraw;
import pack.utils.AssetPool;

public class LevelEditorScene extends Scene{

    private GameObject obj1, obj2 ;
    private int currentPage = 0; // Add this as a class field

    private Spritesheet sprites;
    MouseControls mouseControls = new MouseControls();
    SpriteRenderer obj1Sprite;
    GameObject levelEditorStuff = new GameObject("level Editor", new TransForm(new Vector2f()), 0);
    private GameObject activeGameObject;

    public LevelEditorScene() {}

    @Override
    public void init() {

        this.camera = new Camera(new Vector2f(-250, 0));
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());
        levelEditorStuff.addComponent(new EditorCamera(this.camera));

        loadResources();
        // Initialize sprites after loading resources
        sprites = AssetPool.getSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png");

        // Create and initialize game objects
        obj1 = new GameObject("obj1",
                new TransForm(new Vector2f(200, 100), new Vector2f(0, 0)), 2);
        obj1Sprite = new SpriteRenderer();
        obj1Sprite.setColor(new Vector4f(1, 0, 0, 1));
        obj1.addComponent(obj1Sprite);
        obj1.addComponent(new RigidBody());
        this.addGameObjectToScene(obj1);

        obj2 = new GameObject("obj2",
                new TransForm(new Vector2f(400, 100), new Vector2f(256, 256)), 3);
        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
        Sprite obj2Sprite = new Sprite();
        obj2Sprite.setTexture(AssetPool.getTexture("assets/images/blendImage2.png"));
        obj2SpriteRenderer.setSprite(obj2Sprite);
        obj2.addComponent(obj2SpriteRenderer);
        this.addGameObjectToScene(obj2);

    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png",
                new Spritesheet(AssetPool.getTexture(
                        "assets/images/spritesheets/decorationsAndBlocks.png"),
                        16, 16, 26, 0));
        AssetPool.getTexture("assets/images/blendImage2.png");

        for (GameObject go : gameObjects) {
            if (go.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }
        }
    }

    @Override
    public void update(float dt) {

        levelEditorStuff.update(dt);
        this.camera.adjustProjection();

        for (GameObject go : this.gameObjects){
            go.update(dt);
        }
        this.renderer.render();
    }

    @Override
    public void render(){
        this.renderer.render();
    }

    @Override
    public void imgui() {
        ImGui.begin("Sprite Selector");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        int itemsPerPage = 20; // Configurable items per page
        //int currentPage = 0;
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, sprites.size());

        float scale = 1.0f; // Configurable scale
        for (int i = startIndex; i < endIndex; i++) {
            Sprite sprite = sprites.getSprite(i);

            float spriteWidth = sprite.getWidth() * scale;
            float spriteHeight = sprite.getHeight() * scale;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x,
                    texCoords[0].y, texCoords[0].x, texCoords[2].y)) {

                GameObject object = Prefabs.generateSpriteObject(sprite, spriteWidth, spriteHeight);
                System.out.println("Object generated: " + object.getName());
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);

            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < endIndex && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }

        if (currentPage > 0) {
            if (ImGui.button("Previous Page")) {
                currentPage--;
            }
            ImGui.sameLine();
        }
        if (endIndex < sprites.size()) {
            if (ImGui.button("Next Page")) {
                currentPage++;
            }
        }

        ImGui.end();
    }

}
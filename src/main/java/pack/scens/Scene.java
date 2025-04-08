package pack.scens;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import pack.components.ComponentDeserializer;
import pack.gsonManager.GameObjectDeserializer;
import pack.matriale.Camera;
import pack.objectes.GameObject;
import pack.renderer.Renderer;
import pack.components.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    protected boolean levelLoaded = false;

    public Scene(){}

    public void init(){}

    public void start(){

        for (GameObject gameObject : gameObjects) {
            gameObject.start();
            this.renderer.add(gameObject);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go){

        if (!isRunning) {
            gameObjects.add(go);
            System.out.println("\nObject " + go.getName() + " is already created !! on the scene *_*");
        }else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
            System.out.println("Added GameObject: " + go+ " to Renderer.");
        }
    }

    public abstract void update(float deltaTime);
    public abstract void render();

    public Camera camera() {

        return this.camera;
    }

    public void imgui(){}

    public void saveExit() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try (FileWriter writer = new FileWriter("level.txt")) {
            // Convert game objects to JSON and save to file
            String json = gson.toJson(this.gameObjects.toArray());
            writer.write(json);
            System.out.println("Level saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving level: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String inFile = "";
        try {
            // Read the JSON file content
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
            if (inFile.isEmpty()) {
                System.out.println("No data to load. File is empty.");
                return;
            }
        }catch (IOException e){ e.printStackTrace();}

        if (inFile.equals("")) {
            int maxGoId = -1;
            int maxCompId = -1;
            // Deserialize JSON into GameObject array
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (GameObject obj : objs) {
                addGameObjectToScene(obj);
                for (Component c : obj.getAllComponents()) {

                    if (c.getUid() > maxCompId){
                        maxCompId = c.getUid();
                    }
                }
                if (obj.getUid() > maxGoId){
                    maxGoId = obj.getUid();
                }
            }
            maxGoId++;
            maxCompId++;
            GameObject.init(maxGoId);
            Component.init(maxCompId);
            this.levelLoaded = true;
        }
    }

    public GameObject getGameObject(int gameObjetId){

        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject-> gameObject.getUid() == gameObjetId)
                .findFirst();
        return result.orElse(null);
    }

}


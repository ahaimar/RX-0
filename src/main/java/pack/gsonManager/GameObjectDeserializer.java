package pack.gsonManager;

import com.google.gson.*;
import pack.matriale.TransForm;
import pack.objectes.GameObject;
import pack.components.Component;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject> {
    @Override
    public GameObject deserialize(JsonElement jsonElement, Type type,
                                  JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.getAsJsonArray("components");
        TransForm transForm = jsonDeserializationContext.deserialize(jsonObject.get("transForm"),
                TransForm.class);
        int zIndex = jsonDeserializationContext.deserialize(jsonObject.get("zIndex"), int.class);
        GameObject go = new GameObject(name, transForm, zIndex);

        for (JsonElement e : components){

            Component c = jsonDeserializationContext.deserialize(e, Component.class);
            go.addComponent(c);
        }

        return go;
    }

}

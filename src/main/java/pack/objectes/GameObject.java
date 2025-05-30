package pack.objectes;

import lombok.Getter;
import lombok.Setter;
import pack.matriale.TransForm;
import pack.components.Component;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class GameObject {

    private static  int ID_COUNTER = 0;
    @Getter
    private int uid = -1;

    private String name;
    private List<Component> components;
    public TransForm transForm;
    private  int zIndex;

    public GameObject(String name){

        this.name = name;
        this.components = new ArrayList<>();
        this.transForm = new TransForm();
        this.zIndex = 0;
    }

    public GameObject(String name, TransForm transForm, int zIndex) {

        this.name = name;
        this.components = new ArrayList<>();
        this.transForm = transForm;
        this.zIndex = zIndex;

        this.uid = ID_COUNTER++;
    }

    public <T extends  Component> T getComponent(Class<T> componentClass) {

        for (Component c : components ){

            if (componentClass.isAssignableFrom(c.getClass())) {

                try {
                return componentClass.cast(c);
                } catch (ClassCastException e) {
                    throw new RuntimeException("Invalid component type for " + name, e);
                }
            }
        }
        return  null;
    }

    public <T extends  Component> void removeComponent(Class<T> componentClass) {

        for (int i = 0; i < components.size(); i++) {

            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {

                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c) {

        c.generateId();
        this.components.add(c);
        c.gameObject = this;
    }

    public void update(float deltaTime) {

        for (int i = 0; i < components.size(); i++) {

            components.get(i).update(deltaTime);
        }
    }

    public void start() {

        for (int i = 0; i < components.size(); i++) {

            components.get(i).start();
        }
    }

    public int zIndex() {

        return this.zIndex;
    }

    public void imgui() {

        for (Component c : components) {
            c.imgui();
        }
    }

    public static void init(int maxId) {

        ID_COUNTER = maxId;
    }

    public List<Component> getAllComponents() {
        return this.components;
    }
}

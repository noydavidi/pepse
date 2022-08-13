package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * this class represents the sky object
 */
public class Sky {

    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");

    /**
     * this method creates the sky in the simulation
     * @param gameObjects the game objects collection
     * @param windowDimensions the window dimensions
     * @param skyLayer the layer to put the sky in the simulation
     * @return the sky object
     */
    public static GameObject create(GameObjectCollection gameObjects, Vector2 windowDimensions, int skyLayer){
        GameObject sky = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sky, skyLayer);
        sky.setTag("sky");
        return sky;
    }
}

package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * this class represents the sun's halo game object
 */
public class SunHalo extends GameObject{

    public static GameObject mainSun;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public SunHalo(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    /**
     * this method creates the halo
     * @param gameObjects the game objects collection
     * @param layer the layer in the simulation
     * @param sun the object sun to follow
     * @param color the color of the halo
     * @return the sun halo
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    int layer,
                                    GameObject sun,
                                    Color color){
        mainSun = sun;
        OvalRenderable sunHaloRenderable = new OvalRenderable(color);
        GameObject sunHalo = new GameObject(sun.getTopLeftCorner(),
                new Vector2(Sun.SIZE + 50 , Sun.SIZE + 50), sunHaloRenderable);
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setCenter(sun.getCenter());
        sunHalo.addComponent(num->sunHalo.setCenter(sun.getCenter()));
        gameObjects.addGameObject(sunHalo, layer);
        sunHalo.setTag("sunHalo");
        return sunHalo;
    }
}

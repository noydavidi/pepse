package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * this class represents the night situation in the simulation
 */
public class Night {

    private static final Color BASIC_SKY_COLOR = Color.decode("#000000");
    private static final Float MIDNIGHT_OPACITY = 0.5f;


    /**
     * this method creates the Night object
     * @param gameObjects the game objects collection
     * @param windowDimensions the window dimensions
     * @param cycleLength the cycle length of a day
     * @param layer the layer in the game objects
     * @return the Night object
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    int layer,
                                    Vector2 windowDimensions,
                                    float cycleLength){
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(night, layer);

        new Transition<Float>(
                night, //the game object being changed
                night.renderer()::setOpaqueness,  //the method to call
                0f,    //initial transition value
                MIDNIGHT_OPACITY,   //final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT,  //use a cubic interpolator
                cycleLength/2,   //transtion fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);  //nothing further to execute upon reaching final value        )

        night.setTag("night");
        return night;
    }


}


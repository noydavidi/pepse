package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**`
 * this class represents the sun object
 */
public class Sun extends GameObject{

    public static int SIZE = 60;
    private final Vector2 windowDimensions;
    private final float cycleLength;
    public float radius;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Sun(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Vector2 windowDimensions, float cycleLength) {
        super(topLeftCorner, dimensions, renderable);
        this.windowDimensions = windowDimensions;
        this.radius = windowDimensions.y() / 2 - 100;
        this.cycleLength = cycleLength;
        this.setRadiusTransition();
        this.setSunMovement();
    }


    /**
     * this method creates the Sun object
     * @param gameObjects the game objects collection
     * @param windowDimensions the window dimensions
     * @param cycleLength the cycle length of a day
     * @param layer the layer in the game objects
     * @return the sun object
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    int layer,
                                    Vector2 windowDimensions,
                                    float cycleLength ){

        OvalRenderable sunRenderable = new OvalRenderable(Color.YELLOW);
        GameObject sun = new Sun(Vector2.ZERO,
                new Vector2(SIZE , SIZE), sunRenderable, windowDimensions, cycleLength);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        Vector2 vectorCetner = new Vector2(windowDimensions.x()/4, windowDimensions.y()/6);
        sun.setCenter(vectorCetner);


        gameObjects.addGameObject(sun, layer);
        sun.setTag("sun");
        return sun;
    }

    /**
     * this method sets the radius transition, changed it by little
     */
    private void setRadiusTransition(){
        new Transition<Float>(
                this, //the game object being changed
                (Float change) -> this.radius += change,  //the method to call
                0f,    //initial transition value
                3f,   //final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,  //use a cubic interpolator
                cycleLength * 60,   //transtion fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);  //nothing further to execute upon reaching final value
    }

    /**
     * this method set the sum move in a circle by transition
     */
    private void setSunMovement(){
        new Transition<Float>(
                this, //the game object being changed
                (Float angle) -> this.setCenter(calcSunPosition(angle)),  //the method to call
                0f,    //initial transition value
                360f,   //final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,  //use a cubic interpolator
                cycleLength * 60,   //transtion fully over half a day
                Transition.TransitionType.TRANSITION_LOOP,
                null);  //nothing further to execute upon reaching final value
    }

    /**
     * this method calculate the sun position
     * @param angle the angle we want the sun to appear
     * @return the new vector position
     */
    private Vector2 calcSunPosition(float angle) {
        float r = this.radius;
        float x;
        float y;

        x = (windowDimensions.x() / 2) - (r * (float) Math.cos(angle));
        y = (windowDimensions.y() / 2) + r * (float) Math.sin(angle);
        return new Vector2(x,y);
    }
}

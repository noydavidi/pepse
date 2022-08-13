package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;

/**
 * this class reprents the Leaf block idea
 * the object extends Block
 */
public class Leaf extends Block{

    private final Random random = new Random();
    private final Vector2 windowDimensions;
    private final GameObjectCollection gameObjects;
    private final int layer;


    private static final Color LEAVES_COLOR = new Color(50, 200, 30);
    private final float cycleLength;
    private GameObject block;
    private final int lifeTime;


    /**
     * Construct the Leaf game object
     * @param topLeftCorner the coordinates for the object's position
     * @param gameObjects the game object collection
     * @param layer the layer to put the leaves in
     * @param windowDimensions the window dimensions
     * @param cycleLength the cycle length
     */
    public Leaf(Vector2 topLeftCorner, GameObjectCollection gameObjects,
                int layer, Vector2 windowDimensions, float cycleLength) {
        super(topLeftCorner, new RectangleRenderable(LEAVES_COLOR));
        this.cycleLength = cycleLength;
        this.windowDimensions = windowDimensions;
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.lifeTime = getRandomLifeTime();
    }

    /**
     * this method creates a leaf block
     */
    public void create(){
        this.block = Block.create(gameObjects, layer, this.getTopLeftCorner(),
                new RectangleRenderable(LEAVES_COLOR));
        this.block.physics().setMass(0);
        this.setScheduledTasks();
    }

    /**
     * this method creates the leaf's transitions with time
     */
    private void setScheduledTasks(){
        int waitime = random.nextInt(10);
        new ScheduledTask(this.block, waitime, true, this::setAngleTransition);
        new ScheduledTask(this.block, waitime, true, this::setDimensionsTransition);
        new ScheduledTask(this.block, this.lifeTime, false, this::setFallingDownAndFadeOut);
        new ScheduledTask(this.block, this.lifeTime, false, this::setHorizontalTransition);
        new ScheduledTask(this.block, 1.75f*this.lifeTime, false, this::create);
    }

    /**
     * the method creates the angle leaf transition
     * makes it move back and forth
     */
    private void setAngleTransition(){
        new Transition<Float>(
                this.block, //the game object being changed
                (Float angle) -> this.block.renderer().setRenderableAngle(angle),//the method to call
                5f,    //initial transition value
                -5f,   //final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

    /**
     * this method creates the leaf dimension transition
     * makes it wider and thinner
     */
    private void setDimensionsTransition(){
        new Transition<Float>(
                this.block, //the game object being changed
                (Float width) -> this.block.setDimensions(new Vector2(this.block.getDimensions().x() + width,
                        this.block.getDimensions().y())),
                0.0000002f,
                -0.0000002f,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

    /**
     * this method gets a random lifetime for a leaf
     * @return the random lifetime
     */
    private int getRandomLifeTime(){
        return random.nextInt(200)+5;
    }

    /**
     * the method makes the leaf to fall and fade out after its lifetime ends
     */
    private void setFallingDownAndFadeOut(){
        this.block.renderer().fadeOut(this.lifeTime);
        Vector2 newVelocity = new Vector2(this.block.getVelocity().x(), 30);
        this.block.transform().setVelocity(newVelocity);
    }

    /**
     * this method create the leaf to move a little from side to side
     */
    private void setHorizontalTransition(){
        new Transition<Float>(
                this.block, //the game object being changed
                (Float x) -> this.block.transform().setVelocityX(x),//the method to call
                10f,
                -10f,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                this.lifeTime / 6f,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }
}

package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * this class represents the blocks objects in the simulation
 */
public class Block extends GameObject {

    public static final int SIZE = 30;

    /**
     * Construct the block
     * @param topLeftCorner the coordinate for the block's position
     * @param renderable the render to put
     */
    public Block(Vector2 topLeftCorner, Renderable renderable){
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    /**
     * this static method creates the block itself by constructing it and put it in the game object collection
     * @param gameObjects the game object collection
     * @param layer the layer to put on
     * @param topLeftCorner the coordinate for the block's position
     * @param renderable the render to put
     * @return the block object
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    int layer,
                                    Vector2 topLeftCorner,
                                    Renderable renderable){
        GameObject block = new Block(topLeftCorner, renderable);
        gameObjects.addGameObject(block, layer);
        return block;
    }


    /**
     * this method creates the behaviour of the block on collision with objects
     * @param other the other object the block colided with
     * @param collision the collision effect
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.transform().setVelocity(Vector2.ZERO);
    }


}

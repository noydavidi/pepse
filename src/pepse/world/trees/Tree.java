package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.Random;

/**
 * this class represents the tree object in the simulation.
 * it is created by Leaf objects and Trunk object
 */
public class Tree extends GameObject {

    private static final Color WOOD_COLOR = new Color(100, 50, 20);
    private static final Color LEAVES_COLOR = new Color(50, 200, 30);

    public Random random = new Random();
    private final Vector2 windowDimensions;
    private final int groundLayer;
    private final Terrain terrain;
    private int layer;
    private final GameObjectCollection gameObjects;
    private final float cycleLength;


    /**
     * Construct a new Tree instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param gameObjects   The game objects collection
     * @param windowDimensions the window dimensions
     * @param treeLayer     The tree layer in the simulation
     * @param groundLayer   The ground layer in the simulation
     * @param cycleLength   The cycle length of the simulation
     */
    public Tree(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, GameObjectCollection gameObjects,
                Vector2 windowDimensions, int treeLayer, int groundLayer, float cycleLength, Terrain terrain) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;
        this.layer = treeLayer;
        this.groundLayer = groundLayer;
        this.cycleLength = cycleLength;
        this.terrain = terrain;
    }

    /**
     * this method creates a single tree. it gets a random height and a random leaves width
     * and calculate how to set the tree. it sets it by the wood and leaf classes.
     * @param x the x coordinate to put the tree in the window
     */
    private void createSingleTree(float x){

        int blockHeight = random.nextInt(5) + 10; //height can be 10-15 blocks, width will be 1 block

        //create trunk
        float yCoor = terrain.groundHeightAt(x) - Block.SIZE * 2;
        for (int j = 0; j < blockHeight; j++) {
            Vector2 topLeftCorner = new Vector2(x, windowDimensions.y() - yCoor);
            Trunk trunk = new Trunk(topLeftCorner, gameObjects, layer, windowDimensions);
            yCoor += Block.SIZE;
        }

        //create leaves
        layer += 1;
        int LeavesBlocksWidth = (random.nextInt(3) + 2);
        float heightAtTrunkBlock = terrain.groundHeightAt(x) - Block.SIZE * 2;
        for(int row = -LeavesBlocksWidth; row < LeavesBlocksWidth + 1; row++){
            float yCoorLeaf = heightAtTrunkBlock + ((blockHeight - 1) * Block.SIZE);
            for(int col = 0; col < LeavesBlocksWidth * 2; col++){
                Vector2 topLeftCorner = new Vector2(x + (row * Block.SIZE), windowDimensions.y() -
                        yCoorLeaf + Block.SIZE);
                Leaf leaf = new Leaf(topLeftCorner, gameObjects, layer,windowDimensions, cycleLength);
                leaf.create();
                yCoorLeaf += Block.SIZE;
            }
        }
        gameObjects.layers().shouldLayersCollide(layer, groundLayer, true);
    }

    /**
     * this method creates the tree in a given x range
     * @param minX the x coordinate to start with
     * @param maxX the x coordinate to end
     */
    public void createInRange(int minX, int maxX){

        minX = getCloserInt(minX, true);
        maxX = getCloserInt(maxX, false);

        int amountOfBlocksBetweeenTrees;
        for(float x = minX; x < maxX; x += (amountOfBlocksBetweeenTrees * Block.SIZE)){
            amountOfBlocksBetweeenTrees = random.nextInt(3)+5;
            if(!random.nextBoolean())continue;
            createSingleTree(x);
        }
    }


    /**
     * this method gets the closer number which is shared in Block.Size
     * @param x the number to get closer
     * @param ifMin if we should get the lower number or the higher
     * @return the closer number
     */
    private int getCloserInt(int x, boolean ifMin){
        if(ifMin){
            while(!(x % Block.SIZE == 0))
                x--;
        }
        else{
            while(!(x % Block.SIZE == 0))
                x++;
        }
        return x;
    }

}

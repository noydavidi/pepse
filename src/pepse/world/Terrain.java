package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.PerlinNoise;

import java.awt.*;
import java.util.Random;

/**
 * this method represents the terrain in the simulation
 */
public class Terrain {

    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final Vector2 windowDimensions;
    private final double groundHeightAtX0;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);

    private static final Color[] colorsArray = {new Color(212, 123, 74),
                                                new Color(190, 123, 89),
                                                new Color(190, 123, 59)};

    private static final int TERRAIN_DEPTH = 20;
    private final PerlinNoise perlinNoiseObject;

    public Terrain(GameObjectCollection gameObjects, int groundLayer,
                   Vector2 windowDimensions, int seed){
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        this.groundHeightAtX0 = 7 * Block.SIZE;
        this.perlinNoiseObject = new PerlinNoise(seed);
    }

    /**
     * this method calculate and return y which represents the height of the ground in x
     * @param x the x coordinate on ground to calculate
     * @return the calculated height
     */
    public float groundHeightAt(float x){
        if(x == windowDimensions.x()/2) return (float)groundHeightAtX0;
        float pn = Math.abs(perlinNoiseObject.perlin((int)x));
        int a = (int)(groundHeightAtX0 - pn * Block.SIZE);
        a = getCloserInt(a, false);
        if(a < groundHeightAtX0) a += Block.SIZE;
        else a-= Block.SIZE;
        return a;
    }

    /**
     * this method creates in a range a number of trees
     * @param minX the minimum x to put a tree
     * @param maxX the maximum x to put a tree
     */
    public void createInRange(int minX, int maxX){
        minX = getCloserInt(minX, true);
        maxX = getCloserInt(maxX, false);
        int xSpace = maxX - minX;
        int amountOfBlocksInRow = xSpace / Block.SIZE + 1;
        for(int i = 0; i < amountOfBlocksInRow; i++){
            int amountOfBlocksInCol = (int)(groundHeightAt(minX + (i * Block.SIZE))) / Block.SIZE;
            for(int j = 0; j < amountOfBlocksInCol; j++){
                Renderable renderable = new RectangleRenderable(ColorSupplier.approximateColor(chooseRandomColor()));
                float xCoordinate = minX + i * Block.SIZE;
                float yCoordinate = windowDimensions.y() - j * Block.SIZE;
                GameObject block = Block.create(gameObjects,  groundLayer,
                        new Vector2(xCoordinate, yCoordinate), renderable);
                block.setTag("ground");
            }
        }

    }

    /**
     * this method choose a random color from the array
     * @return a color
     */
    private Color chooseRandomColor(){
        Random ran = new Random();
        int index = ran.nextInt(colorsArray.length);
        return colorsArray[index];
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

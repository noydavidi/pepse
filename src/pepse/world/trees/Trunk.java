package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;

public class Trunk {

    private static final Color WOOD_COLOR = new Color(100, 50, 20);

    private static final Color[] colorsArray = {new Color(100, 50, 20),
                                                new Color(110, 40, 20)};

    public Trunk(Vector2 topLeftCorner , GameObjectCollection gameObjects,
                 int layer, Vector2 windowDimensions) {
        Block.create(gameObjects, layer,
                topLeftCorner, new RectangleRenderable(chooseRandomColor()));
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

}

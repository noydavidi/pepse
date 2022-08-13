package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.trees.Tree;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class PepseGameManager extends GameManager {

//    private final Vector2 windowDimensions;
    private final float cycleLength = 10;

    public int SKY_LAYER = Layer.BACKGROUND;
    public int GROUND_LAYER = Layer.STATIC_OBJECTS;
    public int NIGHT_LAYER = Layer.FOREGROUND;
    public int SUN_LAYER = Layer.BACKGROUND + 1;
    public int SUN_HALO_LAYER = Layer.BACKGROUND + 10;
    public int TREE_LAYER = Layer.STATIC_OBJECTS + 1;
    public int AVATAR_LAYER = Layer.DEFAULT;

    private float currentCameraCenterX;
    private float currentMinX;
    private float currentMaxX;
    private Terrain terrain;
    private float space = 0;
    private Tree tree;

    private final Random random = new Random();
    private Avatar avatar;
    private UserInputListener inputListener;
    private WindowController windowController;
    private Vector2 windowDimensions;


    /**
     * The method will be called once when a GameGUIComponent is created, and again after every invocation
     * of windowController.resetGame().
     * @param imageReader Contains a single method: readImage, which reads an image from disk
     * @param soundReader Contains a single method: readSound, which reads a wav file from disk
     * @param inputListener Contains a single method: isKeyPressed, which returns whether a given key is currently pressed by the user or not
     * @param windowController Contains an array of helpful, self explanatory methods concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        int seed = 3;

        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();

        Sky.create(gameObjects(), windowDimensions, SKY_LAYER);
        Night.create(gameObjects(),NIGHT_LAYER, windowDimensions, cycleLength);


        this.terrain = new Terrain(gameObjects(), GROUND_LAYER, windowDimensions, seed);
        terrain.createInRange(0, (int)windowDimensions.x());
        this.currentMinX = 0;
        this.currentMaxX = windowDimensions.x();

        GameObject sun = Sun.create(gameObjects(), SUN_LAYER, windowDimensions, cycleLength);
        SunHalo.create(gameObjects(),SUN_HALO_LAYER, sun, new Color(255, 255, 0, 20));

        this.tree = new Tree(Vector2.ZERO, Vector2.ZERO,null, gameObjects(), windowDimensions, TREE_LAYER, GROUND_LAYER, cycleLength, terrain);
        tree.createInRange(0,(int) windowDimensions.x() + 1);

        Vector2 avatarTopLeftCorner = new Vector2(windowDimensions.x()/2, windowDimensions.y()
                - terrain.groundHeightAt(windowDimensions.x()/2));
        this.avatar = Avatar.create(gameObjects(), AVATAR_LAYER, avatarTopLeftCorner, inputListener, imageReader);

        float x = windowController.getWindowDimensions().mult(0.5f).x() - avatarTopLeftCorner.x();
        float y = windowController.getWindowDimensions().mult(0.5f).y() - avatarTopLeftCorner.y();
        setCamera(new Camera(avatar, new Vector2(x, y), windowController.getWindowDimensions(), windowController.getWindowDimensions()));
        this.currentCameraCenterX = avatar.getCenter().x();


    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if(inputListener.isKeyPressed(KeyEvent.VK_ESCAPE)) windowController.closeWindow();

        if(this.getCamera().getCenter().x() != currentCameraCenterX){
            space = (currentCameraCenterX - this.getCamera().getCenter().x()); //checking how much the camera moved
            if(space != 0) {
                if(space < 0){ //means avatar went right
                    terrain.createInRange((int)currentMaxX, (int)(currentMaxX - space));
                    if(random.nextInt(17) % 9 == 0) tree.createInRange((int)currentMaxX, (int)(currentMaxX - space));
                    currentMaxX += Block.SIZE;
//                    currentMinX += Block.SIZE;
                }
                else { //means avatar went left
                    terrain.createInRange((int)(currentMinX - space), (int)currentMinX);
                    if(random.nextInt(17) % 9 == 0) tree.createInRange((int)(currentMinX - space), (int)currentMinX);
                    currentMinX -= Block.SIZE;
//                    currentMaxX -= Block.SIZE;
                }
                for(GameObject gameObject: gameObjects().objectsInLayer(GROUND_LAYER)){ //deleting unnecessary game objects
                    if(gameObject.getCenter().x() > currentMaxX || gameObject.getCenter().x() < currentMinX) {
                        gameObjects().removeGameObject(gameObject);
                    }
                }
                space = 0;
            }
            currentCameraCenterX = this.getCamera().getCenter().x();
        }
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}

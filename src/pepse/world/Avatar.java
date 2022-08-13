package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.*;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * this class represents the avatar in the simulation
 */
public class Avatar extends GameObject {

    private final UserInputListener inputListener;
    private final ImageReader imageReader;
    private final TextRenderable numericEnergyRender;

    private static final Vector2 AVATAR_SIZE = new Vector2(40, 40);
    private static final float VELOCITY_Y = -80;
    private static final float VELOCITY_X = 200;
    private static final float GRAVITY = 100;
    private static final int ENERGY_CHANGE = 5;

    private int energyLevel;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, GameObjectCollection gameObjects, UserInputListener inputListener,
                  ImageReader imageReader) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        this.energyLevel = 100;

        this.numericEnergyRender = new TextRenderable("Your energy level: " + energyLevel);
        numericEnergyRender.setColor(Color.WHITE);
        GameObject numericLife = new GameObject(new Vector2(20, 20), new Vector2(17, 17), numericEnergyRender);
        gameObjects.addGameObject(numericLife, Layer.UI);
        numericLife.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    /**
     * this method create one single avatar gameobject
     * @param gameObjects the game objects collection
     * @param layer the layer on the simulation
     * @param topLeftCorner the coordinates to put the avatar on
     * @param inputListener object helps to read keyboard buttons
     * @param imageReader object helps to read an image
     * @return the avatar object
     */
    public static Avatar create(GameObjectCollection gameObjects,
                                int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader){

        Renderable avatarImg = imageReader.readImage("pepse/assets/avatar.png",true);
        Avatar avatar = new Avatar(topLeftCorner, AVATAR_SIZE, avatarImg, gameObjects, inputListener, imageReader);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        avatar.transform().setAccelerationY(GRAVITY);
//        avatar.physics().setMass(0);
        gameObjects.addGameObject(avatar, layer);
        return avatar;
    }

    /**
     * this method updates avatar every deltaTime, makes it to move, to jump, to change render
     * @param deltaTime the time to update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        int velX = 0;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            velX -= VELOCITY_X;
            this.renderer().setIsFlippedHorizontally(true);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            velX += VELOCITY_X;
            this.renderer().setIsFlippedHorizontally(false);
        }

        this.transform().setVelocityX(velX);

        if(inputListener.isKeyPressed(KeyEvent.VK_SHIFT) && inputListener.isKeyPressed(KeyEvent.VK_SPACE) && this.energyLevel != 0){
            this.transform().setVelocityY(VELOCITY_Y);
            this.energyLevel -= ENERGY_CHANGE;
        }

        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && this.getVelocity().y() == 0) {
            this.transform().setVelocityY(VELOCITY_Y);
        }

        if(this.getVelocity().x() == 0 && this.getVelocity().y() == 0){
            if(this.energyLevel != 100) this.energyLevel += ENERGY_CHANGE;
            Renderable avatarImg = imageReader.readImage("pepse/assets/avatar.png",true);
            this.renderer().setRenderable(avatarImg);
        }
        else if(this.getVelocity().x() != 0){
            Renderable runningAvatar = imageReader.readImage("pepse/assets/runningAvatar.png",true);
            this.renderer().setRenderable(runningAvatar);
        }

        else if(this.getVelocity().y() != 0){
            Renderable jumpingAvatar = imageReader.readImage("pepse/assets/jumpingAvatar.png",true);
            this.renderer().setRenderable(jumpingAvatar);
        }

        this.numericEnergyRender.setString("Your energy level: " + this.energyLevel);
    }

}

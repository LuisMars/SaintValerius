package es.luismars.Characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import es.luismars.Maps.GameMap;

import java.util.Map;

/**
 * Created by Dalek on 03/09/2015.
 */
public abstract class Movable extends Actor {

    float stateTime;
    float GRAVITY = -208;
    float MAX_SPEED = 96; // 48, 96
    float TERMINAL_SP = 128;
    float ACCELERATION = 128; // 48, 128
    float JUMP_SPEED = 148; //88, 148
    float LADDER_SPEED = MAX_SPEED / 2;

    int damageToTake = 0;

    Map<String, Animation> animations;
    String currentAnimation, lastAnimation;

    Rectangle[] hitBoxes, bounds;

    boolean isFlipped;
    public boolean isGrounded;
    boolean collidesWallLeft, collidesWallRight;

    public boolean isOnLadder;
    public boolean isAttacking;

    Vector2 position;
    Vector2 speed = new Vector2(0, 0);
    Vector2 acceleration = new Vector2(0, GRAVITY);


    GameMap gameMap;
    TiledMap map;
    TiledMapTileLayer blocks;

    @Override
    public void act(float delta) {
        applyPhysics(delta);
        checkCollisions();
        setPosition();
        updateAnimations(delta);

        super.act(delta);
    }

    public void updateAnimations(float delta) {
        stateTime += delta;
        flipSprite();
        lastAnimation = currentAnimation;
    }

    void applyPhysics(float delta) {

        if (!isOnLadder) {
            acceleration.y = GRAVITY;
        } else {
            acceleration.y = 0;
            speed.clamp(-LADDER_SPEED, LADDER_SPEED);
        }

        speed.mulAdd(acceleration, delta);
        if (!isGrounded) {
            speed.x = MathUtils.clamp(speed.x, -ACCELERATION * .66f, ACCELERATION * .66f);
            speed.x = MathUtils.clamp(speed.x, -MAX_SPEED * .66f, MAX_SPEED * .66f);
        } else {
            speed.x = MathUtils.clamp(speed.x, -MAX_SPEED, MAX_SPEED);
        }
        speed.y = Math.max(speed.y, -TERMINAL_SP);

        position.mulAdd(speed, delta);

        position.x = MathUtils.clamp(position.x, 0 - getOriginX(), gameMap.getWidth() - getOriginX());

    }

    void checkCollisions() {
        updateHitBoxes();
        isGrounded = isOnLadder = collidesWallLeft = collidesWallRight = false;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                checkCollision(i, j);
            }
        }
    }

    protected void createHitBoxes() {
        for (int i = 0, boundsLength = bounds.length; i < boundsLength; i++) {
            Rectangle r = bounds[i];
            hitBoxes[i] = new Rectangle((position.x + r.x), (position.y + r.y), r.width, r.height);
        }
    }

    void updateHitBoxes() {
        for (int i = 0, boundsLength = bounds.length; i < boundsLength; i++) {
            Rectangle r = bounds[i];
            hitBoxes[i].x = position.x + r.x;
            hitBoxes[i].y = position.y + r.y;
        }
    }

    void checkCollision(int a, int b) {
        //position to cell coordinates
        int x = MathUtils.floor((position.x + getOriginX()) / 16) + a;
        int y = Math.round((position.y + getOriginY()) / 16) + b;


        TiledMapTileLayer.Cell cell = blocks.getCell(x, y);
        boolean isLadder = gameMap.isLadder(cell);
        //get the code from the map
        int rectCode = gameMap.isCollidable(cell);

        if (rectCode != 0) {

            Rectangle[] rectangles = gameMap.getCollisionBoxes(x, y);
            if (rectangles == null) return;
            for (Rectangle r : rectangles) {
                if (r == null) continue;

                //BOTTOM
                if (r.overlaps(hitBoxes[3])) {
                    position.y = r.getY() + r.height;
                    speed.y = 0;
                    isGrounded = true;
                    damageToTake = gameMap.canDamage(cell);
                    updateHitBoxes();
                }

                //TOP
                if (r.overlaps(hitBoxes[0])) {
                    position.y = r.getY() - bounds[0].height - bounds[0].y;
                    speed.y = 0;
                    updateHitBoxes();
                }
                //LEFT
                if (r.overlaps(hitBoxes[1])) {
                    position.x = r.x + r.width - bounds[1].x;
                    speed.x = 0;
                    if (isLadder && !isGrounded && isFlipped) {
                        position.x--;
                        isOnLadder = true;
                    }
                    collidesWallLeft = true;
                    updateHitBoxes();
                }
                //RIGHT
                if (r.overlaps(hitBoxes[2])) {
                    position.x = r.x - bounds[2].width - bounds[2].x;
                    speed.x = 0;
                    if (isLadder && !isGrounded && !isFlipped) {
                        position.x++;
                        isOnLadder = true;
                    }
                    collidesWallRight = true;
                    updateHitBoxes();
                }
            }
        }
    }

    public void flipSprite() {
        if (speed.x > 0) {
            isFlipped = false;
        } else if (speed.x < 0) {
            isFlipped = true;
        }
    }

    public boolean collidesWith(Movable l) {
        for (Rectangle r : hitBoxes) {
            for (Rectangle rl : l.hitBoxes) {
                if (r.overlaps(rl)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void setPosition() {
        setPosition(position.x, position.y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getCenter() {
        return new Vector2(position.x + getOriginX(), position.y + getOriginY());
    }

    public void setMap(GameMap gameMap) {
        this.gameMap = gameMap;

        map = gameMap.getMap();

        blocks = ((TiledMapTileLayer) map.getLayers().get("front"));
    }

    @Override
    public void drawDebug(ShapeRenderer shapes) {
        for (Rectangle r : hitBoxes) {
            shapes.rect(r.x, r.y, r.width, r.height);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor());
        TextureRegion currentFrame = animations.get(currentAnimation).getKeyFrame(stateTime);
        currentFrame.flip(currentFrame.isFlipX() != isFlipped, false);
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
        batch.setColor(Color.WHITE);
    }
}

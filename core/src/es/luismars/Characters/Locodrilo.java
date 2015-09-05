package es.luismars.Characters;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import es.luismars.Maps.GameMap;
import es.luismars.Tools.AssetsLoader;

/**
 * Created by Dalek on 20/08/2015.
 */
public class Locodrilo extends MovableCharacter {

    SanValero sanValero;
    int attackDistance = 24;
    boolean preparingAttack = false;
    float attackDelay = 0.5f;
    float attackDelayTimer = 0;

    public Locodrilo(GameMap map) { this(map.startPos().x, map.startPos().y, map);}

    public Locodrilo(float x, float y, GameMap gameMap) {
        super(x, y, gameMap);

        setMap(gameMap);

        animations = AssetsLoader.Locodrilo;
        currentAnimation = "walk";

        stateTime = MathUtils.random();

        bounds[0] = new Rectangle(12, 26, 8, 2);
        bounds[1] = new Rectangle(6, 8, 10, 18);
        bounds[2] = new Rectangle(16, 8, 10, 18);
        bounds[3] = new Rectangle(10, 0, 12, 8);
        createHitBoxes();

        MAX_SPEED = 40;


        health = MAX_HEALTH = 3;
    }

    @Override
    public void updateAnimations(float delta) {
        stateTime += delta;
        flipSprite();
        lastAnimation = currentAnimation;

        if (isAttacking) {

            currentAnimation = "attack";
            if (!currentAnimation.equals(lastAnimation)) {
                stateTime = 0;
                speed.x = 0;
            } else {
                int lastFrame = animations.get(currentAnimation).getKeyFrameIndex(stateTime - delta);
                int curFrame = animations.get(currentAnimation).getKeyFrameIndex(stateTime);
                if (lastFrame == 1 && curFrame == 0) {
                    isAttacking = false;
                    currentAnimation = "walk";
                }
            }
        } else {
            currentAnimation = "walk";
        }
        //System.out.println(currentAnimation);
    }

    @Override
    public void attack() {
        if (!preparingAttack) {
            attackDelayTimer = 0;
            preparingAttack = true;
        }
    }

    @Override
    public void act(float delta) {
        if (preparingAttack) {
            attackDelayTimer += delta;
            if (attackDelayTimer >= attackDelay) {
                isAttacking = true;
                preparingAttack = false;
                attackDelayTimer = 0;
            }
        }

        super.act(delta);

        if (position.y < 0) {
            remove();
            return;
        }

        if (hurtTimer == 0) {
            if (isGrounded && !isAttacking) {
                if (collidesWallLeft) {
                    //position.x += 16;
                    speed.x = MAX_SPEED;
                } else if (collidesWallRight) {
                    speed.x = -MAX_SPEED;
                    //position.x -= 16;
                } else if (speed.x == 0){
                    speed.x = MAX_SPEED * MathUtils.random(-1, 1);
                }
            }
            if (!(isOnFloor(0) || isOnFloor(-1))) {
                speed.x *= -1;
            }
        }

    }




    public boolean isOnFloor(int offY) {
        int x = MathUtils.floor((position.x + getOriginX()) / 16);
        int y = MathUtils.round((position.y + getOriginY()) / 16) + offY;
        Rectangle[] boxes = gameMap.getCollisionBoxes(x, y);
        Rectangle lowSensor = new Rectangle(position.x + getOriginX() - 4, position.y - 8, 8, 8);

        if (boxes == null) {
            return false;
        }

        for (Rectangle r : boxes) {

            if (r != null && r.overlaps(lowSensor)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void applyDamage(int i) {
        super.applyDamage(i);

        if (health <= 0) {
            getParent().addActor(new Drop(position.cpy().add(getOriginX(), getOriginY()), gameMap));
            remove();
        }
    }

    @Override
    public void drawDebug(ShapeRenderer shapes) {
        super.drawDebug(shapes);
        int x = MathUtils.floor((position.x + getOriginX()) / 16);
        int y = MathUtils.round((position.y + getOriginY()) / 16) - 1;
        Rectangle[] boxes = gameMap.getCollisionBoxes(x, y);


        for (Rectangle r : boxes) {
            if (r != null) {
                shapes.rect(r.x, r.y, r.width, r.height);
            }
        }
        Rectangle lowSensor = new Rectangle(position.x + getOriginX() - 4, position.y - 8, 8, 8);

        shapes.rect(lowSensor.x, lowSensor.y, lowSensor.width, lowSensor.height);
    }

    @Override
    public void flipSprite() {
        if (!isAttacking || !preparingAttack) {
            super.flipSprite();
        }
    }

    public void checkDistance(SanValero sanValero) {
        if (sanValero.getCenter().dst(getCenter()) < attackDistance) {
            isFlipped = sanValero.getCenter().x < getCenter().x;
            attack();
        }
    }
}

package es.luismars.Characters;

import com.badlogic.gdx.Gdx;
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
import es.luismars.Tools.AssetsLoader;
import es.luismars.Maps.GameMap;
import es.luismars.Tools.SaveGame;
import es.luismars.Tools.Shake;

import java.util.Map;

/**
 * Created by Dalek on 15/07/2015.
 */
public abstract class MovableCharacter extends Movable {

    Rectangle attackBox;

    float stateTime;
    float GRAVITY = -208;
    float MAX_SPEED = 96; // 48, 96
    float TERMINAL_SP = 128;
    float ACCELERATION = 128; // 48, 128
    float JUMP_SPEED = 128; //88, 148
    float LADDER_SPEED = MAX_SPEED / 2;


    int health = 10;
    int MAX_HEALTH = 10;


    public MovableCharacter(GameMap gameMap) {
        this(gameMap.startPos().x, gameMap.startPos().y, gameMap);
    }

    public MovableCharacter(float x, float y, GameMap gameMap) {
        position = new Vector2(x, y);
        setSize(AssetsLoader.size, AssetsLoader.size);
        setOrigin(AssetsLoader.size / 2, 0);

        hitBoxes = new Rectangle[4];
        bounds = new Rectangle[4];
        attackBox = new Rectangle(x, y, getWidth(), getHeight() * 2/3f);
        this.gameMap = gameMap;
    }


    public void moveX(float speed) {
        if (!isOnLadder)
            acceleration.x = speed * ACCELERATION;
    }

    public void stopX() {
        acceleration.x = 0;
        speed.x = 0;
    }

    public void jump() {
        if (isGrounded) {
            speed.y = JUMP_SPEED;
        } else if (isOnLadder) {
            speed.y = LADDER_SPEED;
        }
    }

    public void stopJump() {
        if (speed.y > 0 || isOnLadder) {
            speed.y /= 2;
        }
    }

    public void down() {
        if (isOnLadder) {
            speed.y = -LADDER_SPEED;
        } else {
            for (int j = -1; j <= 1; j++) {
                int x = ((int) (position.x / 16)) + 1;
                int y = Math.round(position.y / 16) + j;
                gameMap.useCell(x, y, (SanValero) this);
            }
        }
    }

    public void attack() {
        isAttacking = true;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateHurtTimer(delta);
        if (damageToTake != 0) {
            applyDamage(damageToTake);
            damageToTake = 0;
        }
    }

    public void updateHurtTimer(float delta) {
        float oldtimer = hurtTimer;
        hurtTimer = Math.max(hurtTimer -delta, 0);
        if (oldtimer != 0 && hurtTimer == 0) {
            speed.x = 0;
        }
    }


    public void updateAnimations(float delta) {
        stateTime += delta;
        flipSprite();
        lastAnimation = currentAnimation;
        if (isAttacking) {
            currentAnimation = "attack";
            if (!lastAnimation.equals(currentAnimation)) {
                stateTime = 0;
            }
        } else if (isGrounded && lastAnimation.equals("jump-down")) {
            currentAnimation = "jump-land";
        } else if (speed.x == 0 && speed.y == 0 && (isGrounded || isOnLadder)) {
            if (!isOnLadder) {
                currentAnimation = "idle";
                if (lastAnimation.equals(currentAnimation)) {
                    stateTime = 0;
                }
            }
            else if (lastAnimation.equals("ladder"))
                stateTime -= delta;
        } else if (isOnLadder) {
            currentAnimation = "ladder";
        } else if (speed.x != 0 && speed.y == 0) {
            currentAnimation = "walk";
        } else {
            if (currentAnimation.equals("jump-up")
                    && animations.get(currentAnimation).isAnimationFinished(stateTime)) {
                currentAnimation = "jump-down";
            } else if (!currentAnimation.startsWith("jump")) {
                currentAnimation = "jump-up";
            } else {
                currentAnimation = "jump-down";
            }
        }

        if (lastAnimation.equals("jump-land") && currentAnimation.equals("idle") &&
                !animations.get(lastAnimation).isAnimationFinished(stateTime)) {
            currentAnimation = "jump-land";
        }

    if (currentAnimation.equals("attack") && animations.get(currentAnimation).isAnimationFinished(stateTime)) {
        isAttacking = false;
    }

    if(!lastAnimation.equals(currentAnimation)) {
        stateTime = 0;
    }
        //Gdx.graphics.setTitle("San Valero: From under the Sèquia (pre-Alpha) " + currentAnimation + " " + stateTime);
}



    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (hurtTimer == 0) {
            setColor(1, 1, 1, parentAlpha);
        } else {
            float alpha = 1 - (hurtTimer / hurtTime);
            setColor(1, alpha, alpha, 1);
        }
        batch.setColor(getColor());
        TextureRegion currentFrame = animations.get(currentAnimation).getKeyFrame(stateTime);
        currentFrame.flip(currentFrame.isFlipX() != isFlipped, false);
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
        batch.setColor(Color.WHITE);
    }

    @Override
    public void drawDebug(ShapeRenderer shapes) {

        float v = health / ((float) MAX_HEALTH);
        shapes.setColor(v > 0.5f ? Color.GREEN : v > 0.25f ? Color.YELLOW : Color.RED);

        for (Rectangle r : hitBoxes) {
            shapes.rect(r.x, r.y, r.width, r.height);
        }

        shapes.rect(attackBox.x, attackBox.y, attackBox.width, attackBox.height);

    }

    public void setPosition() {
        super.setPosition();
        attackBox.setPosition(position);
    }

    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    public int getHealth() {
        return health;
    }

    float hurtTimer;
    float hurtTime = 1;

    public void applyDamage(int i) {
        //if (isAttacking) return;
        if (hurtTimer == 0) {
            health -= i;
            hurtTimer = hurtTime;
            speed.y /= 2;
            speed.x /= 2;
            Shake.shake(0.25f * i);
        }
    }



    public boolean collidesAttack(MovableCharacter l) {
        if (isAttacking) {
            for (Rectangle rl : l.hitBoxes) {
                if (attackBox.overlaps(rl)) {
                    return true;
                }
            }
        }
        return false;
    }


}

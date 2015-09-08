package es.luismars.Characters;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import es.luismars.Maps.GameMap;

/**
 * Created by Dalek on 07/09/2015.
 */
public class FireLocodrilo extends Locodrilo {

    public FireLocodrilo(float x, float y, GameMap gameMap) {
        super(x, y, gameMap);

        attackDelay = 1.5f;
        currentAnimation = "launch-fire";
    }

    @Override
    public void behaviour() {
        if (isAttacking && animations.get(currentAnimation).getKeyFrameIndex(stateTime) == 4) {
            isAttacking = false;
            getParent().addActor(new Bullet(gameMap, getCenter().cpy().add(0, 16), !isFlipped));
        }
    }

    @Override
    public void updateAnimations(float delta) {
        stateTime += delta;
        flipSprite();
        lastAnimation = currentAnimation;

        if (animations.get(currentAnimation).getKeyFrameIndex(stateTime) == 0) {
            isAttacking = true;
            stateTime -= delta * 0.66f;
        }

        if (!currentAnimation.equals(lastAnimation)) {
            stateTime = 0;
        }

    }

    @Override
    public void checkDistance(SanValero sanValero) {
        /*if (MathUtils.isEqual(sanValero.getY(), getY(), 32)) {
            attack();
        }*/

        isFlipped = sanValero.getX() < getX();
    }
}

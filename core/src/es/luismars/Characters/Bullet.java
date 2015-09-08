package es.luismars.Characters;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import es.luismars.Maps.GameMap;
import es.luismars.Tools.AssetsLoader;

/**
 * Created by Dalek on 07/09/2015.
 */
public class Bullet extends Movable {


    public Bullet(GameMap map, Vector2 position, boolean right) {
        this.position = position;
        acceleration.y = 0;
        GRAVITY = 0;
        bounds = new Rectangle[4];
        hitBoxes = new Rectangle[4];

        setMap(map);
        setSize(16, 16);
        setOrigin(8, 8);

        animations = AssetsLoader.Fireball;
        currentAnimation = "move";

        bounds[0] = new Rectangle(9, 10, 6, 2);
        bounds[1] = new Rectangle(8, 6, 4, 4);
        bounds[2] = new Rectangle(12, 6, 4, 4);
        bounds[3] = new Rectangle(9, 4, 6, 2);

        createHitBoxes();


        MAX_SPEED = 192; // 48, 96
        speed.x = right ? MAX_SPEED : -MAX_SPEED;
    }

    @Override
    public void act(float delta) {

        super.act(delta);

        if (position.x < 0 || position.x >= gameMap.getWidth() - 16) {
            remove();
        }
    }


    @Override
    void updateHitBoxes() {

        for (int i = 0, boundsLength = bounds.length; i < boundsLength; i++) {
            Rectangle r = bounds[i];

            hitBoxes[i].x = position.x + (isFlipped ? 0 : r.x);
            hitBoxes[i].y = position.y + r.y;
        }
    }

    public boolean collidesWithEnvoirement() {
        return collidesWallLeft || collidesWallRight || isGrounded;
    }
}

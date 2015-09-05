package es.luismars.Characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import es.luismars.Maps.GameMap;
import es.luismars.Tools.AssetsLoader;

/**
 * Created by Dalek on 03/09/2015.
 */
public class Drop extends Movable {

    float catchTimer = 0;
    float catchDuration = 1;

    public Drop(Vector2 position, GameMap map) {
        this.position = position;
        this.speed = new Vector2(MathUtils.random(-40, 40), MathUtils.random(96, 128));

        setMap(map);

        setSize(8, 8);
        setOrigin(0, 0);

        bounds = new Rectangle[4];
        hitBoxes = new Rectangle[4];

        bounds[0] = new Rectangle(2, 6, 4, 2);
        bounds[1] = new Rectangle(0, 2, 4, 4);
        bounds[2] = new Rectangle(4, 2, 4, 4);
        bounds[3] = new Rectangle(2, 0, 4, 2);

        createHitBoxes();

        animations = AssetsLoader.Drops;
        currentAnimation = "heart";

    }

    @Override
    public void act(float delta) {
        catchTimer += delta;

        setColor(new Color(1, 1, 1, catchTimer/catchDuration));
        super.act(delta);
    }

    public boolean canBeCaught() {
        if (catchTimer < catchDuration) {
            return false;
        } else {
            catchTimer = catchDuration;
            return true;
        }
    }

    @Override
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
                    speed.y *= -0.75f;
                    speed.x *= 0.75f;
                    isGrounded = true;
                    updateHitBoxes();
                }

                //TOP
                if (r.overlaps(hitBoxes[0])) {
                    position.y = r.getY() - hitBoxes[0].height - bounds[0].y;
                    speed.y = 0;
                    updateHitBoxes();
                }
                //LEFT
                if (r.overlaps(hitBoxes[1])) {
                    position.x = r.x + r.width - bounds[1].x;
                    speed.x *= -0.75f;
                    if (isLadder && !isGrounded && isFlipped) {
                        position.x--;
                        isOnLadder = true;
                    }
                    collidesWallLeft = true;
                    updateHitBoxes();
                }
                //RIGHT
                if (r.overlaps(hitBoxes[2])) {
                    position.x = r.x - hitBoxes[2].width - bounds[2].x;
                    speed.x *= -0.75f;
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
}

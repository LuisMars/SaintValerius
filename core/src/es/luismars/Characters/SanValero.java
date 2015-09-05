package es.luismars.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import es.luismars.Tools.AssetsLoader;
import es.luismars.Maps.GameMap;
import es.luismars.Tools.SaveGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dalek on 15/07/2015.
 */
public class SanValero extends MovableCharacter {
    public List<Integer> currentLevelKeys = new ArrayList<Integer>();;

    public SanValero (GameMap map) {
        this(map.startPos().x, map.startPos().y, map);
    }

    public SanValero (float x, float y, GameMap map) {
        super(x, y, map);

        setMap(gameMap);

        health = SaveGame.getStat("health", MAX_HEALTH);

        animations = AssetsLoader.SanValero;
        currentAnimation = "jump-down";

        bounds[0] = new Rectangle(14, 19, 4, 2);
        bounds[1] = new Rectangle(12, 4, 4, 15);
        bounds[2] = new Rectangle(16, 4, 4, 15);
        bounds[3] = new Rectangle(14, 0, 4, 4);
        createHitBoxes();


        int tpDoor = SaveGame.getStat("tpDoor", 0);
        setLevel(map.doorPos(tpDoor), map);


    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (position.y < 0) {
            gameMap.restart();
        }

        int x = ((int) (position.x / 16)) + 1;
        int y = Math.round(position.y / 16) + 1;
        gameMap.checkAuto(x, y, this);

    }
    public void setLevel(Vector2 pos, GameMap gameMap) {
        speed = Vector2.Zero.cpy();
        acceleration.x = 0;
        acceleration.y = GRAVITY;
        position = pos.cpy();

        setMap(gameMap);
    }

    public void changeLevel(Vector2 pos, GameMap gameMap) {
        speed = Vector2.Zero.cpy();
        acceleration.x = 0;
        acceleration.y = GRAVITY;
        position = pos.cpy();
        health = SaveGame.getStat("health", MAX_HEALTH);
        setMap(gameMap);
    }


    @Override
    public void drawDebug(ShapeRenderer shapes) {
        super.drawDebug(shapes);
        shapes.setColor(Color.RED);
        gameMap.drawBlockRectangles(shapes);
    }

    public void addKey(int level, int key) {
        SaveGame.addItem("keys", level, key);
    }

    @Override
    public void applyDamage(int i) {
        if (isAttacking) return;
        super.applyDamage(i);
        if (health <= 0) {
            gameMap.restart();
        }
    }

    public void loadStats() {
        try {
            health = SaveGame.getStat("health", MAX_HEALTH);
        } catch (NullPointerException ignored) {}
    }

    public boolean addHealth(int i) {
        if (health < MAX_HEALTH) {
            health += i;
            return true;
        }
        else
            return false;
    }
}

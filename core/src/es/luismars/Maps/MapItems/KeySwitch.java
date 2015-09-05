package es.luismars.Maps.MapItems;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.math.Vector2;
import es.luismars.Tools.SaveGame;

import java.util.Hashtable;

/**
 * Created by Dalek on 04/08/2015.
 */
public class KeySwitch {

    int doorID;
    int level;
    int id;
    int keyID;
    TiledMapTileLayer.Cell cell;
    boolean toggled;
    Door door;
    TiledMapTile[] switchLever;

    Vector2 position;

    Hashtable<Integer, Door> doors;

    public KeySwitch(int level, int id, int doorID, int keyID, Vector2 position, Hashtable<Integer, Door> doors, TiledMapTileLayer tiles, TiledMapTileSets tileSets) {
        this.id = id;
        this.keyID = keyID;
        this.level = level;
        this.doorID = doorID;
        this.position = position;
        this.doors = doors;

        switchLever = new TiledMapTile[2];

        switchLever[0] = tileSets.getTile(107);
        switchLever[1] = tileSets.getTile(123);

        cell = tiles.getCell(((int) position.x), ((int) position.y));

        if (SaveGame.containsItem("keySwitches", level, id)) {
            cell.setTile(switchLever[1]);
            toggled = true;
        }

    }

    public void toggle() {
        if (SaveGame.containsItem("keys", level, keyID)) {
            door = doors.get(doorID);
            door.toggle();

            cell.setTile(switchLever[1]);
            SaveGame.addItem("keySwitches", level, id);
        }
    }
}

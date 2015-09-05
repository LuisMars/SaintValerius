package es.luismars.Maps.MapItems;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.math.Vector2;
import es.luismars.Tools.SaveGame;

import java.util.Hashtable;

/**
 * Created by Dalek on 22/07/2015.
 */
public class Switch {

    int doorID;
    int level;
    int id;
    TiledMapTileLayer.Cell cell;
    boolean toggled;
    Door door;
    TiledMapTile[] switchLever;

    Vector2 position;

    Hashtable<Integer, Door> doors;

    public Switch(int level, int id, int doorID, Vector2 position, Hashtable<Integer, Door> doors, TiledMapTileLayer tiles, TiledMapTileSets tileSets) {
        this.id = id;
        this.level = level;
        this.doorID = doorID;
        this.position = position;
        this.doors = doors;

        switchLever = new TiledMapTile[2];

        switchLever[0] = tileSets.getTile(26);
        switchLever[1] = tileSets.getTile(27);

        cell = tiles.getCell(((int) position.x), ((int) position.y));

        if (SaveGame.containsItem("switches", level, id)) {
            cell.setTile(switchLever[1]);
            toggled = true;
        } else {
            cell.setTile(switchLever[0]);
            toggled = false;
        }

    }

    public void toggle() {
        door = doors.get(doorID);
        door.toggle();
        toggled = !toggled;
        if (toggled) {
            cell.setTile(switchLever[1]);
            SaveGame.addItem("switches", level, id);
        }
        else {
            cell.setTile(switchLever[0]);
            SaveGame.removeItem("switches", level, id);
        }


    }
}

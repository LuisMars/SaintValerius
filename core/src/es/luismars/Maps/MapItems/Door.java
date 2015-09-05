package es.luismars.Maps.MapItems;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.math.Vector2;
import es.luismars.Tools.SaveGame;

/**
 * Created by Dalek on 22/07/2015.
 */
public class Door {

    int level;
    int id;
    boolean open;
    int keyID;
    int tpLevel;
    int tpDoor;
    Vector2 position;
    TiledMapTileLayer tiles;
    TiledMapTileSets tileSets;

    TiledMapTile[] up, down;
    TiledMapTileLayer.Cell[] cells;



    public Door(int level, int id, boolean open, int tpLevel, int tpDoor, int keyID, Vector2 position, TiledMapTileLayer tiles, TiledMapTileSets tileSets) {

        this.level = level;
        this.id = id;
        this.open = open;

        this.tpLevel = tpLevel;
        this.tpDoor = tpDoor;

        this.keyID = keyID;

        this.position = position;
        this.tiles = tiles;
        this.tileSets = tileSets;

        cells = new TiledMapTileLayer.Cell[2];

        cells[0] = tiles.getCell(((int) position.x), ((int) position.y + 1));
        cells[1] = tiles.getCell(((int) position.x), ((int) position.y));

        up = new TiledMapTile[2];
        down = new TiledMapTile[2];

        up[0] = tileSets.getTile(101); //Closed
        up[1] = tileSets.getTile(103); //Opened
        down[0] = tileSets.getTile(117);
        down[1] = tileSets.getTile(119);

        if (open)
            open();
        else if (keyID == 0)
            close();
    }

    public void toggle() {
        if (keyID == 0 || SaveGame.containsItem("keys", level, keyID)) {
            open = !open;
            if (open) {
                open();
                SaveGame.addItem("doors", level, id);
            } else {
                close();
                SaveGame.removeItem("doors", level, id);
            }
        }
    }

    private void open() {
        cells[0].setTile(up[1]);
        cells[1].setTile(down[1]);

    }

    private void close() {
        cells[0].setTile(up[0]);
        cells[1].setTile(down[0]);
    }

    public int getID() {
        return id;
    }

    public Vector2 getPosition() {
        return position.cpy().scl(16, 16);
    }

    public Vector2 getPositionInCells() {
        return position.cpy();
    }

    public int getTpLevel() {
        return tpLevel;
    }

    public int getTpDoor() {
        return tpDoor;
    }

    public boolean isOpen() {
        return open;
    }

    public int getKey() {
        return keyID;
    }
}

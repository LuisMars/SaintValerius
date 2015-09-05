package es.luismars.Maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import es.luismars.Characters.Drop;
import es.luismars.Characters.Locodrilo;
import es.luismars.Characters.SanValero;
import es.luismars.Maps.MapItems.KeySwitch;
import es.luismars.Tools.AssetsLoader;
import es.luismars.Maps.MapItems.Door;
import es.luismars.Maps.MapItems.Switch;
import es.luismars.Stages.GameStage;
import es.luismars.Tools.SaveGame;

import java.util.*;

/**
 * Created by Dalek on 22/07/2015.
 */
public class GameMap {

    Rectangle [][][] rectangles;

    MapRenderer mapRenderer;
    TiledMap tiledMap;
    OrthographicCamera camera;
    GameStage gameStage;

    TiledMapTileSets tileSets;

    TiledMapTileLayer topLayer;
    TiledMapTileLayer frontLayer;
    TiledMapTileLayer frontDetailLayer;
    TiledMapTileLayer backLayer;

    MapLayer objectLayer;

    TiledMapTile[] chest;

    Hashtable<Integer, Door> doors;
    Hashtable<Vector2, Switch> switches;
    Hashtable<Vector2, Integer> keys;
    Hashtable<Vector2, KeySwitch> keySwitches;

    int level;

    int[] bottomLayers = new int[] {0, 1};
    int[] topLayers = new int[] {2};

    int[] fireAnimation = new int[] {40, 41, 42};
    int[] pipeAnimation = new int[] {7, 23, 39};
    int[][] waterfall = new int[][] {{29, 30, 31, 32}, {45, 46, 47, 48}, {61, 62, 63, 64}, {77, 78, 79, 80}};
    int[][][] water = new int[][][] {
            {{209, 210, 211, 212}, {225, 226, 227, 228}},
            {{145, 146, 147, 148}, {161, 162, 163, 164}},
            {{177, 178, 179, 180}, {193, 194, 195, 196}}};


    public GameMap() {}
    public GameMap(GameStage gameStage) {
        this.gameStage = gameStage;

        camera = (OrthographicCamera) gameStage.getCamera();
        SaveGame.load();
        int level = SaveGame.getStat("level", 0);

        setupMap(level);
        //changeStage(tpDoor);
        //gameStage.sanValero.changeLevel(doorPos(tpDoor), this);

    }

    public void restart() {
        SaveGame.load();
        setupMap(level);
        int tpDoor = SaveGame.getStat("tpDoor", 0);
        gameStage.sanValero.changeLevel(doorPos(tpDoor), this);

    }

    public void setupMap(int level) {
        //tiledMap = new TmxMapLoader().load("notlevel.tmx");
        this.level = level;
        tiledMap = new TmxMapLoader().load("level" + level + ".tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1, gameStage.getBatch());
        mapRenderer.setView(camera);

        try {
            gameStage.sanValero.loadStats();
        } catch (NullPointerException ignored) {}

        tileSets = tiledMap.getTileSets();

        topLayer = (TiledMapTileLayer) tiledMap.getLayers().get("top");
        frontLayer = (TiledMapTileLayer) tiledMap.getLayers().get("front");
        backLayer = (TiledMapTileLayer) tiledMap.getLayers().get("back");

        loadObjects();
        loadAnimations();
        loadEnemies();
        generateRectangles();

    }

    private void loadEnemies() {
        gameStage.enemies.clear();
        MapLayer enemyLayer = tiledMap.getLayers().get("enemies");
        Array<RectangleMapObject> enemies = enemyLayer.getObjects().getByType(RectangleMapObject.class);

        for (RectangleMapObject enemy : enemies) {
            Rectangle r = enemy.getRectangle();
            gameStage.enemies.addActor(new Locodrilo(r.x, r.y, this));
        }
    }

    private void loadAnimations() {

        chest = new TiledMapTile[2];
        chest[0] = tileSets.getTile(88);
        chest[1] = tileSets.getTile(74);

        setAnimatedTiles(fireAnimation, AssetsLoader.time * 2, "fire", frontLayer);
        setAnimatedTiles(pipeAnimation, AssetsLoader.time * 4, "pipe", frontLayer);

        for (int i = 0; i < waterfall.length; i++) {
            setAnimatedTiles(waterfall[i], AssetsLoader.time * 1.5f, "waterfall"+i, topLayer);
        }
        //System.out.println(water.length + " " + water[1].length);
        for (int i = 0; i < water.length; i++) {
            for (int j = 0; j < water[i].length; j++) {
                setAnimatedTiles(water[i][j], AssetsLoader.time * 3, "water"+i+""+j, topLayer);
            }
        }
    }

    protected void loadObjects() {
        objectLayer = tiledMap.getLayers().get("objects");

        Array<RectangleMapObject> objects = objectLayer.getObjects().getByType(RectangleMapObject.class);
        switches = new Hashtable<Vector2, Switch>();
        doors = new Hashtable<Integer, Door>();
        keys = new Hashtable<Vector2, Integer>();
        keySwitches = new Hashtable<Vector2, KeySwitch>();

        for (RectangleMapObject object : objects) {
            MapProperties properties = object.getProperties();
            String s = object.getName();
            if (s.equals("door")) {
                addDoor(object, properties);

            } else if (s.equals("switch")) {
                addSwitch(object, properties);

            } else if (s.equals("key")) {
                addKey(object, properties);
            } else if (s.equals("keySwitch")) {
                addKeySwitch(object, properties);
            }
        }
    }

    private void addKeySwitch(RectangleMapObject object, MapProperties properties) {
        int id = Integer.parseInt(properties.get("id", String.class));
        Rectangle r = object.getRectangle();
        int doorId = Integer.parseInt(properties.get("doorID", String.class));
        int key = Integer.parseInt(properties.get("key", String.class));
        Vector2 pos = new Vector2((int) (r.x / 16), (int) (r.y / 16));

        keySwitches.put(pos, new KeySwitch(level, id, doorId, key, pos, doors, frontLayer, tileSets));

    }

    private void addKey(RectangleMapObject object, MapProperties properties) {
        int id = Integer.parseInt(properties.get("id", String.class));
        Rectangle r = object.getRectangle();
        if (!SaveGame.containsItem("keys", level, id)) {
            Vector2 pos = new Vector2((int) (r.x / 16), (int) (r.y / 16));
            keys.put(pos, id);
        } else {
            frontLayer.setCell((int) (r.x / 16), (int) (r.y / 16), null);
        }
    }
    private void addSwitch(RectangleMapObject object, MapProperties properties) {
        int id = Integer.parseInt(properties.get("id", String.class));
        int doorId = Integer.parseInt(properties.get("doorID", String.class));
        Rectangle r = object.getRectangle();
        Vector2 pos = new Vector2((int)(r.x / 16), (int) (r.y / 16));

        switches.put(pos, new Switch(level, id, doorId, pos, doors, frontLayer, tileSets));
    }
    private void addDoor(RectangleMapObject object, MapProperties properties) {
        int id = Integer.parseInt(properties.get("id", String.class));
        boolean open = Boolean.parseBoolean(properties.get("open", String.class));
        int tpLevel = Integer.parseInt(properties.get("tpLevel", String.class));
        int tpDoor = Integer.parseInt(properties.get("tpDoor", String.class));
        int key = Integer.parseInt(properties.get("key", String.class));
        Rectangle r = object.getRectangle();
        Vector2 pos = new Vector2((int)(r.x / 16), (int) (r.y / 16));

        if (SaveGame.containsItem("doors", level, id)) {
            open = !open;
            //frontLayer.setCell((int) (r.x / 16), (int) (r.y / 16), null);
        }

        doors.put(id, new Door(level, id, open, tpLevel, tpDoor, key, pos, backLayer, tileSets));
    }
    protected void setAnimatedTiles(int[] tiles, float interval, String name, TiledMapTileLayer tileLayer) {
        AnimatedTiledMapTile animatedTile = getAnimatedTile(tiles, interval);

        for (int i = 0; i < tileLayer.getWidth(); i++) {
            for (int j = 0; j < tileLayer.getHeight(); j++) {
                putAnimation(name, animatedTile, i, j, tileLayer);
            }
        }
    }
    protected AnimatedTiledMapTile getAnimatedTile(int[] tiles, float interval) {
        Array<StaticTiledMapTile> tileArray = new Array<StaticTiledMapTile>();

        for (int i : tiles) {
            tileArray.add(((StaticTiledMapTile) tiledMap.getTileSets().getTileSet(0).getTile(i)));
        }

        return new AnimatedTiledMapTile(interval, tileArray);
    }
    protected void putAnimation(String name, AnimatedTiledMapTile pipeTiles, int i, int j, TiledMapTileLayer tileLayer) {
        TiledMapTileLayer.Cell cell = tileLayer.getCell(i, j);
        if (cell != null && cell.getTile().getProperties().containsKey("animation")
            && cell.getTile().getProperties().get("animation").equals(name)) {

            MapProperties properties = cell.getTile().getProperties();
            cell.setTile(pipeTiles);
            cell.getTile().getProperties().putAll(properties);
        }
    }
    public void useCell(int x, int y, SanValero character) {
        checkFrontLayer(x, y, character);
        checkBackLayer(x, y, character);
    }

    private void checkBackLayer(int x, int y, SanValero character) {
        TiledMapTileLayer.Cell cell = backLayer.getCell(x, y);
        Vector2 pos = new Vector2(x, y);
        if (cell == null) return;
        switch (cell.getTile().getId()) {
            //Door
            case 119:
                for (Door door : doors.values()) {
                    Vector2 doorPos = door.getPositionInCells();
                    if (pos.equals(doorPos) && gameStage.sanValero.isGrounded) {
                        int level = door.getTpLevel();
                        SaveGame.setStat("health", gameStage.sanValero.getHealth());
                        setupMap(level);
                        int tpDoor = door.getTpDoor();
                        changeStage(tpDoor);
                        saveLastDoor(level, tpDoor);
                        SaveGame.save();
                        break;
                    }
                }
                break;
            case 118:
                for (Door door : doors.values()) {
                    if (door.getPositionInCells().equals(pos)) {
                        door.toggle();
                        break;
                    }
                }
                break;
        }
    }

    private void saveLastDoor(int level, int tpDoor) {
        SaveGame.setStat("level", level);
        SaveGame.setStat("tpDoor", tpDoor);
    }

    private void changeStage(int tpDoor) {
        gameStage.changeLevel(doorPos(tpDoor), this);
    }

    private void checkFrontLayer(int x, int y, SanValero character) {
        TiledMapTileLayer.Cell cell = frontLayer.getCell(x, y);
        Vector2 pos = new Vector2(x, y);
        if (cell == null) return;

        switch (cell.getTile().getId()) {

            //Switch lever
            case 26:
                frontLayer.setCell(x, y, cell);
                switches.get(pos).toggle();
                break;
            case 27:
                frontLayer.setCell(x, y, cell);
                switches.get(pos).toggle();
                break;
            case 107:
                keySwitches.get(pos).toggle();
                break;
            //Chest
            case 88:
                cell.setTile(chest[1]);
                frontLayer.setCell(x, y, cell);
                gameStage.enemies.addActor(new Drop(new Vector2(x * 16, y * 16), this));
                break;
            /*
            case 74:
                cell.setTile(chest[0]);
                frontLayer.setCell(x, y, cell);
                break;*/
        }
    }

    public void checkAuto(int x, int y, SanValero sanValero) {
        TiledMapTileLayer.Cell cell = frontLayer.getCell(x, y);
        Vector2 pos = new Vector2(x, y);
        if (cell != null) {

            switch (cell.getTile().getId()) {
                case 55:
                case 56:
                    Integer key = keys.get(pos);
                    sanValero.addKey(level, key);
                    frontLayer.setCell(x, y, null);
                    break;
            }
        }

        //for toxic water
        cell = topLayer.getCell(x, y);
        if (cell == null) return;
        int damage = canDamage(cell);
        System.out.println(canDamage(cell));
        if (damage != 0) {
            gameStage.sanValero.applyDamage(damage);
        }
    }


    public TiledMap getMap() {
        return tiledMap;
    }

    public void renderBottom() {
        //camera.update();
        mapRenderer.render(bottomLayers);
    }


    public void renderTop() {
        camera.update();
        mapRenderer.render(topLayers);
    }

    public void updateMapCamera(final OrthographicCamera camera) {
        //this.camera = camera;
        //camera.update();
        mapRenderer.setView(camera);
    }

    public void generateRectangles() {
        rectangles = new Rectangle[frontLayer.getWidth()][frontLayer.getHeight()][4];

        for (int i = 0; i < frontLayer.getWidth(); i++) {
            for (int j = 0; j < frontLayer.getHeight(); j++) {
                TiledMapTileLayer.Cell cell = frontLayer.getCell(i, j);
                int code = isCollidable(cell);
                rectangles[i][j] = getRectangles(i, j, code);
            }
        }
    }

    public Rectangle[] getCollisionBoxes(int x, int y) {
        try {
            return rectangles[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }

    }

    public Rectangle[] getRectangles(int x, int y, int code) {
        Rectangle[] rectangles = new Rectangle[4];

        if ((code & 1) != 0) {
            rectangles[0] = new Rectangle(x * 16, 8 + y * 16, 8, 8);
        }
        if ((code & 2) != 0) {
            rectangles[1] = new Rectangle(8 + x * 16, 8 + y * 16, 8, 8);
        }
        if ((code & 4) != 0) {
            rectangles[2] = new Rectangle(8 + x * 16, y * 16, 8, 8);
        }
        if ((code & 8) != 0) {
            rectangles[3] = new Rectangle(x * 16, y * 16, 8, 8);
        }
        if ((code & 16) != 0) { //bridge
            rectangles[3] = new Rectangle(x * 16, y * 16, 16, 2);
        }
        return rectangles;
    }

    public int isCollidable(TiledMapTileLayer.Cell cell) {
        if (cell != null && cell.getTile().getProperties().containsKey("collides")) {
            return Integer.parseInt(((String) cell.getTile().getProperties().get("collides")));
        } else return 0;
    }

    public int canDamage(TiledMapTileLayer.Cell cell) {
        if (cell != null && cell.getTile().getProperties().containsKey("damage")) {
            return Integer.parseInt(((String) cell.getTile().getProperties().get("damage")));
        } else return 0;
    }

    public void drawBlockRectangles(ShapeRenderer shapes) {

        for (Rectangle[][] row : rectangles) {
            for (Rectangle[] col : row) {
                for (Rectangle r : col) {
                    if (r != null) {
                        shapes.rect(r.x, r.y, r.width, r.height);
                    }
                }
            }
        }
    }


    private int isCollidable(int x, int y) {
        return isCollidable(frontLayer.getCell(x, y));
    }

    public boolean isLadder(TiledMapTileLayer.Cell cell) {
        return cell != null && cell.getTile().getProperties().containsKey("ladder");
    }
/*
    public void drawBlockRectangles(ShapeRenderer shapes, MovableCharacter movableCharacter) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                drawBlockRectangles(shapes, i, j, movableCharacter);
            }
        }
    }
*/
    public Vector2 startPos() {
        int tpDoor = SaveGame.getStat("tpDoor", 0);
        return doorPos(tpDoor);
    }

    public Vector2 doorPos(int tpDoor) {
        for (Door door : doors.values()) {
            if (door.getID() == tpDoor) {
                Vector2 doorPosition = door.getPosition().cpy();
                doorPosition.x -= 8;
                return doorPosition;
            }
        }
        return null;
    }

    public int getWidth() {
        return backLayer.getWidth() * 16;
    }


    public int getHeight() {
        return backLayer.getHeight() * 16;
    }

    public void dispose() {
        tiledMap.dispose();
    }

    public void setAlpha(Color color) {
        backLayer.setOpacity(color.a);
        frontLayer.setOpacity(color.a);
        topLayer.setOpacity(color.a);
    }

}

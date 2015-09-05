package es.luismars.Maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import es.luismars.Maps.GameMap;
import es.luismars.Stages.GameStage;
import es.luismars.Tools.AssetsLoader;

/**
 * Created by Dalek on 30/07/2015.
 */
public class OverworldMap extends GameMap {

    int[] sequia = new int[] {72, 76, 77, 78, 77, 76};

    public OverworldMap(GameStage gameStage, int level) {
        super();
        this.gameStage = gameStage;
        camera = (OrthographicCamera) gameStage.getCamera();
        setupMap(level);
    }

    public void setupMap(int level) {
        tiledMap = new TmxMapLoader().load("notlevel.tmx");
        //tiledMap = new TmxMapLoader().load("level" + level + ".tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1);
        mapRenderer.setView(camera);

        tileSets = tiledMap.getTileSets();

        topLayer = (TiledMapTileLayer) tiledMap.getLayers().get("front-detail");
        frontLayer = (TiledMapTileLayer) tiledMap.getLayers().get("front");
        backLayer = (TiledMapTileLayer) tiledMap.getLayers().get("back");
        bottomLayers = new int[] {0, 1, 2};
        topLayers = new int[] {};

//        loadObjects();
        loadAnimations();

        generateRectangles();

    }

    private void loadAnimations() {
        setAnimatedTiles(sequia, AssetsLoader.time * 4, "sequia", frontLayer);
    }

    @Override
    public void renderBottom() {
        Gdx.gl.glClearColor(0.3568627450980392f, 0.6588235294117647f, 1, 1);
        super.renderBottom();
    }
}

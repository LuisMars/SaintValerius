package es.luismars.Stages;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.Viewport;
import es.luismars.Characters.*;
import es.luismars.Maps.OverworldMap;
import es.luismars.Tools.AssetsLoader;
import es.luismars.Main;
import es.luismars.Maps.GameMap;
import es.luismars.Stages.Menus.*;
import es.luismars.Tools.SaveGame;
import es.luismars.Tools.Shake;

/**
 * Created by Dalek on 15/07/2015.
 */
public class GameStage extends CustomStage {

    GameMap map;

    Shake shake;

    String name;
    boolean debug = false;
    public SanValero sanValero;

    public Group enemies;

    FrameBuffer frameBuffer;
    TextureRegion frameBufferRegion;

    public GameStage(String name) {
        super();
        this.name = name;

        shake = new Shake();

        AssetsLoader.loadSanValero();
        AssetsLoader.loadLocodrilo();
        SaveGame.setName(name);
        SaveGame.load();
        enemies = new Group();
        addActor(enemies);

        map = new GameMap(this);


        frameBuffer = new FrameBuffer(Pixmap.Format.RGB888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        frameBufferRegion = new TextureRegion(frameBuffer.getColorBufferTexture(), frameBuffer.getWidth(), frameBuffer.getHeight());
        frameBufferRegion.flip(false, true);
        //lightBufferRegion = new TextureRegion(lightBuffer.getColorBufferTexture(),0,lightBuffer.getHeight()-lowDisplayH,lowDisplayW,lowDisplayH);

        sanValero = new SanValero(map);
        addActor(sanValero);



        //moveCameraTo(sanValero);
        //((OrthographicCamera) getCamera()).zoom = 0.75f;
        //getRoot().setColor(1, 1, 1, 0);
        //getRoot().addAction(fadeIn(1f));
    }

    @Override
    public void draw() {

        //map.setAlpha(getRoot().setColor());

        //OrthographicCamera camera = (OrthographicCamera) getCamera();
        getBatch().setColor(Color.WHITE);
        map.renderBottom();

        super.draw();

        map.renderTop();
/*
        if (!(map instanceof OverworldMap))
            renderLights(camera);
*/
    }


    private void renderLights(OrthographicCamera camera) {
        frameBuffer.begin();
        getBatch().begin();
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        Gdx.gl.glClearColor(0, 0, 0, 2/3f);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        getBatch().setColor(1, 1, 1, 2/3f);
        //getBatch().setColor(1, 0.75f, 0.65f, 0.45f);

/*
        float tx = sanValero.getX() + sanValero.getOriginX();
        float ty = sanValero.getY() + sanValero.getOriginY();
        renderLight(tx, ty);
*/
        TiledMapTileLayer tileLayer = (TiledMapTileLayer)map.getMap().getLayers().get("front");
        for (int i = 0; i < tileLayer.getWidth(); i++) {
            for (int j = 0; j < tileLayer.getHeight(); j++) {
                TiledMapTileLayer.Cell cell = tileLayer.getCell(i, j);
                if (cell == null) continue;
                TiledMapTile tile = cell.getTile();
                if (tile != null && tile.getProperties().containsKey("light")) {
                    renderLight(8 + i * 16, 8 + j * 16);
                }
            }
        }

        getBatch().end();
        frameBuffer.end();

        Gdx.gl.glBlendFunc(GL20.GL_DST_COLOR, GL20.GL_ZERO);

        getBatch().begin();
        Viewport viewport = getViewport();
        getBatch().draw(frameBufferRegion, ((int) (camera.position.x - camera.viewportWidth / 2)), ((int) (camera.position.y - camera.viewportHeight / 2)), viewport.getWorldWidth(), viewport.getWorldHeight());
        getBatch().end();
    }

    Texture light = new Texture("light.png");
    private void renderLight(float tx, float ty) {
        float tw =(128/100f)*96;

        tx -=(tw/2);
        ty -=(tw/2);
        getBatch().draw(light,
                tx, ty,
                tw, tw,
                0, 0,
                128, 128,
                false, true);
    }

    public void toggleDebug() {
        debug = !debug;
        setDebugAll(debug);
    }

    public void backToMenu() {
        Main.ChangeScreen(new MenuScreen());
        dispose();
    }

    @Override
    public void act() {
        super.act();

        for (Actor a : getActors()) {
            if (a instanceof Bullet) {
                Bullet b = ((Bullet) a);
                for (Actor e : enemies.getChildren()) {
                    if (e instanceof Locodrilo) {
                        Locodrilo l = (Locodrilo) e;
                        if (b.collidesWith(l)) {
                            l.applyDamage(1);
                            b.remove();
                        }
                    }
                }
                if (b.collidesWithEnvoirement()) {
                    b.remove();
                }
            }
        }

        for (Actor a : enemies.getChildren()) {
            if (a instanceof Locodrilo) {
                Locodrilo l = (Locodrilo) a;
                l.checkDistance(sanValero);
                if (sanValero.collidesAttack(l)) {
                    l.applyDamage(1);
                }
                if (l.collidesAttack(sanValero)) {
                    sanValero.applyDamage(2);
                } else if (l.collidesWith(sanValero)) {
                    sanValero.applyDamage(1);
                }
            } else if (a instanceof Drop) {
                Drop drop = (Drop) a;
                if (sanValero.collidesWith(drop)) {
                    if (drop.canBeCaught() && sanValero.addHealth(1)) {
                        a.remove();
                    }
                }
            } else if (a instanceof Bullet) {
                Bullet b = (Bullet) a;
                if (sanValero.collidesWith(b)) {
                    sanValero.applyDamage(1);
                    b.remove();
                }
            }
        }
        map.updateMapCamera(((OrthographicCamera) getCamera()));
        moveCameraTo(sanValero);

    }

    public void moveCameraTo(MovableCharacter actor) {
        moveCameraTo(actor.getX() + actor.getOriginX(), actor.getY() + actor.getOriginY());
    }

    public void moveCameraTo(float x, float y) {
        //getCamera().position.set(Math.round(x * 100) / 100, Math.round(y * 100) / 100, 100);

        OrthographicCamera cam = (OrthographicCamera) getCamera();
        Vector3 pos = cam.position;

        float min = 0;

        int margin = 64;
        if (Gdx.app.getType().equals(Application.ApplicationType.Android)) {
            if (y < margin) {
                if (x < margin) {
                    min = margin - x;
                } else if (x > map.getWidth() - margin) {
                    min = x - (map.getWidth() - margin);
                }
            }
        }

        float halfWidth = cam.viewportWidth * (cam.zoom / 2);
        float halfHeight = cam.viewportHeight * (cam.zoom / 2);

        x = MathUtils.clamp(x, halfWidth, map.getWidth() - halfWidth);
        y = MathUtils.clamp(y, halfHeight - min, map.getHeight() - halfHeight);


        pos.set((int) x, (int) y, 100);
        shake.update(Gdx.graphics.getDeltaTime(), getCamera(), new Vector2(x, y));

    }


    @Override
    public void dispose() {
        super.dispose();
        map.dispose();
        AssetsLoader.disposeSanValero();
    }

    public void resize(int width, int height) {
        /*
        getViewport().update(width, height, true);*/
        super.resize(width, height);
        map.updateMapCamera(((OrthographicCamera) getViewport().getCamera()));

        if (frameBuffer!=null) frameBuffer.dispose();
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        frameBufferRegion = new TextureRegion(frameBuffer.getColorBufferTexture(),frameBuffer.getWidth(),frameBuffer.getHeight());
        frameBufferRegion.flip(false, true);
    }


    public void changeLevel(Vector2 vector2, GameMap gameMap) {
        //getRoot().addAction(fadeOut(1f));

        sanValero.changeLevel(vector2, gameMap);
        //getRoot().addAction(fadeIn(1f));
    }

    public void changeLevel () {
        map = new OverworldMap(this, 0);
        sanValero.changeLevel(new Vector2(64, 64), map);
    }

}

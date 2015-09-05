package es.luismars.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.GdxRuntimeException;
import es.luismars.Stages.Hud.HudStage;
import es.luismars.Tools.PixelShader;
import es.luismars.Tools.SaveGame;
import es.luismars.Tools.Utils;

/**
 * Created by Dalek on 15/07/2015.
 */
public class GameScreen implements Screen  {

    GameStage gameStage;
    HudStage hudStage;
    String name;

    public GameScreen(String name) {
        this(new GameStage(name), name);
    }

    public GameScreen(GameStage gameStage, String name) {
        this.gameStage = gameStage;
        this.name = name;

        hudStage = new HudStage(gameStage);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        gameStage.act();
        hudStage.act();

        gameStage.draw();
        hudStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        Gdx.graphics.setDisplayMode(width, height, Gdx.graphics.isFullscreen());
        gameStage.resize(width, height);
        hudStage.resize(width, height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        gameStage.dispose();
    }


}

package es.luismars.Stages.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import es.luismars.Main;

/**
 * Created by Dalek on 24/07/2015.
 */
public class MenuScreen implements Screen {

    public static Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    public MenuStage stage;

    public MenuScreen(MenuStage stage) {
        this.stage = stage;
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    }

    public MenuScreen() {
        this(new MainMenuStage());
    }


    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        stage.act();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.resize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        Main.ChangeScreen(new MenuScreen());
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}

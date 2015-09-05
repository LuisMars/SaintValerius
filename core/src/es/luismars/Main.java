package es.luismars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import es.luismars.Stages.GameScreen;
import es.luismars.Stages.Menus.MenuScreen;

public class Main extends Game {

	@Override
	public void create() {
        setScreen(new MenuScreen());
	}

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    public static void ChangeScreen(Screen screen) {
        ((Game) Gdx.app.getApplicationListener()).setScreen(screen);
    }

}

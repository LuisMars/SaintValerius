package es.luismars.Stages;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import es.luismars.Tools.PixelViewport;

/**
 * Created by Dalek on 27/07/2015.
 */
public class CustomStage extends Stage {

    public CustomStage(boolean expand) {
        int size = (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) ? 1 : 2;
        float density = size * 0.6f/Gdx.app.getGraphics().getDensity();
        setViewport(new PixelViewport(((int) (320 * density)), ((int) (240 * density)), getCamera(), expand));
    }
/*
    public CustomStage(boolean expand) {
        this(false, expand);
    }
*/
    public CustomStage() {
        this(true);
    }

    public void setFullscreen() {
        Preferences preferences = Gdx.app.getPreferences("graphics");
        int width = preferences.getInteger("width", 768);
        int height = preferences.getInteger("height", 600);
        boolean fullscreen = preferences.getBoolean("fullscreen", false);

        if (fullscreen) {
            Gdx.graphics.setDisplayMode(width, height, true);
            Gdx.graphics.setDisplayMode(768, 600, false);
            preferences.putBoolean("fullscreen", false);
        } else {
            Gdx.graphics.setDisplayMode(width, height, true);
            preferences.putBoolean("fullscreen", true);
        }
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }
}

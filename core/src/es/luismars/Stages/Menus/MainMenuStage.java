package es.luismars.Stages.Menus;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import es.luismars.Main;
import es.luismars.Stages.GameScreen;
import es.luismars.Stages.Menus.Widgets.*;
import es.luismars.Tools.ScreenshotFactory;

/**
 * Created by Dalek on 01/08/2015.
 */
public class MainMenuStage extends MenuStage {
    Button continueButton, newGameButton, optionsButton, exitButton;
    public MainMenuStage() {
        super("San Valero", "From under the s\u00E8quia");
        loadPreferences();
    }

    @Override
    protected void setupTable() {

        continueButton = new Button("Continuar");
        addDefault(continueButton, 100).padTop(5);
        bottomTable.row();

        newGameButton = new Button("Nueva partida");
        addDefault(newGameButton, 100);
        bottomTable.row();

        optionsButton = new Button("Opciones");
        addDefault(optionsButton, 100);
        bottomTable.row();

        exitButton = new Button("Salir");
        addDefault(exitButton, 100);
    }


    @Override
    protected void addListeners() {
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.ChangeScreen(new MenuScreen(new ContinueStage()));
            }
        });

        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                Main.ChangeScreen(new MenuScreen(new NewGameStage()));
            }
        });

        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.ChangeScreen(new MenuScreen(new OptionsStage()));
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    private void loadPreferences() {
        if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            Preferences preferences = Gdx.app.getPreferences("graphics");
            int width = preferences.getInteger("width", 768);
            int height = preferences.getInteger("height", 600);
            boolean fullscreen = preferences.getBoolean("fullscreen", false);

            if (fullscreen && !Gdx.graphics.isFullscreen()) {
                //Gdx.graphics.setDisplayMode(width, height, false);
                Gdx.graphics.setDisplayMode(width, height, true);

            } else if (!fullscreen && Gdx.graphics.isFullscreen()){
                Gdx.graphics.setDisplayMode(width, height, true);
                Gdx.graphics.setDisplayMode(768, 600, false);
            } else {
                Gdx.graphics.setDisplayMode(width, height, fullscreen);
            }

        }
    }

    @Override
    public void draw() {
        super.draw();
    }
}

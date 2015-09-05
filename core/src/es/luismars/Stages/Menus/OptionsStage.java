package es.luismars.Stages.Menus;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import es.luismars.Main;
import es.luismars.Stages.Menus.Widgets.Button;
import es.luismars.Stages.Menus.Widgets.Label;
import es.luismars.Stages.Menus.Widgets.SelectBox;

import java.util.Arrays;

/**
 * Created by Dalek on 02/08/2015.
 */
public class OptionsStage extends MenuStage {

    Button backButton, saveButton, resetButton;
    SelectBox<Integer> width, height;
    CheckBox fullscreen;

    public OptionsStage() {
        super("Opciones", "");
        load();
    }

    @Override
    protected void setupTable() {
        if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            bottomTable.add(new Label("Resoluci\u00F3n:"));
            width = new SelectBox<Integer>(MenuScreen.skin);
            width.setItems(768, 800, 1024, 1093, 1152, 1280, 1360, 1366, 1400, 1440, 1536, 1600, 1680, 1920, 2048, 2560);
            addDefault(width, 45);
            height = new SelectBox<Integer>(MenuScreen.skin);
            height.setItems(600, 614, 720, 768, 800, 864, 900, 960, 1024, 1050, 1080, 1152, 1200, 1440, 1600);
            addDefault(height, 45);
            bottomTable.row();
            bottomTable.add(new Label("Pantalla completa:"));

            fullscreen = new CheckBox("", MenuScreen.skin);
            bottomTable.add(fullscreen).colspan(2);

            bottomTable.row().expand().fill();

        }
        Table bottomButtons = new Table();
        bottomTable.add(bottomButtons).expand().fill().colspan(3);
        backButton = new Button("Volver");
        bottomButtons.add(backButton).pad(2).width(65);

        resetButton = new Button("Deshacer");
        bottomButtons.add(resetButton).pad(2).width(65);

        saveButton = new Button("Guardar");
        bottomButtons.add(saveButton).pad(2).width(65);

    }

    @Override
    protected void addListeners() {

        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                save();
            }
        });

        resetButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                load();
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.ChangeScreen(new MenuScreen());
            }
        });
    }

    private void save() {
        if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            Preferences preferences = Gdx.app.getPreferences("graphics");

            preferences.putInteger("width", width.getSelected());
            preferences.putInteger("height", height.getSelected());
            preferences.putBoolean("fullscreen", fullscreen.isChecked());

            if (fullscreen.isChecked() && !Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setDisplayMode(width.getSelected(), height.getSelected(), true);
            } else if (!fullscreen.isChecked() && Gdx.graphics.isFullscreen()){
                Gdx.graphics.setDisplayMode(width.getSelected(), height.getSelected(), true);
                Gdx.graphics.setDisplayMode(768, 600, false);
            } else {
            Gdx.graphics.setDisplayMode(width.getSelected(), height.getSelected(), fullscreen.isChecked());
            }
            preferences.flush();
        }
    }
    private void load() {
        if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            Preferences preferences = Gdx.app.getPreferences("graphics");
            width.setSelected(preferences.getInteger("width", 768));
            height.setSelected(preferences.getInteger("height", 600));
            fullscreen.setChecked(preferences.getBoolean("fullscreen", Gdx.graphics.isFullscreen()));
        }
    }
}

package es.luismars.Stages.Menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import es.luismars.Main;
import es.luismars.Stages.GameScreen;
import es.luismars.Stages.Menus.Widgets.Button;
import es.luismars.Tools.SaveGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dalek on 03/08/2015.
 */
public class ContinueStage extends MenuStage {

    Button backButton, startButton;
    List<Table> slots;
    String selected;

    public ContinueStage() {
        super("Continuar", "Seleccione una partida");
    }

    @Override
    protected void setupTable() {
        slots = new ArrayList<Table>();
        for (final String name : SaveGame.getSaveFiles()) {
            final Table slot = new Table(MenuScreen.skin);
            slots.add(slot);
            slot.setBackground("background-light");
            slot.add(name);
            slot.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    slot.setBackground("background-dark2");
                    selected = name;
                    for (Table t : slots) {
                        if (t != slot) {
                            t.setBackground("background-light");
                        }
                    }

                    return true;
                }
            });
            bottomTable.add(slot).width(150).expandX().pad(5);

            bottomTable.row();
        }

        Table bottomButtons = new Table();
        bottomTable.add(bottomButtons).expand().fill().colspan(3);
        backButton = new Button("Volver");
        bottomButtons.add(backButton).pad(2).width(65);

        startButton = new Button("Jugar");
        bottomButtons.add(startButton).pad(2).width(65);
    }

    @Override
    protected void addListeners() {
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.ChangeScreen(new MenuScreen());
            }
        });

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (selected != null) {
                    Main.ChangeScreen(new GameScreen(selected));
                }
            }
        });
    }
}
package es.luismars.Stages.Menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import es.luismars.Main;
import es.luismars.Stages.GameScreen;
import es.luismars.Stages.Menus.Widgets.Button;
import es.luismars.Stages.Menus.Widgets.Label;

/**
 * Created by Dalek on 03/08/2015.
 */
public class NewGameStage extends MenuStage {

    Button startButton, backButton;
    TextField name;

    public NewGameStage() {
        super("Nueva partida", "");
    }

    @Override
    protected void setupTable() {

        addDefault(new Label("Nombre"), 40);
        name = new TextField("San Valero", MenuScreen.skin);
        name.setAlignment(Align.center);
        bottomTable.add(name).expandX();
        bottomTable.row();


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
                Main.ChangeScreen(new GameScreen(name.getText()));
            }
        });
    }
}

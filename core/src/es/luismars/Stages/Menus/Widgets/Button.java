package es.luismars.Stages.Menus.Widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import es.luismars.Main;
import es.luismars.Stages.GameScreen;
import es.luismars.Stages.GameStage;
import es.luismars.Stages.Menus.MenuScreen;
import es.luismars.Stages.Menus.MenuStage;

/**
 * Created by Dalek on 01/08/2015.
 */
public class Button extends TextButton {

    public Button(String text) {
        super(text, MenuScreen.skin.get("toggle", TextButtonStyle.class));

    }

    public void addScreen(final Screen nextScreen) {

        this.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Main.ChangeScreen(nextScreen);
                return true;
            }
        });
    }

}

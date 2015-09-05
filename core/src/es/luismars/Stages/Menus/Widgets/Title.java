package es.luismars.Stages.Menus.Widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import es.luismars.Stages.Menus.MenuScreen;

/**
 * Created by Dalek on 01/08/2015.
 */
public class Title extends Label {

    public Title(CharSequence text) {
        super(text, MenuScreen.skin, "title");
    }
}

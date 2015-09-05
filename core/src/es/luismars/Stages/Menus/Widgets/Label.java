package es.luismars.Stages.Menus.Widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import es.luismars.Stages.Menus.MenuScreen;

/**
 * Created by Dalek on 01/08/2015.
 */
public class Label extends com.badlogic.gdx.scenes.scene2d.ui.Label {

    public Label(CharSequence text) {
        super(text, MenuScreen.skin);
    }

    public Label(CharSequence text, boolean big) {
        super(text, MenuScreen.skin, "subtitle");
    }
}

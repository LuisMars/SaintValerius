package es.luismars.Stages.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import es.luismars.Stages.CustomStage;
import es.luismars.Stages.Menus.Widgets.*;
import es.luismars.Stages.Menus.Widgets.Label;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;

/**
 * Created by Dalek on 24/07/2015.
 */
public abstract class MenuStage extends CustomStage {

    Table table;
    Table bottomTable;
    Table contentTable;
    TitleTable titleTable;

    public MenuStage(String title, String subtitle) {
        super(false);

        table = new Table();

        addActor(table);
        table.columnDefaults(2);
        table.center().top();
        table.setFillParent(true);

        titleTable = new TitleTable();
        table.add(titleTable).center().colspan(2).width(220);

        titleTable.add(new Title(title)).top();
        titleTable.row();
        titleTable.add(new Label(subtitle, true)).expandY().bottom().pad(5);
        table.row();

        bottomTable = new Table(MenuScreen.skin);

        ScrollPane scrollPane = new ScrollPane(bottomTable, MenuScreen.skin);
        scrollPane.setVariableSizeKnobs(false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setOverscroll(false, false);
        table.add(scrollPane).colspan(2).center().width(220).expandY().fillY();


        Gdx.input.setInputProcessor(this);
        setupTable();
        addListeners();
    }

    protected abstract void setupTable();

    protected abstract void addListeners();
    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public boolean keyDown(int keyCode) {
        switch (keyCode) {
            case Input.Keys.F11:
                setFullscreen();
                break;
        }
        return true;
    }

    public void resize(int width, int height) {
        Viewport viewport = getViewport();
        viewport.update(width, height, true);
        table.setWidth(width);
    }

    public <T extends Actor> Cell<T> addDefault(T actor, int width) {
        return bottomTable.add(actor).width(width).pad(2);
    }

}

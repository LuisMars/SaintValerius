package es.luismars.Stages.Hud;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import es.luismars.Stages.CustomStage;
import es.luismars.Tools.AssetsLoader;
import es.luismars.Stages.GameStage;
import es.luismars.Tools.ScreenshotFactory;

/**
 * Created by Dalek on 25/07/2015.
 */
public class HudStage extends CustomStage {

    GameStage gameStage;
    HealthBar healthBar;

    Skin skin;
    boolean debug;
    Table table;
    Touchpad touchpad;
    Table buttons;
    Button aButton;
    Button xButton;
    Button bButton, yButton;


    public HudStage (final GameStage gameStage) {
        super(false);
        AssetsLoader.loadHud();

        this.gameStage = gameStage;
        table = new Table();
        table.setColor(1, 1, 1, 0.5f);
        table.setFillParent(true);
        addActor(table);

        healthBar = new HealthBar(gameStage.sanValero);
        table.add(healthBar).top().left().expand().pad(8);
        table.row().pad(16);



        loadController();
        loadOnScreenControls(gameStage);

        //((OrthographicCamera) getCamera()).zoom = 2.5f;
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(this);
    }

    private void loadController() {
        Controllers.addListener(new ControllerListener() {
            @Override
            public void connected(Controller controller) {

            }

            @Override
            public void disconnected(Controller controller) {

            }

            @Override
            public boolean buttonDown(Controller controller, int buttonCode) {
                switch (buttonCode) {
                    case Xbox360Pad.BUTTON_A:
                        buttonA();
                        break;
                    case Xbox360Pad.BUTTON_X:
                        buttonX();
                        break;
                    case Xbox360Pad.BUTTON_B:
                        buttonB();
                        break;
                }
                return true;
            }

            @Override
            public boolean buttonUp(Controller controller, int buttonCode) {
                switch (buttonCode) {
                    case Xbox360Pad.BUTTON_A:
                        releaseButtonB();
                        break;
                }
                return true;
            }

            @Override
            public boolean axisMoved(Controller controller, int axisCode, float value) {
                switch (axisCode) {
                    case Xbox360Pad.AXIS_LEFT_X:
                        actAxisX(value);
                        break;
                    case Xbox360Pad.AXIS_LEFT_Y:
                        actAxisY(-value);
                        break;
                }
                return true;
            }

            @Override
            public boolean povMoved(Controller controller, int povCode, PovDirection value) {
                return false;
            }

            @Override
            public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
                return false;
            }

            @Override
            public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
                return false;
            }

            @Override
            public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
                return false;
            }
        });
    }

    private void releaseButtonB() {
        gameStage.sanValero.stopJump();
    }

    private void buttonX() {
        gameStage.sanValero.attack();
    }

    private void buttonA() {
        gameStage.sanValero.jump();
    }

    private void buttonB() { gameStage.sanValero.down();}

    private void actAxisX(float value) {
        if (!MathUtils.isZero(value, 0.25f))
            gameStage.sanValero.moveX(value);
        else
            gameStage.sanValero.stopX();
    }

    private void actAxisY(float percentY) {
        if (gameStage.sanValero.isOnLadder) {
            if (!MathUtils.isZero(percentY, 0.6f)) {
                if (-percentY < 0) {
                    gameStage.sanValero.jump();
                } else {
                    gameStage.sanValero.down();
                }
            } else {
                gameStage.sanValero.stopJump();
            }
        }
    }
    private void loadOnScreenControls(final GameStage gameStage) {
        if (debug || !Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
            touchpad = new Touchpad(0, skin);

            aButton = new Button(skin, "a");

            aButton.addListener(
                    new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            gameStage.sanValero.jump();
                            return true;
                        }

                        @Override
                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                            gameStage.sanValero.stopJump();
                            super.touchUp(event, x, y, pointer, button);
                        }
                    }
            );

            xButton = new Button(skin, "x");
            xButton.setSize(16, 16);
            xButton.addListener(
                    new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            gameStage.sanValero.attack();
                            return super.touchDown(event, x, y, pointer, button);
                        }
                    }
            );


            bButton = new Button(skin, "b");
            bButton = new Button(skin, "y");

            bButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    gameStage.sanValero.down();
                    return true;
                }
            });

            buttons = new Table();

            table.add(touchpad).size(80).padLeft(32).padBottom(8).bottom().left().expandX();
            table.add(buttons).bottom().left();
            buttons.columnDefaults(3);
            buttons.add(yButton).size(40).pad(4).center().colspan(3).row();
            buttons.add(xButton).size(40).padRight(16).left();
            buttons.add(bButton).size(40).padLeft(16).right().row();
            buttons.add(aButton).size(40).pad(4).center().colspan(3);

        }
    }

    @Override
    public void act() {
        super.act();

        if (!Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            float percentX = touchpad.getKnobPercentX();
            float percentY = touchpad.getKnobPercentY();

            actAxisX(percentX);
            actAxisY(percentY);

        }
    }


    @Override
    public boolean keyDown(int keyCode) {
        switch (keyCode) {
            case Input.Keys.A:
                gameStage.sanValero.moveX(-1);
                break;
            case Input.Keys.D:
                gameStage.sanValero.moveX(1);
                break;
            case Input.Keys.W:
                gameStage.sanValero.jump();
                break;
            case Input.Keys.S:
                gameStage.sanValero.down();
                break;
            case Input.Keys.F2:
                ScreenshotFactory.saveScreenshot();
                break;
            case Input.Keys.F3:
                gameStage.toggleDebug();
                debug = !debug;
                table.setDebug(debug, true);
                //loadOnScreenControls(gameStage);
                break;
            case Input.Keys.ESCAPE:
            case Input.Keys.BACK:
                gameStage.backToMenu();
                break;
            case Input.Keys.F11:
                gameStage.setFullscreen();
                break;
            case Input.Keys.J:
                //gameStage.changeLevel();
                break;
            case Input.Keys.SPACE:
                gameStage.sanValero.attack();
        }
        return true;
    }



    @Override
    public boolean keyUp(int keyCode) {
        switch (keyCode) {
            case Input.Keys.A:
            case Input.Keys.D:
                gameStage.sanValero.stopX();
                break;
            case Input.Keys.W:
            case Input.Keys.S:
                gameStage.sanValero.stopJump();
                break;
        }
        return true;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        table.setWidth(width);
    }
}

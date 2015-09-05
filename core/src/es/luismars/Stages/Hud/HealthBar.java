package es.luismars.Stages.Hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import es.luismars.Tools.AssetsLoader;
import es.luismars.Characters.SanValero;

/**
 * Created by Dalek on 25/07/2015.
 */
public class HealthBar extends Actor {

    int MAX_HEALTH;
    int health;
    int size = 8;
    SanValero sanValero;
    public HealthBar(SanValero sanValero) {
        this.sanValero = sanValero;
        MAX_HEALTH = sanValero.getMaxHealth();
        health = sanValero.getHealth();
        setBounds(size, 240 - size * (MAX_HEALTH + 1), size, size * MAX_HEALTH);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        MAX_HEALTH = sanValero.getMaxHealth();
        health = sanValero.getHealth();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        for (int i = MAX_HEALTH - 1; i >= 0; i--) {
            int index = 1;
            if (i == 0) index = 2;
            if (i == MAX_HEALTH - 1) index = 0;
            batch.draw(AssetsLoader.healthbar[index][0], getX(), getY() + size * i, getWidth(), size);
        }
        int color = Math.min(4, 5 - Math.round(4*(health/ (float)MAX_HEALTH)));

        for (int i = health - 1; i >= 0; i--) {
            int index = 1;
            if (i == 0) index = 2;
            if (i == health - 1) index = 0;
            if (health == 1) index = 3;
            batch.draw(AssetsLoader.healthbar[index][color], getX(), getY() + size * i, getWidth(), size);
        }


    }

    public void resize(int height) {
        setY((height/2) - getHeight());
    }
}

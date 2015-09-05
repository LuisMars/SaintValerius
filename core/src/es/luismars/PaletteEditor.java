package es.luismars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import java.util.*;

/**
 * Created by Dalek on 13/08/2015.
 */
public class PaletteEditor extends Game {

    Set<HSVColor> colors;
    ShapeRenderer renderer;

    int space = 200;

    @Override
    public void create() {
        colors = new TreeSet<HSVColor>();
        for (int i = 0; i < 8; i++) {
            colors.add(new HSVColor(0, 0, 7.0f/i));
        }

        addRamp(0);
        addRamp(42);
        addRamp(60);
        addRamp(90);
        addRamp(130);
        addRamp(190);
        addRamp(220);
        addRamp(300, 2);
        System.out.println(colors.size());
    }

    private void addRamp(float h) {
        addRamp(h, 64);
    }
    private void addRamp(float h, float size) {
        h /= 360;
        colors.add(new HSVColor(h, 1, 1));
        for (int i = 1; i <= size; i ++) {
            float s = i / size;
            float t = s/16.0f;
            float c = 1;
            while (c > 0 && !colors.add(new HSVColor(h + t, s, c)))
                c -= 1/8.0f;
            c = 1;
            while (c > 0 && !colors.add(new HSVColor(h - t, c, s)))
                c -= 1/8.0f;
        }
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
    }

    public HSVColor closetColor(HSVColor color) {
        float min = 9999999;
        HSVColor closest = null;
        for (HSVColor oColor : colors) {
            float distance = oColor.distance(color);
            if (distance > 0 && distance < min && oColor != color) {
                min = distance;
                closest = oColor;
            }
        }
        return closest;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.begin();

        renderer.rect(10, 10, space, space);
        renderer.rect(10, space + 20, space, space);
        renderer.rect(space + 20, space + 20, space, space);

        renderer.set(ShapeRenderer.ShapeType.Filled);
        for (HSVColor color : colors) {
            renderer.setColor(color);
            renderer.circle(10 + color.h * space, 10 + color.v * space, 4);
            renderer.circle(10 + color.h * space, space + 20 + color.s * space, 4);
            renderer.circle(space + 20 + color.v * space, space + 20 + color.s * space, 4);
        }

        renderer.end();

    }
}

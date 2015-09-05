package es.luismars;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Dalek on 13/08/2015.
 */
public class HSVColor extends Color implements Comparable<HSVColor> {
    float h, s, v;

    public HSVColor() {
        this.h = MathUtils.random();
        this.s = MathUtils.random();
        this.v =  MathUtils.random();
        setColor();
    }

    public HSVColor(float h, float s, float v) {
        if (h < 0)
            h += 1;
        this.h = h;
        this.s = s;
        this.v = v;
        setColor();
    }

    public HSVColor(int rgba) {
        super(rgba);
        setHSV();
    }

    public void setHSV() {
        float max = Math.max(r, Math.max(g, b)), min = Math.min(r, Math.max(g, b));
        v = max;
        float d = max - min;
        s = max == 0 ? 0 : d / max;

        if (max == min) {
            h = 0; // achromatic
        } else {
            if (max == r)
                h = (g - b) / d + (g < b ? 6 : 0);
            else if (max == g)
                h = (b - r) / d + 2;
            else if (max == b)
                h = (r - g) / d + 4;

            h /= 6;
        }
    }

    public void setColor() {

        int i = ((int) Math.floor(h * 6));
        float f = h * 6 - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);

        switch (i % 6) {
            case 0:
                r = v;
                g = t;
                b = p;
                break;
            case 1:
                r = q;
                g = v;
                b = p;
                break;
            case 2:
                r = p;
                g = v;
                b = t;
                break;
            case 3:
                r = p;
                g = q;
                b = v;
                break;
            case 4:
                r = t;
                g = p;
                b = v;
                break;
            case 5:
                r = v;
                g = p;
                b = q;
                break;
        }
    }

    public float distance(HSVColor color) {
        //return (float) (Math.pow(r - color.r, 2) + Math.pow(g - color.g, 2) + Math.pow(b - color.b, 2));
        return (float) (Math.pow(30 * (r - color.r), 2) + Math.pow(59 * (g - color.g), 2) + Math.pow(11 * (b - color.b), 2));
    }

    @Override
    public int compareTo(HSVColor o) {
        float tolerance = 0.15f;
        if (MathUtils.isEqual(r, o.r, tolerance) && MathUtils.isEqual(g, o.g, tolerance) && MathUtils.isEqual(b, o.b, tolerance))
            return 0;
        else if (MathUtils.isEqual(h, o.h, tolerance) && MathUtils.isEqual(s, o.s, tolerance) && MathUtils.isEqual(v, o.v, tolerance))
            return 0;
        else
            return 1;
    }

}

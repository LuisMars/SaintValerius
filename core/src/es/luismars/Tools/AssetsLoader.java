package es.luismars.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dalek on 15/07/2015.
 */
public class AssetsLoader {


    public static int size = 32;
    public static float time = 3/60f;
    public static Map<String, Animation> SanValero;
    public static Map<String, Animation> Locodrilo;
    public static TextureRegion[][] healthbar;
    public static Map<String, Animation> Drops;

    public static void loadHud() {
        healthbar = loadCharset("hud.png", 8);
    }

    public static void loadSanValero() {
        SanValero = new HashMap<String, Animation>();

        TextureRegion[][] charset = loadCharset("charset.png");

        SanValero.put("walk", getAnimation(charset[0], 0, 8, PlayMode.LOOP, 1));
        SanValero.put("jump-up", getAnimation(charset[1], 0, 4, PlayMode.NORMAL, 1));
        SanValero.put("jump-down", getAnimation(charset[1], 4, 3, PlayMode.LOOP_PINGPONG, 2));
        SanValero.put("jump-land", getAnimation(charset[1], 1, 3, PlayMode.REVERSED, 1));
        SanValero.put("attack", getAnimation(charset[2], 0, 7, PlayMode.NORMAL, 2/3f));
        SanValero.put("idle", getAnimation(charset[3], 0, 1, PlayMode.NORMAL, 1));
        SanValero.put("ladder", getAnimation(charset[3], 1, 3, PlayMode.LOOP_PINGPONG, 2));
    }

    public static void loadLocodrilo() {
        Locodrilo = new HashMap<String, Animation>();
        Drops = new HashMap<String, Animation>();
        TextureRegion[][] charset = loadCharset("locodrilo.png");

        Locodrilo.put("walk", getAnimation(charset[0], 0, 6, PlayMode.LOOP, 2));
        charset = loadCharset("locodrilo attack.png");
        Locodrilo.put("attack", getAnimation(charset[0], 0, 5, PlayMode.LOOP_PINGPONG, 2));

        TextureRegion[] heart = new TextureRegion[1];
        heart[0] = new TextureRegion(new Texture("heart.png"));
        Drops.put("heart", getAnimation(heart, 0, 1, PlayMode.LOOP, 1));
    }

    private static Animation getAnimation(TextureRegion[] textureRegions, int start, int length, PlayMode playMode, float playrate) {
        TextureRegion[] regions = Arrays.copyOfRange(textureRegions, start, start + length);
        Animation animation = new Animation(time * playrate, regions);
        animation.setPlayMode(playMode);
        return animation;
    }

    public static void disposeSanValero() {
        SanValero = null;
    }

    public static TextureRegion[][] loadCharset(String file) {
        Texture texture = new Texture(file);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Nearest);
        return new TextureRegion(texture).split(size, size);
    }

    public static TextureRegion[][] loadCharset(String file, int size) {
        return new TextureRegion(new Texture(file)).split(size, size);
    }


}

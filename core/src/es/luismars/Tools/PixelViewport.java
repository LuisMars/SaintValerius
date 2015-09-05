package es.luismars.Tools;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Dalek on 04/08/2015.
 */
public class PixelViewport extends Viewport {

    private int minWorldWidth, minWorldHeight;
    private boolean expand;

    public PixelViewport (int minWorldWidth, int minWorldHeight, Camera camera, boolean expand) {
        this.minWorldHeight = minWorldHeight;
        this.minWorldWidth = minWorldWidth;
        this.setCamera(camera);
        this.expand = expand;
    }

    @Override
    public void update (int screenWidth, int screenHeight, boolean centerCamera) {

        setScreenSize(screenWidth, screenHeight);

        int maxHorizontalMultiple = screenWidth / minWorldWidth;
        int maxVerticalMultiple = screenHeight / minWorldHeight;
        int pixelSize;

        if (expand)
            pixelSize = Math.max(maxHorizontalMultiple, maxVerticalMultiple);
        else
            pixelSize = Math.min(maxHorizontalMultiple, maxVerticalMultiple);

        setWorldWidth((float) screenWidth / (float) pixelSize);
        setWorldHeight((float) screenHeight / (float) pixelSize);

        super.update(screenWidth, screenHeight, centerCamera);
    }
}
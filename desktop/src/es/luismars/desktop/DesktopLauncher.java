package es.luismars.desktop;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import es.luismars.Main;
import es.luismars.PaletteEditor;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new Main(), config);

        config.title = "San Valero: From under the Sèquia (pre-Alpha)";

        config.vSyncEnabled = true;
		config.addIcon("icon16.png", Files.FileType.Internal);
        config.addIcon("icon32.png", Files.FileType.Internal);

	}

}

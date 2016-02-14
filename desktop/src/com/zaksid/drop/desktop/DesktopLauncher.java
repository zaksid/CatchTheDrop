package com.zaksid.drop.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.zaksid.drop.Drop;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Catch drops";
        config.width = Drop.SCREEN_WIDTH;
        config.height = Drop.SCREEN_HEIGHT;
        new LwjglApplication(new Drop(), config);
    }
}

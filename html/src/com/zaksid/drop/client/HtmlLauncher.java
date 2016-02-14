package com.zaksid.drop.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.zaksid.drop.Drop;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(Drop.SCREEN_WIDTH, Drop.SCREEN_HEIGHT);
    }

    @Override
    public ApplicationListener getApplicationListener() {
        return new Drop();
    }
}
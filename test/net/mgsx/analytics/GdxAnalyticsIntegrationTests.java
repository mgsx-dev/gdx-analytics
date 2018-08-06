package net.mgsx.analytics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Disposable;

public class GdxAnalyticsIntegrationTests  extends ApplicationAdapter 
{
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;
		config.height = 480;
		new LwjglApplication(new GdxAnalyticsIntegrationTests(), config);
	}

	private GdxAnalytics library;
	
	@Override
	public void create () {
		library = new GdxAnalytics();
	}

	@Override
	public void render () {
		if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
			library.perform();
		}
	}
	
	@Override
	public void dispose () {
		if(library instanceof Disposable){
			((Disposable) library).dispose();
		}
	}
}

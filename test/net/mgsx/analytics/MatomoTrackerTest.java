package net.mgsx.analytics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import net.mgsx.analytics.matomo.MatomoTracker;

public class MatomoTrackerTest  extends ApplicationAdapter 
{
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;
		config.height = 480;
		new LwjglApplication(new MatomoTrackerTest(), config);
	}

	private MatomoTracker tracker;
	
	@Override
	public void create () {
		tracker = new MatomoTracker("http://localhost:9000", "1");
		tracker.setUserID(0xdeadbeef); // optional visitor uid
	}

	@Override
	public void render () {
		if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
			
			tracker.trackEvent("game", "new game");
			tracker.trackEvent("game", "play", "world 1");
			tracker.trackEvent("game", "buy", "sword", 17);
			tracker.trackEvent("game", "run", "distance", 322.56f);
		}
	}
	
}

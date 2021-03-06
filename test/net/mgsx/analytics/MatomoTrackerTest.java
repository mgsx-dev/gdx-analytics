package net.mgsx.analytics;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import net.mgsx.analytics.matomo.MatomoTracker;

public class MatomoTrackerTest extends Game
{
	public static void main (String[] arg) {
		new LwjglApplication(new MatomoTrackerTest(), new LwjglApplicationConfiguration());
	}

	@Override
	public void create() {
		// initilize the app tracker :
		// replace with your Matomo server URL, site tracking id and get user ID from preferences or such (optional).
		GdxAnalytics.tracker = new MatomoTracker("http://localhost:9000", "1", 0xdeadbeef);
	}
	
	@Override
	public void render() {
		// perform some tracking in your game
		if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
			GdxAnalytics.tracker.trackEvent("game", "new game");
			GdxAnalytics.tracker.trackEvent("game", "play", "world 1");
			GdxAnalytics.tracker.trackEvent("game", "run", "distance", 14);
		}
	}
}

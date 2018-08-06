package net.mgsx.analytics;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import net.mgsx.analytics.google.GoogleAnalyticsTracker;

public class GoogleAnalyticsTrackerTest extends Game
{
	public static void main (String[] arg) {
		new LwjglApplication(new GoogleAnalyticsTrackerTest(), new LwjglApplicationConfiguration());
	}

	@Override
	public void create() {
		// initilize the app tracker :
		// replace with your Google Analytic UA and get user ID from preferences or such (optional).
		GdxAnalytics.tracker = new GoogleAnalyticsTracker("UA-XXXXXX-YYYY", "666");
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

package net.mgsx.analytics;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class MockTrackerTest extends Game
{
	public static void main (String[] arg) {
		new LwjglApplication(new MockTrackerTest(), new LwjglApplicationConfiguration());
	}

	@Override
	public void create() {
		// app tracker is by default a LogTracker
		// we just set log level to see calls in console
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
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

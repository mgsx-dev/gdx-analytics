package net.mgsx.analytics.mock;

import com.badlogic.gdx.Gdx;

import net.mgsx.analytics.Tracker;

public class LogOnlyTracker implements Tracker
{
	private static final String TAG = "NoTracker";
	
	@Override
	public void trackEvent(String category, String action) {
		Gdx.app.debug(TAG, "skip trackEvent " + category + ", " + action);
	}

	@Override
	public void trackEvent(String category, String action, String name) {
		Gdx.app.debug(TAG, "skip trackEvent " + category + ", " + action + ", " + name);
	}

	@Override
	public void trackEvent(String category, String action, String name, int value) {
		Gdx.app.debug(TAG, "skip trackEvent " + category + ", " + action + ", " + name + ", " + value);
	}
}

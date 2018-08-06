package net.mgsx.analytics;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import net.mgsx.analytics.google.GoogleAnalyticsTracker;

public class GoogleAnalyticsTrackerTest
{
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;
		config.height = 480;
		
		GoogleAnalyticsTracker.initialize("your tracking ID", "???");
		GoogleAnalyticsTracker tracker = new GoogleAnalyticsTracker();
		new LwjglApplication(new TrackerTest(tracker), config);
	}
}

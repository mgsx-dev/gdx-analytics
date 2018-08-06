package net.mgsx.analytics;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import net.mgsx.analytics.matomo.MatomoTracker;

public class MatomoTrackerTest
{
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;
		config.height = 480;
		
		MatomoTracker tracker = new MatomoTracker("http://localhost:9000", "1");
		tracker.setUserID(0xdeadbeef); // optional visitor uid
		new LwjglApplication(new TrackerTest(tracker), config);
	}
}
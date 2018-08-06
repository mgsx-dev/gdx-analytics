package net.mgsx.analytics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class TrackerTest extends ApplicationAdapter {

	protected Tracker tracker;
	
	public TrackerTest(Tracker tracker) {
		super();
		this.tracker = tracker;
	}

	@Override
	public void render () {
		if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
			tracker.trackEvent("game", "new game");
			tracker.trackEvent("game", "play", "world 1");
			tracker.trackEvent("game", "run", "distance", 14);
		}
	}
	
}

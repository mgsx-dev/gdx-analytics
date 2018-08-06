package net.mgsx.analytics;

import com.badlogic.gdx.Gdx;

public class GdxAnalytics
{
	public static final Tracker noTracker = new NoTracker();
	
	public static Tracker tracker = noTracker;
	
	public boolean perform(){
		if(Gdx.app != null) Gdx.app.log("GdxAnalytics", "calling perform");
		return true;
	}
	
}

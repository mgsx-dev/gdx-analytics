package net.mgsx.analytics;

import net.mgsx.analytics.mock.LogOnlyTracker;

public class GdxAnalytics
{
	public static final Tracker noTracker = new LogOnlyTracker();
	
	public static Tracker tracker = noTracker;
}

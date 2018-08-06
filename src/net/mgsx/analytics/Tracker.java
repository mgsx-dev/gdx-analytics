package net.mgsx.analytics;

public interface Tracker {
	public void trackEvent(String category, String action);
	public void trackEvent(String category, String action, String name);
	public void trackEvent(String category, String action, String name, int value);
}

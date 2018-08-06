package net.mgsx.analytics;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class GdxAnalyticsTest
{
	protected GdxAnalytics service = new GdxAnalytics();

	@Before
	public void setup(){
		service = new GdxAnalytics();
	}

	@After
	public void tearDown(){
		service = null;
	}

	@Test
	public void testNominal(){
		Assert.assertTrue(service.perform());
	}
}

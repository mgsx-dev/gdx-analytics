package net.mgsx.analytics.matomo;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.net.HttpStatus;

import net.mgsx.analytics.Tracker;

/**
 * Matomo (Piwik) tracker client
 * 
 * API : https://developer.matomo.org/api-reference/tracking-api
 * 
 * @author mgsx
 *
 */
public class MatomoTracker implements Tracker
{
	private static final String TAG = "MatomoTracker";
	private static final String urlPath = "/piwik.php";
	private String urlBase;
	private String idSite;
	private String idUser;
	
	/**
	 * @param urlBase something like http://my-domain.com or http://localhost:9000
	 * @param idSite id of the site to track (typically integer : 1 for first website, etc) see your matomo website configuation.
	 */
	public MatomoTracker(String urlBase, String idSite) {
		super();
		this.urlBase = urlBase;
		this.idSite = idSite;
	}
	
	/**
	 * @param urlBase something like http://my-domain.com or http://localhost:9000
	 * @param idSite id of the site to track (typically integer : 1 for first website, etc) see your matomo website configuation.
	 * @param uid unique visitor identifier (optional)
	 */
	public MatomoTracker(String urlBase, String idSite, long uid) {
		super();
		this.urlBase = urlBase;
		this.idSite = idSite;
		this.idUser = String.format("%x", uid);
	}
	
	/**
	 * Optionnaly set a unique user identifier in order to have accurate analytics
	 * @param uid
	 */
	public void setUserID(long uid){
		idUser = String.format("%x", uid);
	}

	/** see <a href="https://matomo.org/docs/event-tracking/">https://matomo.org/docs/event-tracking/</a> */
	@Override
	public void trackEvent(String category, String action) {
		trackEvent(category, action, null, null);
	}
	
	/** see <a href="https://matomo.org/docs/event-tracking/">https://matomo.org/docs/event-tracking/</a> */
	@Override
	public void trackEvent(String category, String action, String name) {
		trackEvent(category, action, name, null);
	}
	
	/** see <a href="https://matomo.org/docs/event-tracking/">https://matomo.org/docs/event-tracking/</a> */
	@Override
	public void trackEvent(String category, String action, String name, int value) {
		trackEvent(category, action, name, String.valueOf(value));
	}
	
	/** Matomo specific feature : float value storage.
	 * see <a href="https://matomo.org/docs/event-tracking/">https://matomo.org/docs/event-tracking/</a> */
	public void trackEvent(String category, String action, String name, float value) {
		trackEvent(category, action, name, String.valueOf(value));
	}
	
	private void trackEvent(String category, String action, String name, String value) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("e_c", category);
		parameters.put("e_a", action);
		if(name != null) parameters.put("e_n", name);
		if(value != null) parameters.put("e_v", value);
		track(parameters);
	}
	
	private void track(Map<String, String> parameters){
		// mandatory
		parameters.put("idsite", idSite);
		parameters.put("rec", "1");
		// recommended
		parameters.put("rand", String.valueOf(MathUtils.random()));
		parameters.put("apiv", "1");
		if(idUser != null) parameters.put("_id", idUser);
		
		HttpRequest httpRequest = new HttpRequest(HttpMethods.GET);
		httpRequest.setUrl(urlBase + urlPath + "?" + HttpParametersUtils.convertHttpParameters(parameters));
		
		
		Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				int statusCode = httpResponse.getStatus().getStatusCode();
				if(statusCode == HttpStatus.SC_OK){
					Gdx.app.log(TAG, "request success");
				}else{
					Gdx.app.error(TAG, "request error code " + statusCode);
				}
			}
			
			@Override
			public void failed(Throwable t) {
				Gdx.app.error(TAG, "request error", t);
			}
			
			@Override
			public void cancelled() {
				Gdx.app.error(TAG, "request cancelled");
			}
		});
	}
}

package net.mgsx.analytics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Disposable;

public class GdxAnalyticsIntegrationTests  extends ApplicationAdapter 
{
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;
		config.height = 480;
		new LwjglApplication(new GdxAnalyticsIntegrationTests(), config);
	}

	private GdxAnalytics library;
	
	@Override
	public void create () {
		library = new GdxAnalytics();
	}

	@Override
	public void render () {
		if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
			library.perform();
			
			// see https://developer.matomo.org/api-reference/tracking-api
			// http://your-piwik-domain.example/piwik.php
			
			HttpRequest httpRequest = new HttpRequest(HttpMethods.GET);
			httpRequest.setUrl("http://localhost:9000/piwik.php?idsite=1&rec=1&e_c=game&e_a=buy&e_n=sword&e_v=14");
			
			Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
				
				@Override
				public void handleHttpResponse(HttpResponse httpResponse) {
					// TODO Auto-generated method stub
					int statusCode = httpResponse.getStatus().getStatusCode();
					if(statusCode == HttpStatus.SC_OK){
						Gdx.app.log("net", "OK");
					}else if(statusCode == HttpStatus.SC_BAD_REQUEST){
						Gdx.app.log("net", "Bad Request");
					}else{
						Gdx.app.error("net", "handleHttpResponse code " + statusCode);
					}
				}
				
				@Override
				public void failed(Throwable t) {
					// TODO Auto-generated method stub
					Gdx.app.log("net", "failed");
				}
				
				@Override
				public void cancelled() {
					// TODO Auto-generated method stub
					Gdx.app.log("net", "cancelled");
				}
			});
			
			
		}
	}
	
	@Override
	public void dispose () {
		if(library instanceof Disposable){
			((Disposable) library).dispose();
		}
	}
}

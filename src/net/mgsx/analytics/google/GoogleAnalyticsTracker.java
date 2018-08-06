package net.mgsx.analytics.google;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpRequestHeader;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Array;

/**
 * 
 * specification API :
 * protocol : https://developers.google.com/analytics/devguides/collection/protocol/v1/reference
 * api : https://developers.google.com/analytics/devguides/collection/protocol/v1/devguide
 * 
 * @author mgsx
 *
 */
public class GoogleAnalyticsTracker 
{
	public static final HttpResponseListener responseLogger = new HttpResponseListener() {
		
		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			HttpStatus status = httpResponse.getStatus();
			if(status.getStatusCode() != HttpStatus.SC_OK){
				Gdx.app.error("GAService", "bad status : " + status.getStatusCode() + " : " + status.toString());
			}
		}
		
		@Override
		public void failed(Throwable t) {
			Gdx.app.error("GAService", "error", t);
		}
		
		@Override
		public void cancelled() {
			Gdx.app.error("GAService", "canceled");
		}
	};
	
	public static boolean disabled = false;
	
	public static final String ANONYMOUS = "555"; // TODO is it an example or a keyword ?
	
	/* default to empty to avoid error during request compilation (disabled mode only) */
	
	private static String trackingID = "";
	private static String userID = "";
	private static String userAgent = "";
	
	public static void initialize(String trackingID, String userAgent)
	{
		initialize(trackingID, userAgent, ANONYMOUS);
	}
	public static void initialize(String trackingID, String userAgent, String userID)
	{
		GoogleAnalyticsTracker.trackingID = trackingID;
		GoogleAnalyticsTracker.userAgent = userAgent;
		GoogleAnalyticsTracker.userID = userID;
	}
	
	public static RequestBuilder builder()
	{
		return new RequestBuilder();
	}
	
	public static class RequestBuilder
	{
		private Array<String> payloads = new Array<String>();
		
		public ExceptionBuilder exception() {
			return new ExceptionBuilder(this);
		}
		
		public EventBuilder event(String category, String action) {
			return new EventBuilder(this, category, action);
		}
		
		public void send()
		{
			send(responseLogger);
		}
		public void send(HttpResponseListener responseListener)
		{
			if(disabled){
				Gdx.app.log("GAService", "Skip disabled");
				Gdx.app.log("GAService", userAgent);
				for(String content : payloads)
					Gdx.app.log("GAService", "request : " + content);
			}
			else if(payloads.size == 1) {
				HttpRequest httpRequest = newRequest()
						.method("POST")
						.url("https://www.google-analytics.com/collect")
						.content(payloads.first())
						.build();
				Gdx.net.sendHttpRequest(httpRequest, responseListener);
				
			}
			else if(payloads.size > 0)
			{
				while(payloads.size > 0){
					String content = null;
					for(int i=0 ; i<20 && payloads.size>0 ; i++){
						if(content == null){
							content = payloads.removeIndex(0);
						}else{
							content += "\n" + payloads.removeIndex(0);
						}
					}
					HttpRequest httpRequest = newRequest()
							.method("POST")
							.url("https://www.google-analytics.com/batch")
							.content(content)
							.build();
					Gdx.net.sendHttpRequest(httpRequest, responseListener);
				}
			}
			payloads.clear();
		}
		
		private HttpRequestBuilder newRequest(){
			HttpRequestBuilder builder = new HttpRequestBuilder().newRequest();
			// user agent is automatically set in HTML5 context and can't be overriden (causing JS error)
			if(Gdx.app.getType() != ApplicationType.WebGL){
				builder.header(HttpRequestHeader.UserAgent, userAgent);
			}
			return builder;
		}
	}
	
	private abstract static class MeasurementBuilder
	{
		private RequestBuilder parentBuilder;
		
		public MeasurementBuilder(RequestBuilder parentBuilder) {
			super();
			this.parentBuilder = parentBuilder;
		}

		abstract protected void compile(Map<String, String> parameters);
		abstract protected String type();
		
		public RequestBuilder next() {
			
			Map<String, String> parameters = new HashMap<String, String>();
			
			// mandatory
			parameters.put("v", "1");
			parameters.put("tid", trackingID);
			parameters.put("cid", userID); // client
			
			parameters.put("t", type());
			
			compile(parameters);
			
			parentBuilder.payloads.add(HttpParametersUtils.convertHttpParameters(parameters));
			
			return parentBuilder;
		}
	}
	
	public static class EventBuilder extends MeasurementBuilder
	{
		private String category, action, label;
		private Integer value;
		
		public EventBuilder(RequestBuilder parentBuilder, String category, String action)
		{
			super(parentBuilder);
			this.category = category;
			this.action = action;
		}
		

		public EventBuilder label(String label) {
			this.label = label;
			return this;
		}
		
		public EventBuilder value(Integer value) {
			this.value = value;
			return this;
		}

		@Override
		protected void compile(Map<String, String> parameters) 
		{
			parameters.put("ec", category); // category
			parameters.put("ea", action); // action
			if(label != null){
				parameters.put("el", label); // label
			}
			if(value != null){
				parameters.put("ev", String.valueOf(value)); // value optional
			}
		}

		@Override
		protected String type() {
			return "event";
		}
	}
	
	public static class ExceptionBuilder extends MeasurementBuilder
	{
		private boolean fatal;
		private String description;

		public ExceptionBuilder(RequestBuilder parentBuilder) {
			super(parentBuilder);
		}

		public ExceptionBuilder fatal(boolean fatal){
			this.fatal = fatal;
			return this;
		}
		
		public ExceptionBuilder description(Throwable e){
			
			// shrink as much as possible to fit the 150 bytes limit.
			
			// TODO take care of any errors in error reporting !
			Throwable c = e;
			String message = "";
			while(c != null){
				StackTraceElement st = c.getStackTrace()[0];
				String [] name = st.getClassName().split("\\.");
				message += c.getClass().getSimpleName() + ":" + name[name.length-1] + ":" + st.getLineNumber() + ":";
				if(c.getCause() == null) message += c.getMessage();
				c = c.getCause();
			}
			
			description(message);
			
//			StringWriter writer = new StringWriter();
//			PrintWriter printer = new PrintWriter(writer);
//			e.printStackTrace(printer);
//			printer.close();
//			description(writer.toString());
			
			
			// XXX description(e.getClass().getSimpleName() + ":" + e.getMessage());
			return this;
		}
		
		public ExceptionBuilder description(String description){
			// limit to 150 bytes as required in API
			// https://developers.google.com/analytics/devguides/collection/protocol/v1/parameters#exd
			if(description != null){
				this.description = truncateToFitUtf8ByteLength(description, 150);
			}else{
				this.description = null;
			}
			return this;
		}
		
		/* GWT compatible method (not accurate) */
		private static String truncateToFitUtf8ByteLength(String s, int maxBytes)
		{
			int len = maxBytes;
			while(s.getBytes().length > maxBytes){
				s = s.substring(0, len);
				len--;
			}
			return s;
		}
		
		/*
		private static String truncateToFitUtf8ByteLength(String s, int maxBytes) 
		{
		    Charset charset = Charset.forName("UTF-8");
		    CharsetDecoder decoder = charset.newDecoder();
		    byte[] sba = s.getBytes(charset);
		    if (sba.length <= maxBytes) {
		        return s;
		    }
		    // Ensure truncation by having byte buffer = maxBytes
		    ByteBuffer bb = ByteBuffer.wrap(sba, 0, maxBytes);
		    CharBuffer cb = CharBuffer.allocate(maxBytes);
		    // Ignore an incomplete character
		    decoder.onMalformedInput(CodingErrorAction.IGNORE);
		    decoder.decode(bb, cb, true);
		    decoder.flush(cb);
		    return new String(cb.array(), 0, cb.position());
		}
		*/

		@Override
		protected void compile(Map<String, String> parameters) {
			if(fatal) parameters.put("exf", "1");
			if(description != null) parameters.put("exd", description);
		}
		
		@Override
		protected String type() {
			return "exception";
		}
		
	}
	
}

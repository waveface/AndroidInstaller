package com.waveface.installer.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class HttpInvoker {
	private static final String TAG = "Installer";
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static String getStringFromUrl(String url)  {
		String content = null;
		content = retry(url, 0);
		return content;
	}
	public static String retry(String url,int count){
		String content = "";
		int countLimit = 3;
			if(count < countLimit){
			    Log.d(TAG, "URL(time:"+(count+1)+"):"+url);			
				HttpGet request = new HttpGet(url);
				request.setHeader("User-Agent", "Android");
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response;
				try {
					response = httpclient.execute(request);
					BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
					StringBuilder builder = new StringBuilder();
					for (String line = null; (line = reader.readLine()) != null;) {
					    builder.append(line).append("\n");
					}
					content = builder.toString();
//					content = EntityUtils.toString(response.getEntity());
				} catch (ClientProtocolException e) {
					Log.d(TAG, e.getMessage());
				} catch (IOException e) {
					Log.d(TAG, e.getMessage());
				}
			}
	
		return content;
	}
}

package com.igrapesinc.securekids;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	
	//Constructor
	public JSONParser() {
		
	}
	
	//Function to get json from url
	//by making HTTP POST or GET method
	public JSONObject makeHttpRequest(String url, String method,
			List<NameValuePair> params) {
		
		//Making HTTP request
		try {
			//Check for request method
			if(method == "POST"){
				//Request method is POST
				//defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));
				Log.d("Secure Kids", url);
				
				HttpResponse httpResponse = httpClient.execute(httpPost);
				Log.d("Secure Kids", "Set httpResponse");
				HttpEntity httpEntity = httpResponse.getEntity();
				Log.d("Secure Kids", "Set httpResponse");
				is = httpEntity.getContent();
				Log.d("Secure Kids", "Set input string");
			} else if(method == "GET"){
				//Request method is GET
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				HttpGet httpGet = new HttpGet(url);
				
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			} 
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			//Log.d("Secure Kids", "Entered try statement");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			//Log.d("Secure Kids", "Created buffer reader");
			StringBuilder sb = new StringBuilder();
			String line = null;
			/*while ((line = reader.readLine()) != null){
				sb.append(line + "\n");
			}*/
			while ((line = reader.readLine()) != null) {
				if(!line.startsWith("<", 0)){
					if(!line.startsWith("(", 0)){
						sb.append(line + "\n");
					}
				}
			}
			is.close();
			//Log.d("Secure Kids", "Closed input string");
			json = sb.toString();
			Log.d("Secure Kids", json);
			//Log.d("Secure Kids", "Set json");
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		
		//Try to parse the string to a JSON object
		try {
			//Log.d("Secure Kids", json);
			jObj = new JSONObject(json);
			//Log.d("Secure Kids", "Created json object");
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		
		//Return JSON String
		return jObj;
		
	}
	
}

package org.binaryfissiongames.cloudbit;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Utils {
	private static float EPSILON = 0.00001f;
	public static HttpsURLConnection connectToCloudbit(String link, String authToken, String method){
		try{
			URL url = new URL(link);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod(method);
			con.addRequestProperty("Authorization", "Bearer " + authToken);
			return con;
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0xbad);
		}
		return null;
	}
	public static boolean withinEpsilon(float a, float b){
		return Math.abs(a - b) < EPSILON;
	}
}

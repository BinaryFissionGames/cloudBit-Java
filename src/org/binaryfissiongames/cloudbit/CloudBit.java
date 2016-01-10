package org.binaryfissiongames.cloudbit;
import java.io.*;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class CloudBit {
	public final static int TIME_INFINITE = -1;
	private String ID;
	public CloudBit(String cloudBitID){
		ID = cloudBitID;
	}
	//TODO fix this funciton, if the problem is on my end, but I'm fairly certain it's not.
	public float getVoltage(String authToken){
		HashMap<JSONNode, JSONNode> vals; 
		HttpsURLConnection con = Utils.connectToCloudbit("https://api-http.littlebitscloud.cc/v3/devices/" + ID + "/input", authToken, "GET");
		
		con.setUseCaches(false);
		con.setDoInput(true);
		con.setDoOutput(false);
		
		//Response:
		try{
			BufferedReader rd;
			StringBuilder response = new StringBuilder();
			String line;
			
			if(con.getResponseCode() != 200){
				rd = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				while((line = rd.readLine()) != null){
					response.append(line);
					response.append('\n');
				}
				vals = JSONParser.parse(response.toString());
				
				System.err.println("An error occured!");
				System.err.println("Status code: " + JSONParser.getByName("statusCode", vals));
				System.err.println("Error: " + JSONParser.getByName("error", vals));
				System.err.println("Message: " + JSONParser.getByName("message", vals));
				
				
				rd.close();
				con.disconnect();
				return -1;
			}else{
				rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
				int i = 0;
				while((line = rd.readLine()) != null && i<1){
					response.append(line);
					response.append('\n');
					i++;
				}
				vals = JSONParser.parse(response.toString());
				rd.close();
				con.disconnect();
				return (float)(JSONParser.getByName("percent", vals).getValue());
			}
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0xbad);
		}
		return -1;
	}
	public boolean outputVoltage(String authToken, float percent, int duration_ms){
		HttpsURLConnection con = Utils.connectToCloudbit("https://api-http.littlebitscloud.cc/v3/devices/" + ID + "/output", authToken, "POST");
		String send = "percent=" + percent +"&duration_ms=" + duration_ms;
		HashMap<JSONNode, JSONNode> vals;
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("Content-Length", Integer.toString(send.getBytes().length));
		con.setUseCaches(false);
		con.setDoInput(true);
		con.setDoOutput(true);
		
		try{
			BufferedReader rd;
			StringBuilder response = new StringBuilder();
			String line;
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(send);
			out.close();
			
			if(con.getResponseCode() != 200){
				rd = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				while((line = rd.readLine()) != null){
					response.append(line);
					response.append('\n');
				}
				vals = JSONParser.parse(response.toString());
				
				System.err.println("An error occured!");
				System.err.println("Status code: " + JSONParser.getByName("statusCode", vals));
				System.err.println("Error: " + JSONParser.getByName("error", vals));
				System.err.println("Message: " + JSONParser.getByName("message", vals));
				
				
				rd.close();
				con.disconnect();
				return false;
			}else{
				rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
				while((line = rd.readLine()) != null){
					response.append(line);
					response.append('\n');
				}
				vals = JSONParser.parse(response.toString());
				if(((boolean)JSONParser.getByName("success", vals).getValue()) == true){
					return true;
				}else{
					return false;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	//TODO Not exactly sure what the relevant stuff to return is here, but I'll probably wrap it up in a class.
	public String getDeviceModel(String authToken){
		HttpsURLConnection con = Utils.connectToCloudbit("https://api-http.littlebitscloud.cc/v3/devices/" + ID, authToken, "GET");
		HashMap<JSONNode, JSONNode> vals;
		con.setUseCaches(false);
		con.setDoInput(true);
		con.setDoOutput(false);
		
		try{
			BufferedReader rd;
			StringBuilder response = new StringBuilder();
			String line;
			
			if(con.getResponseCode() != 200){
				rd = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				while((line = rd.readLine()) != null){
					response.append(line);
					response.append('\n');
				}
				vals = JSONParser.parse(response.toString());
				
				System.err.println("An error occured!");
				System.err.println("Status code: " + JSONParser.getByName("statusCode", vals));
				System.err.println("Error: " + JSONParser.getByName("error", vals));
				System.err.println("Message: " + JSONParser.getByName("message", vals));
				
				
				rd.close();
				con.disconnect();
				return null;
			}else{
				rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
				int i = 0;
				while((line = rd.readLine()) != null && i<1){
					response.append(line);
					response.append('\n');
					i++;
				}
				System.out.println(response.toString());
				vals = JSONParser.parse(response.toString());
				rd.close();
				con.disconnect();
				//TODO return proper object
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	//TODO Implement method
	public boolean updateDeviceModel(String authToken){
		HttpsURLConnection con = Utils.connectToCloudbit("https://api-http.littlebitscloud.cc/v3/devices/" + ID, authToken, "PUT");
		String send = "";
		HashMap<JSONNode, JSONNode> vals;
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("Content-Length", Integer.toString(send.getBytes().length));
		con.setUseCaches(false);
		con.setDoInput(true);
		con.setDoOutput(true);
		try{
			BufferedReader rd;
			StringBuilder response = new StringBuilder();
			String line;
		}catch(Exception e){
			
		}
		return true;
	}
	//TODO implement method
	public boolean activateDevice(String authToken){
		HttpsURLConnection con = Utils.connectToCloudbit("https://api-http.littlebitscloud.cc/v3/devices/" + ID, authToken, "POST");
		String send = "";
		HashMap<JSONNode, JSONNode> vals;
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("Content-Length", Integer.toString(send.getBytes().length));
		con.setUseCaches(false);
		con.setDoInput(true);
		con.setDoOutput(true);
		try{
			BufferedReader rd;
			StringBuilder response = new StringBuilder();
			String line;
		}catch(Exception e){
			
		}
		return true;
	}
	//TODO implement method	
	public boolean deactivateDevice(String authToken){
		HttpsURLConnection con = Utils.connectToCloudbit("https://api-http.littlebitscloud.cc/v3/devices/" + ID, authToken, "DELETE");
		try{
			BufferedReader rd;
			StringBuilder response = new StringBuilder();
			String line;
		}catch(Exception e){
			
		}
		return true;
	}
	//TODO implement method
	public String[] getDeviceSubscriptions(String authToken){
		HttpsURLConnection con = Utils.connectToCloudbit("https://api-http.littlebitscloud.cc/v3/subscriptions", authToken, "GET");
		HashMap<JSONNode, JSONNode> vals;
		con.setUseCaches(false);
		con.setDoInput(true);
		con.setDoOutput(false);
		try{
			BufferedReader rd;
			StringBuilder response = new StringBuilder();
			String line;
		}catch(Exception e){
			
		}		
		return null;
	}
	//TODO implement method
	//TODO events should be their own class, not of type String
	public boolean publishEvents(String authToken, String[] events){
		HttpsURLConnection con = Utils.connectToCloudbit("https://api-http.littlebitscloud.cc/v3/subscriptions", authToken, "POST");
		String send = "";
		HashMap<JSONNode, JSONNode> vals;
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("Content-Length", Integer.toString(send.getBytes().length));
		con.setUseCaches(false);
		con.setDoInput(true);
		con.setDoOutput(true);
		try{
			BufferedReader rd;
			StringBuilder response = new StringBuilder();
			String line;
		}catch(Exception e){
			
		}		
		return true;
	}
	//TODO implement method
	public boolean stopPublishingEvents(String authToken, String[] events){
		HttpsURLConnection con = Utils.connectToCloudbit("https://api-http.littlebitscloud.cc/v3/subscriptions", authToken, "DELETE");
		HashMap<JSONNode, JSONNode> vals;
		
		con.setUseCaches(false);
		con.setDoInput(true);
		con.setDoOutput(false);
		try{
			BufferedReader rd;
			StringBuilder response = new StringBuilder();
			String line;
		}catch(Exception e){
			
		}
		return true;
	}
	public String toString(){
		return ID;
	}
}

package org.binaryfissiongames.cloudbit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class JSONParser {
	//TODO add escape character support
	public static HashMap<JSONNode, JSONNode> parse(String json){
		HashMap<JSONNode, JSONNode> ret = new HashMap<JSONNode, JSONNode>(32, (float) 0.5);
		String curName;
		String curValue;
		int type;
		int i = 0;
		//find opening bracket
		while(i<json.length()){
			if(json.charAt(i) == '{'){
				i++;
				break;
			}
		}
		while(i<json.length()){
			curName = "";
			type = 0;
			curValue = "";
			int startVal;
			int jsonDepth;
			//Go until we have a value...
			while(i<json.length() && Character.isWhitespace(json.charAt(i))){
				i++;
			}
			//Now we found the name for a value, this should always be a String.
			if(i<json.length() && (json.charAt(i) == '\'' ||json.charAt(i) == '\"')){
				i++;
			}
			//Copy the name...
			while(i<json.length() && !(json.charAt(i) == '\'' ||json.charAt(i) == '\"' || json.charAt(i) == ':')){
				curName += json.charAt(i);
				i++;
			}
			//Name copied, now we go to the space after the :
			while(i<json.length() && json.charAt(i) != ':'){
				i++;
			}
			i++;
			//go to the next non whitespace character
			while(i<json.length() && Character.isWhitespace(json.charAt(i))){
				i++;
			}
			//Do some checking, we need to know what type this is
			switch(json.charAt(i)){
				case '\'': 
					//Now copy over the name
					i++;
					while(i<json.length() && json.charAt(i) != '\''){
						curValue += json.charAt(i);
						i++;
					}
					i++;
					ret.put(new JSONNode(JSONNode.JSON_STRING, curName),  new JSONNode(JSONNode.JSON_STRING, curValue));
					break;
				case '\"':
					i++;
					while(i<json.length() && json.charAt(i) != '\"'){
						curValue += json.charAt(i);
						i++;
					}
					i++;
					ret.put(new JSONNode(JSONNode.JSON_STRING, curName),  new JSONNode(JSONNode.JSON_STRING, curValue));
					break;
				case '[':
					jsonDepth = 1;
					startVal = i;
					i++;
					while(i<json.length()){
						if(json.charAt(i) == '['){
							jsonDepth++;
						}else if(json.charAt(i) == ']'){
							jsonDepth --;
							if(jsonDepth == 0){
								break;
							}
						}
						i++;
					}
					ret.put(new JSONNode(JSONNode.JSON_STRING, curName), new JSONNode(JSONNode.JSON_ARRAY, JSONParser.arrayHelper(json.substring(startVal, ++i))));
					break;
				case '{':
					jsonDepth = 1;
					startVal = i;
					i++;
					while(i<json.length()){
						if(json.charAt(i) == '{'){
							jsonDepth++;
						}else if(json.charAt(i) == '}'){
							jsonDepth --;
							if(jsonDepth == 0){
								break;
							}
						}
						i++;
					}
					//Note the pre increment, i is now past the inner array
					ret.put(new JSONNode(JSONNode.JSON_STRING, curName), new JSONNode(JSONNode.JSON_JSON, JSONParser.parse(json.substring(startVal, ++i))));
					break;
				case 't':
					while(i<json.length() && json.charAt(i)!='e'){
						i++;
					}
					i++;
					ret.put(new JSONNode(JSONNode.JSON_STRING, curName), new JSONNode(JSONNode.JSON_BOOLEAN, true));
					break;
				case 'f':
					while(i<json.length() && json.charAt(i)!='e'){
						i++;
					}
					i++;
					ret.put(new JSONNode(JSONNode.JSON_STRING, curName), new JSONNode(JSONNode.JSON_BOOLEAN, true));
					break;
				default:
					type= JSONNode.JSON_INT;
					while(i<json.length()){
						if(json.charAt(i) == '.'){
							type=JSONNode.JSON_FLOAT;
						}
						if(Character.isWhitespace(json.charAt(i)) || (json.charAt(i) == ',' ) || (json.charAt(i) == '}' )){
							break;
						}
						curValue += json.charAt(i);
						i++;
					}
					if(type == JSONNode.JSON_INT){
						ret.put(new JSONNode(JSONNode.JSON_STRING, curName), new JSONNode(JSONNode.JSON_INT, Integer.parseInt(curValue)));
					}else{
						ret.put(new JSONNode(JSONNode.JSON_STRING, curName), new JSONNode(JSONNode.JSON_FLOAT, Float.parseFloat(curValue)));
					}
					break;
			}
			//Now go to the beginning of the next value, after the ',' character
			while(i<json.length() && (json.charAt(i) != ',')){
				i++;
			}
			//on the character ','
			//find the next one. If it reaches json.length(), (AKA no',' is found), it will exit the loop.
			i++;
		}
		return ret;
	}
	private static JSONNode[] arrayHelper(String array){
		//Gets an array, including the [] tags, and turns it into a JSONNode array.
		ArrayList<JSONNode> arrayList = new ArrayList<JSONNode>();
		String val;
		int i = 0;
		int jsonDepth = 0;
		int startVal = 0;
		int type;
		
		while(i<array.length() && array.charAt(i)!='['){
			i++;
		}
		i++; //we are in the array
		//Now we find out what the type of the particular part is.
		while(i<array.length()){
			val = "";
			switch(array.charAt(i)){
				//Array within an array!
				case '[':
					jsonDepth = 1;
					startVal = i;
					i++;
					while(i<array.length()){
						if(array.charAt(i) == '['){
							jsonDepth++;
						}else if(array.charAt(i) == ']'){
							jsonDepth --;
							if(jsonDepth == 0){
								break;
							}
						}
						i++;
					}
					arrayList.add(new JSONNode(JSONNode.JSON_ARRAY, arrayHelper(array.substring(startVal, ++i))));
					break;
				case '\'':
					i++;
					while(i<array.length() && array.charAt(i) != '\''){
						val += array.charAt(i);
						i++;
					}
					i++;
					arrayList.add(new JSONNode(JSONNode.JSON_STRING, val));
					break;
				case '\"':
					i++;
					while(i<array.length() && array.charAt(i) != '\"'){
						val += array.charAt(i);
						i++;
					}
					i++;
					arrayList.add(new JSONNode(JSONNode.JSON_STRING, val));
					break;
				case '{':
					jsonDepth = 1;
					startVal = i;
					i++;
					while(i<array.length()){
						if(array.charAt(i) == '{'){
							jsonDepth++;
						}else if(array.charAt(i) == '}'){
							jsonDepth --;
							if(jsonDepth == 0){
								break;
							}
						}
						i++;
					}
					arrayList.add(new JSONNode(JSONNode.JSON_JSON, JSONParser.parse(array.substring(startVal, ++i))));
					break;
				case 't':
					while(i<array.length() && array.charAt(i)!='e'){
						i++;
					}
					i++;
					arrayList.add(new JSONNode(JSONNode.JSON_BOOLEAN, true));
					break;
				case 'f':
					while(i<array.length() && array.charAt(i)!='e'){
						i++;
					}
					i++;
					arrayList.add(new JSONNode(JSONNode.JSON_BOOLEAN, false));
					break;
				default:
					type= JSONNode.JSON_INT;
					while(i<array.length()){
						if(array.charAt(i) == '.'){
							type=JSONNode.JSON_FLOAT;
						}
						if(Character.isWhitespace(array.charAt(i)) || (array.charAt(i) == ',') || (array.charAt(i) == ']')){
							break;
						}
						val += array.charAt(i);
						i++;
					}
					if(val.compareTo("") == 0){
						arrayList.add(new JSONNode(JSONNode.JSON_STRING, null));
					}else if(type == JSONNode.JSON_INT){
						arrayList.add(new JSONNode(JSONNode.JSON_INT, Integer.parseInt(val)));
					}else{
						arrayList.add(new JSONNode(JSONNode.JSON_FLOAT, Float.parseFloat(val)));
					}
					break;
			}
			//Now go to the beginning of the next value, after the ',' character
			while(i<array.length() && (array.charAt(i) != ',')){
				i++;
			}
		}
		return arrayList.toArray(new JSONNode[0]);
	}
	public static String stringify(HashMap<JSONNode, JSONNode> nodes){
		Iterator<JSONNode> keyIt = nodes.keySet().iterator();
		StringBuilder s = new StringBuilder();
		JSONNode node;
		
		s.append("{");
		while(keyIt.hasNext()){
			node = keyIt.next();
			s.append(node.toString());
			s.append(":");
			s.append(nodes.get(node).toString());
			s.append(",");
		}
		s.deleteCharAt(s.length() - 1);
		s.append("}");
		return s.toString();
	}
	public static JSONNode getByName(String name, HashMap<JSONNode, JSONNode> nodes){
		return nodes.get(new JSONNode(JSONNode.JSON_STRING, name));
	}
}

package org.binaryfissiongames.cloudbit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class JSONNode{
	public static final int JSON_STRING = 0, JSON_INT = 1, JSON_FLOAT = 2, JSON_JSON = 3, JSON_BOOLEAN = 4, JSON_ARRAY = 5;
	
	private Object value;
	private int type;
	public JSONNode(int typ, Object val){
		value = val;
		if(typ >= JSON_STRING && typ <= JSON_ARRAY ){
			type = typ;
		}else{
			type = JSON_STRING;
		}
	}
	public Object getValue(){
		return value;
	}
	public int getType(){
		return type;
	}
	public void setValue(int typ, Object val){
		value = val;
		type = typ;
	}
	@SuppressWarnings("unchecked")
	public boolean equals(Object a){
		if(!(a instanceof JSONNode)){
			return false;
		}
		JSONNode n = (JSONNode)a;
		switch(type){
			case JSON_STRING:
				if(n.getType() == type){
					return ((String)value).equals(n.getValue());
				}
				return false;
			case JSON_INT:
				if(n.getType() == type){
					return (((int)value) == ((int)n.getValue()));
				}
				return false;
			case JSON_FLOAT:
				if(n.getType() == type){
					return Utils.withinEpsilon((float)value, (float)n.getValue());
				}
				return false;
			case JSON_JSON:
				if(n.getType() == type){
					HashMap<JSONNode, JSONNode> json = (HashMap<JSONNode, JSONNode>)value;
					HashMap<JSONNode, JSONNode> jsonOther = (HashMap<JSONNode, JSONNode>)n.getValue();
					Iterator<JSONNode> keysIt = json.keySet().iterator();
					
					JSONNode test;
					
					if(json.size() != jsonOther.size()){
						return false;
					}
					while(keysIt.hasNext()){
						test = keysIt.next();
						if(!(json.get(test).equals(jsonOther.get(test)))){
							return false;
						}
					}
					return true;
				}
				return false;
			case JSON_ARRAY:
				if(n.getType() == type){
					JSONNode[] array = (JSONNode[])value;
					JSONNode[] arrayOther = (JSONNode[])n.getValue();
					if(array.length != arrayOther.length){
						return false;
					}
					for(int i = 0;i<array.length;i++){
						if(!(array[i].equals(arrayOther[i]))){
							return false;
						}
					}
					return true;
				}
				return false;
			case JSON_BOOLEAN:
				if(n.getType() == type){
					if(((boolean)n.getValue()) == ((boolean)value)){
						return true;
					}
					return false;
				}
			default:
				return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public int hashCode(){
		int hash = 0;
		int i = 0;
		JSONNode next;
		switch(type){
			case JSON_STRING:
				return ((String)value).hashCode();
			case JSON_INT:
				return Integer.hashCode((int)value);
			case JSON_FLOAT:
				return Float.hashCode((float)value);
			case JSON_JSON:
				HashMap<JSONNode, JSONNode> json = (HashMap<JSONNode, JSONNode>)value;
				Iterator<JSONNode> keysIt = json.keySet().iterator();
				while(keysIt.hasNext()){
					next = keysIt.next();
					hash += (next.hashCode() * Math.pow(31, json.size() - 1 - i));
					i++;
				}
				return hash;
			case JSON_ARRAY:
				JSONNode[] array = (JSONNode[])value;
				for(i = 0;i<array.length;i++){
					hash += (array[i].hashCode() * Math.pow(31, array.length - 1 - i));
				}
				return hash;
			case JSON_BOOLEAN:
				if((boolean)value){
					return 1;
				}
				return 0;
			default:
				return 0;
		}
	}
	
	@SuppressWarnings("unchecked")
	public String toString(){
		StringBuilder s = new StringBuilder();
		switch(type){
			case JSON_STRING:
				s.append("\"");
				s.append((String)value);
				s.append("\"");
				break;
			case JSON_INT:
				s.append(Integer.toString((int)value));
				break;
			case JSON_FLOAT:
				s.append(Float.toString((float)value));
				break;
			case JSON_JSON:
				s.append(JSONParser.stringify((HashMap<JSONNode, JSONNode>) value));
				break;
			case JSON_ARRAY:
				JSONNode[] array = (JSONNode[])value;
				s.append("[");
				for(int i = 0;i<array.length;i++){
					s.append(array[i].toString());
					s.append(", ");
				}
				s.delete(s.length() - 2, s.length());
				s.append("]");
				break;
			case JSON_BOOLEAN:
				if((boolean)value){
					s.append("true");
				}else{
					s.append("false");
				}
				break;
			default:
				return null;
		}
		return s.toString();
	}
	
}

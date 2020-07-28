package com.serviceImpl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.xml.transform.TransformerException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.service.XMLJSONConverter;

public class ConvertorFactory implements XMLJSONConverter {
	
	public void createXMLJSONConvertor(String xmlPath, String jsonPath) {

		JSONParser jsonParser = new JSONParser();
		FileReader reader;
		try {
			reader = new FileReader(jsonPath);
		
		Object obj = jsonParser.parse(reader);

		Document doc = DocumentHelper.createDocument();

		String str = getUserDefinedName(obj.getClass().getSimpleName());
		Element rootElement = doc.addElement(str);
		createXML(obj, str, doc, rootElement);

		FileWriter out = new FileWriter(xmlPath);
		doc.write(out);
		out.close();
	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}


	private static void createXML(Object obj, String str, Document doc, Element rootElement)
			throws TransformerException {

		if (str.equals("object")) {
					
			Set<String> keys = ((HashMap) obj).keySet();
			JSONObject jsonObject = (JSONObject) obj;

			for (String k : keys) {
				

				String str1;

				if (jsonObject.get(k) != null) {

					str1 = getUserDefinedName(jsonObject.get(k).getClass().getSimpleName());
				} else {
					str1 = "null";

				}

				if (str1.equals("object")) {

					
					if (jsonObject.get(k) != null) {
						Element subElement = rootElement.addElement("object");
						subElement.addElement("name", k);
						createXML((jsonObject.get(k)), getUserDefinedName(jsonObject.get(k).getClass().getSimpleName()),
								doc, subElement);

					} else {
						Element subElement = rootElement.addElement("object");
						subElement.addElement("name", k);
						createXML("null", getUserDefinedName("null"), doc, subElement);
					}

				} else if (str1.equals("array")) {
					
					Element subElement = rootElement.addElement("array");
					subElement.addAttribute("name", k);
					JSONArray jsonArray = (JSONArray) jsonObject.get(k);
					addChildNode(jsonArray, doc, subElement);

				} else if (str1.equals("null")) {
					Element subElement = rootElement.addElement(str1);
					subElement.addAttribute("name", k);
					addChildNode("null", doc, subElement);

				} else {
					Element subElement = rootElement.addElement(str1);
					subElement.addAttribute("name", k);
					addChildNode(jsonObject.get(k), doc, subElement);

				}

			}

		} else if (str.equals("array")) {
			JSONArray jsonArray = (JSONArray) obj;
			addChildNode(jsonArray, doc, rootElement);

		} else {

			if (obj != null)
				rootElement.addText(obj.toString());

		}

	}

	public static void addChildNode(JSONArray arr, Document doc, Element arrElement) throws TransformerException {

		Iterator itr = arr.iterator();

		while (itr.hasNext()) {
			
			Object value = itr.next();
			
			
			if(value != null) {
				if (getUserDefinedName(value.getClass().getSimpleName()).equals("object")) {

					Element subElement = arrElement.addElement("object");
					createXML(value, getUserDefinedName(value.getClass().getSimpleName()), doc, subElement);
				}else if(getUserDefinedName(value.getClass().getSimpleName()).equals("array")) {
					
					Element subElement = arrElement.addElement("array");
					JSONArray jsonArray = (JSONArray) value;
					addChildNode(jsonArray, doc, subElement);
				}
				else {
					String nameItr = getUserDefinedName(value.getClass().getSimpleName());
					Element subElement = arrElement.addElement(nameItr);
					if (value != null)
						subElement.addText(value.toString());
				}
			}

		}

	}

	public static void addChildNode(Object jsonObject, Document doc, Element objElement) {

		if (jsonObject != null)
			objElement.addText((jsonObject.toString()));
	}

	private static String getUserDefinedName(String className) {
		String userName = "";
		if (className.equals("JSONObject")) {
			userName = "object";
		} else if (className.equals("JSONArray")) {
			userName = "array";
		} else if (className.equals("Double")) {
			userName = "number";
		} else if (className.equals("Long")) {
			userName = "number";
		} else {
			userName = className.toLowerCase();
		}
		return userName;

	}

	
}

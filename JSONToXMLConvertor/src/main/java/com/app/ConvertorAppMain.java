package com.app;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.service.XMLJSONConverter;
import com.serviceImpl.ConvertorFactory;

public class ConvertorAppMain {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		
		//String xmlPath = "D:\\google.json";
		//String jsonPath = "D:\\google.xml";
		XMLJSONConverter conv = new ConvertorFactory();
		conv.createXMLJSONConvertor(args[0],args[1]);

	}

}

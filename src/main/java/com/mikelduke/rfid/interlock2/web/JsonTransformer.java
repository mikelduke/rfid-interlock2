package com.mikelduke.rfid.interlock2.web;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public String render(Object model) throws JsonGenerationException, JsonMappingException, IOException {
		return mapper.writeValueAsString(model);
	}
	
	public static String toJson(Object model) throws JsonGenerationException, JsonMappingException, IOException {
		String res = mapper.writeValueAsString(model);
		
		System.out.println("CONVERTED: " + res);
		return res;
	}
}
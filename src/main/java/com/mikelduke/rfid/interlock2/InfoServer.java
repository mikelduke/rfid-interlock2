package com.mikelduke.rfid.interlock2;

import spark.Request;
import spark.Response;

public class InfoServer {
	//TODO This should hold the spark resources for tool info
	
	public static String status(Request req, Response res) {
		return "hello";
	}
}

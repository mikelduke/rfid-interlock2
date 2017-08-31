package com.mikelduke.rfid.interlock2;

import java.util.logging.Level;
import java.util.logging.Logger;

import spark.Spark;

public class Application {
	private static final String CLAZZ = Application.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLAZZ);
	
	public static void main(String[] args) {
		if (args.length > 0) {
			int port = Integer.parseInt(args[0]);
			Spark.port(port);
		}
		
		Spark.get("/", (req, res) -> "hello");
		
		Spark.awaitInitialization();
		LOGGER.logp(Level.INFO, CLAZZ, "main", "Server started on port " + Spark.port());
	}
}

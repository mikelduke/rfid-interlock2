package com.mikelduke.servermock.makertracker;

import java.util.Arrays;

import com.mikelduke.rfid.interlock2.JsonTransformer;
import com.mikelduke.rfid.interlock2.makertracker.MakerTrackerDTO;

import spark.Spark;

public class MakerTrackerTestService {

	public static void main(String[] args) {
		start();
	}
	
	public static void start() {
		Spark.port(8089);
		Spark.get("/api/interlock/:toolid/rfids", (req, res) -> {
			res.type("application/json");
			return MakerTrackerDTO.builder()
				.accessTimeMS(1000)
				.assetId(123)
				.rfidList(Arrays.asList("rfid-1", "rfid-2"))
				.build();
		}, JsonTransformer::toJson);
		
		Spark.get("/api/interlock/:toolid/rfids/:rfid", (req, res) -> "1000");

		Spark.awaitInitialization();
	}
	
	public static void stop()  {
		Spark.stop();
	}
}

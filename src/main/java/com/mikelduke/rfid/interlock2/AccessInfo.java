package com.mikelduke.rfid.interlock2;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessInfo {
	private final String toolId;
	private final long accessTimeMS;
	private final List<String> rfidList;
	
	private final long retrievedAt = System.currentTimeMillis();
	
	public String getRetrievedAtTime() {
		LocalDateTime ldt = LocalDateTime.ofInstant(
				Instant.ofEpochMilli(this.retrievedAt), ZoneId.systemDefault());
		return ldt.toString();
	}
}

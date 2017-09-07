package com.mikelduke.rfid.interlock2;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
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

	@JsonCreator
	public AccessInfo(
			@JsonProperty("toolId") String toolId, 
			@JsonProperty("accessTimeMS") long accessTimeMS, 
			@JsonProperty("rfidList") List<String> rfidList) {
		this.toolId = toolId;
		this.accessTimeMS = accessTimeMS;
		this.rfidList = rfidList;
	}	
}

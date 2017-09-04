package com.mikelduke.rfid.interlock2;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessInfo {
	private final String toolId;
	private final long accessTimeMS;
	private final List<String> rfidList;
}

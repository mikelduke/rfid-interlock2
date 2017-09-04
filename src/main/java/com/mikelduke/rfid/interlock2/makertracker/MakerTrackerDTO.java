package com.mikelduke.rfid.interlock2.makertracker;

import java.util.List;

import lombok.Data;

@Data
public class MakerTrackerDTO {
	private long assetId;
	private List<String> rfidList;
	
	private boolean trainingRequired;
	
	private long accessTimeMS;
}

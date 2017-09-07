package com.mikelduke.rfid.interlock2.makertracker;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MakerTrackerDTO {
	private long assetId;
	private List<String> rfidList;
	
	private boolean trainingRequired;
	
	private long accessTimeMS;
}

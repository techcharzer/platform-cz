package com.cz.platform.dto.order;

import com.cz.platform.dto.AddressDTOV2;

import lombok.Data;

@Data
public class FreeInstallationOrderConfiguration implements OrderConfiguration {
	private Long loadAvailableInWh;
	private AddressDTOV2 address;
	private String shopName;
	private Double premiseOwnerShare;
	private Long electricityRates;
	private String electricityProof;
	private String shopImage;
}

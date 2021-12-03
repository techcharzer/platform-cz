package com.cz.platform.dto;

import java.util.List;
import java.util.Map;

import com.cz.platform.charger.configuration.GlobalChargerHardwareInfo;
import com.cz.platform.enums.ChargerStatus;
import com.cz.platform.enums.VehicleType;

import lombok.Data;

@Data
public class ChargerDTO {
	private String chargerId;
	private String name;
	private ChargerStatus status;
	private Range<Integer> openCloseTimeInSeconds;
	private List<VehicleType> supportedVehicle;
	private List<Image> images;
	private GlobalChargerHardwareInfo hardwareInfo;
	private Map<String, Long> price;
	private AddressDTO address;
	private DealConfigurationDTO dealConfiguration;

	public static final String[] INCLUDED_FIELDS = new String[] { "id", "name", "uniqueIdentifier",
			"openCloseTimeInSeconds", "sockets", "chargerType", "supportedVehicle", "address", "images", "deeplink",
			"configuration", "status", "dealConfiguration", "deeplink" };
}

package com.cz.platform.charger.configuration;

import java.io.Serializable;
import java.util.List;

import com.cz.platform.dto.HardwareSocket;

import lombok.Data;

@Data
public class GlobalChargerHardwareInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8410282833774514608L;
	private String hardwareId;
	private ChargerConfigurationDTO configuration;
	private String deeplink;
	private Boolean isActive;
	private List<HardwareSocket> sockets;
}

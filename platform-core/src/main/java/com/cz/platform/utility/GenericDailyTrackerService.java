package com.cz.platform.utility;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.cz.platform.clients.CustomRabbitMQTemplate;
import com.cz.platform.notifications.GenericRabbitQueueConfiguration;

import lombok.Data;

@Service
public class GenericDailyTrackerService {

	@Autowired
	private GenericRabbitQueueConfiguration rabbitQueueConfiguration;

	@Autowired
	private CustomRabbitMQTemplate template;

	@Value("${spring.application.name}")
	private String applicationName;

	public void updateValue(Instant instant, List<Pair<TrackerKey, Long>> values) {
		DailyTrackerSaveUpdateRequest request = new DailyTrackerSaveUpdateRequest();
		request.setTime(instant);
		Map<String, Long> map = new HashMap<>();
		for (Pair<TrackerKey, Long> val : values) {
			map.put(getKey(val.getFirst().getKey()), val.getSecond());
		}
		request.setKeyValuePair(map);
		template.convertAndSend(rabbitQueueConfiguration.getUpdateDailyTracker(), request);
	}

	public void updateValue(List<Pair<TrackerKey, Long>> values) {
		DailyTrackerSaveUpdateRequest request = new DailyTrackerSaveUpdateRequest();
		request.setTime(Instant.now());
		Map<String, Long> map = new HashMap<>();
		for (Pair<TrackerKey, Long> val : values) {
			map.put(getKey(val.getFirst().getKey()), val.getSecond());
		}
		request.setKeyValuePair(map);
		template.convertAndSend(rabbitQueueConfiguration.getUpdateDailyTracker(), request);
	}

	public void incrementValue(TrackerKey key) {
		incrementValue(key, Instant.now(), 1l);
	}

	public void incrementValue(TrackerKey key, Instant instant) {
		incrementValue(key, instant, 1l);
	}

	public void incrementValue(TrackerKey key, Instant instant, Long delta) {
		DailyTrackerSaveUpdateRequest request = new DailyTrackerSaveUpdateRequest();
		request.setTime(instant);
		Map<String, Long> map = new HashMap<>();
		map.put(getKey(key.getKey()), delta);
		request.setKeyValuePair(map);
		template.convertAndSend(rabbitQueueConfiguration.getUpdateDailyTracker(), request);
	}

	@Data
	static class DailyTrackerSaveUpdateRequest {
		private Instant time;
		private Map<String, Long> keyValuePair;
	}

	private String getKey(String key) {
		return MessageFormat.format("{0}#{1}", applicationName.toUpperCase(), key.toUpperCase());
	}

	public static interface TrackerKey {
		String getKey();
	}

}

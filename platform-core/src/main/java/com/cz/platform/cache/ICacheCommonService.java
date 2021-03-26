package com.cz.platform.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.ObjectUtils;

import com.cz.platform.exception.PlatFormExceptionCodes;
import com.cz.platform.exception.ValidationException;
import com.cz.platform.functionalInterface.AnonymousMethod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ICacheCommonService {

	@Autowired
	@Lazy
	protected ICacheCommonService cacheCommonService;

	protected static final Map<String, AnonymousMethod> REFRESH_KEY_METHOD = new HashMap<>();

	public void refreshCache(List<String> cacheIds) {
		if (ObjectUtils.isEmpty(cacheIds)) {
			return;
		}
		validateRequest(cacheIds);
		for (String keys : cacheIds) {
			log.info("clearing cache for key : {}", keys);
			REFRESH_KEY_METHOD.get(keys).execute();
		}
	}

	private void validateRequest(List<String> cacheIds) {
		for (String key : cacheIds) {
			if (!REFRESH_KEY_METHOD.containsKey(key)) {
				throw new ValidationException(PlatFormExceptionCodes.INVALID_DATA.getCode(),
						"Invalid cacheId : " + key);
			}
		}
	}

}
package com.cz.platform.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.cz.platform.exception.PlatformExceptionCodes;
import com.cz.platform.exception.ValidationException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class AdvancedFilterRepositoryImpl<T> implements AdvancedFilterRepository<T> {

	private MongoTemplate mongoTemplate;

	private GenericEntityToQueryCreatorFactory factory;

	@Override
	public Page<T> filter(List<AbstractFilter> filters, Pageable page, String[] includedFields, Class<T> clazz) {
		GenericFilterToQueryCreator queryMapper = factory.getService(clazz);
		Criteria criterias = queryMapper.getFilter(filters);
		Query query = new Query();
		if (!ObjectUtils.isEmpty(criterias)) {
			query.addCriteria(criterias);
		}
		log.debug("query: {} class : {}", query, clazz);
		validateFilters(filters);
		Long count = mongoTemplate.count(query, clazz);
		// adding pagination for list

		if (!ObjectUtils.isEmpty(includedFields)) {
			query.fields().include(includedFields);
		}

		query.with(page);
		List<T> list = mongoTemplate.find(query, clazz);
		return new PageImpl<>(list, page, count);
	}

	@Override
	public Page<T> filter(List<AbstractFilter> filters, Pageable page, Class<T> clazz) {
		return filter(filters, page, null, clazz);
	}

	@Override
	public List<T> filter(List<AbstractFilter> filters, Class<T> clazz) {
		return this.filter(filters, new String[0], clazz);
	}

	@Override
	public List<T> filter(List<AbstractFilter> filters, String[] includedFields, Class<T> clazz) {
		GenericFilterToQueryCreator queryMapper = factory.getService(clazz);
		Criteria criterias = queryMapper.getFilter(filters);
		Query query = new Query();
		if (!ObjectUtils.isEmpty(criterias)) {
			query.addCriteria(criterias);
		}
		if (!ObjectUtils.isEmpty(includedFields)) {
			query.fields().include(includedFields);
		}
		log.debug("query: {} class : {}", query, clazz);
		// mongo does not support count on geo spatial query
		return mongoTemplate.find(query, clazz);
	}

	private void validateFilters(List<AbstractFilter> filters) {
		// mongo does not support count on geo spatial query
		// if geo spatial query is passed the query will fail
		for (AbstractFilter filter : filters) {
			if (filter instanceof NearFilter) {
				throw new ValidationException(PlatformExceptionCodes.INVALID_DATA.getCode(),
						"Near To filter with does not work.");
			}
		}
	}

	@Override
	public Optional<T> findByFilter(String key, String value, Class<T> clazz) {
		List<AbstractFilter> filters = new ArrayList<>();
		filters.add(new InFilter<>(key, value));
		return findByFilter(filters, clazz);
	}

	@Override
	public Optional<T> findByFilter(List<AbstractFilter> filters, Class<T> clazz) {
		Page<T> data = filter(filters, PageRequest.of(0, 1), clazz);
		if (BooleanUtils.isFalse(data.hasContent())) {
			return Optional.empty();
		}
		if (data.getTotalElements() > 1) {
			throw new ValidationException(PlatformExceptionCodes.INVALID_DATA.getCode(),
					"Multiple values present for filters but expecting single value.");
		}
		return Optional.of(data.getContent().get(0));
	}
}

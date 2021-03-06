package com.cz.platform.filters;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdvancedFilterRepository<T> {

	Page<T> filter(List<AbstractFilter> filters, Pageable page, String[] includedFields, Class<T> clazz);

	Page<T> filter(List<AbstractFilter> filter, Pageable page, Class<T> clazz);

	List<T> filter(List<AbstractFilter> filters, String[] includedFields, Class<T> clazz);

	List<T> filter(List<AbstractFilter> filters, Class<T> clazz);

	Optional<T> findByFilter(List<AbstractFilter> filters, Class<T> clazz);

	Optional<T> findByFilter(String key, String value, Class<T> clazz);

}

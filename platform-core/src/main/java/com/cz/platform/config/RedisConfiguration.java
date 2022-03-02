package com.cz.platform.config;

import java.text.MessageFormat;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import com.cz.platform.PlatformConstants;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class RedisConfiguration {

	private RedisConfigurationProps props;

	@Bean
	protected JedisConnectionFactory jedisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(props.getHost(),
				props.getPort());
		redisStandaloneConfiguration.setDatabase(props.getDb());
		return new JedisConnectionFactory(redisStandaloneConfiguration);
	}

	@Bean(name = PlatformConstants.REDIS_TEMPLATE_FOR_UNIQUE_NUMBERS)
	public RedisTemplate<String, Integer> redisTemplateForUnique() {
		final RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new GenericToStringSerializer<>(String.class));
		redisTemplate.setHashKeySerializer(new GenericToStringSerializer<>(String.class));
		redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(String.class));
		redisTemplate.setValueSerializer(new GenericToStringSerializer<>(String.class));
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}

	@Bean
	public RedissonClient getRedisson() {
		Config config = new Config();
		String url = MessageFormat.format("redis://{0}:{1}", props.getHost(), String.valueOf(props.getPort()));
		config.useSingleServer().setAddress(url).setDatabase(props.getDb());
		RedissonClient client = Redisson.create(config);
		return client;
	}

}

package mango.rentalsystem.global.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import mango.rentalsystem.global.properties.JwtProperties;
import mango.rentalsystem.global.properties.RedisProperties;

@EnableConfigurationProperties({
	JwtProperties.class,
	RedisProperties.class
})
@Configuration
public class ConfigurationPropertiesConfig {
}

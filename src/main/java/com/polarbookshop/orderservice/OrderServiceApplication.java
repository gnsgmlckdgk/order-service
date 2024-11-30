package com.polarbookshop.orderservice;

import com.polarbookshop.orderservice.config.ClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Slf4j
@SpringBootApplication
@ConfigurationPropertiesScan
public class OrderServiceApplication {

	public static void main(String[] args) {
		var ctx = SpringApplication.run(OrderServiceApplication.class, args);

		// Bean이 제대로 로드되었는지 확인
		var properties = ctx.getBean(ClientProperties.class);
		log.info("Main: ClientProperties loaded = {}", properties);

	}

}

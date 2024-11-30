package com.polarbookshop.orderservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class ClientConfig {

    @Bean
    WebClient webClient(
            ClientProperties clientProperties,
            WebClient.Builder webClientBuilder  // WebClient 빈을 만들기 위해 스프링 부트가 자동 설정한 객체
    ) {
        return webClientBuilder // WebClient의 베이스 URL을 사용자 정의 속성을 통해 지정한 카탈로그 서비스 URL로 설정한다.
                .baseUrl(clientProperties.catalogServiceUri().toString())
                .build();
    }


    /**
     * Properties 로그 확인용
     * @param properties
     * @return
     */
    @Bean
    CommandLineRunner runner(ClientProperties properties) {
        return args -> {
            log.info("Loaded properties: greeting={}, uri={}",
                    properties.greeting(),
                    properties.catalogServiceUri());
        };
    }

}

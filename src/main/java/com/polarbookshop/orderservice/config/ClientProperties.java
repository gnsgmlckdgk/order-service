package com.polarbookshop.orderservice.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix = "polar")  // 사용자 지정 속성 이름의 프리픽스
public record ClientProperties(

        @NotNull
        URI catalogServiceUri
) {}

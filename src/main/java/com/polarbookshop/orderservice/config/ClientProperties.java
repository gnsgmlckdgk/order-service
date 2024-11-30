package com.polarbookshop.orderservice.config;

import ch.qos.logback.core.net.server.Client;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@Slf4j
@ConfigurationProperties(prefix = "polar")  // 사용자 지정 속성 이름의 프리픽스
public record ClientProperties(

        @NotNull
        URI catalogServiceUri,

        String greeting
) {}

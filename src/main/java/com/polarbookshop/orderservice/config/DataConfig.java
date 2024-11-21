package com.polarbookshop.orderservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@Configuration
@EnableR2dbcAuditing
public class DataConfig {}  // 지속성 엔티티에 대한 R2DBC 감사를 활성화한다.
// R2DBC Auditing은 Spring Data R2DBC에서 엔티티의 생성일, 수정일, 생성자, 수정자 등을 자동으로 관리하는 기능

package com.team7.spliito_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  // 엔티티의 생성 시간, 수정 시간 등을 자동으로 관리
public class SpliitoServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpliitoServerApplication.class, args);
	}

}

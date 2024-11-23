package com.polarbookshop.orderservice.book;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class BookClient {

    private static final String BOOK_ROOT_API = "/books/";
    private final WebClient webClient;

    public BookClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Book> getBookByIsbn(String isbn) {
        return webClient
                .get()
                .uri(BOOK_ROOT_API + isbn)
                .retrieve() // 요청을 보내고 응답을 받는다.
                .bodyToMono(Book.class)    // 받은 객체를 Mono<Book>으로 반환

                .timeout(Duration.ofSeconds(3), Mono.empty())    // 3초의 타임아웃, 폴백으로 빈 모노 객체를 반환
                // 쓰기/명령 같은 거래의 경우 타임아웃발생 시 실제 서버에서는 나중에 처리가 될 수 있음
                // 이런 문제를 "멱등성이 보장되지 않는다" 라고 함

                //@ 에러처리 ---------------------------------------------------------------------------
                // 404 에러응답의 경우 재시도 하지 않고 빈 모노 객체를 반환한다.
                .onErrorResume(WebClientResponseException.NotFound.class,
                        exception -> Mono.empty())

                // 지수 백오프를 재시도 전략으로 사용
                // 100밀리초의 초기 백오프로 총 3회까지 시도한다.(시도 횟수에 100밀리초를 곱한값만큼 타임아웃 설정)
                // 멱등성이 없는 거래의 경우는 재시도 하면 안됨(일관성이 깨짐)
                .retryWhen(
                        Retry.backoff(3, Duration.ofMillis(100))
                );

    }

}

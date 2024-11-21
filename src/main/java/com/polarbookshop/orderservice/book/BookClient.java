package com.polarbookshop.orderservice.book;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
                .bodyToMono(Book.class);    // 받은 객체를 Mono<Book>으로 반환
    }

}
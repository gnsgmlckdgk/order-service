package com.polarbookshop.orderservice.book;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;


public class BookClientTests {

    private MockWebServer mockWebServer;
    private BookClient bookClient;

    @BeforeEach
    void setup() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start(); // 테스트케이스를 실행하기 앞서 모의 서버를 시작한다.

        // String baseUrl = mockWebServer.url("/").toString();
        String baseUrl = "http://localhost:" + mockWebServer.getPort();

        var webClient = WebClient.builder() // 모의 서버의 URL을 웹 클라이언트의 베이스 URL로 사용한다.
                .baseUrl(baseUrl)
                .build();
        this.bookClient = new BookClient(webClient);
    }

    @AfterEach
    void clean() throws IOException {
        this.mockWebServer.shutdown();  // 테스트가 끝나면 모의서버를 중지한다.
    }


    @Test
    void whenBookExistsThenReturnBook() {
        var bookIsbn = "1234567890";
        var mockResponse = new MockResponse()   // 모의 서버에 의해 반환되는 응답을 정의
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                        {
                            "isbn": %s,
                            "title": "Title",
                            "author": "Author",
                            "price": 9.90,
                            "publisher": "Polarsophia"
                        }
                        """.formatted(bookIsbn));

        mockWebServer.enqueue(mockResponse);    // 모의 서버가 처리하는 큐에 모의 응답을 추가한다.

        Mono<Book> book = bookClient.getBookByIsbn(bookIsbn);

        StepVerifier.create(book)   // BookClient가 반환하는 객체로 StepVerifier 객체를 초기화한다.
                .expectNextMatches(
                        b -> b.isbn().equals(bookIsbn)) // 반환된 책의 ISBN이 요청한 ISBN과 같은지 확인한다.
                .verifyComplete();  // 리엑티브 스트림이 성공적으로 완료됐는지 확인한다.

    }

}

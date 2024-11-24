package com.polarbookshop.orderservice.order.web;

import com.polarbookshop.orderservice.order.domain.Order;
import com.polarbookshop.orderservice.order.domain.OrderRequest;
import com.polarbookshop.orderservice.order.domain.OrderService;
import com.polarbookshop.orderservice.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.sql.SQLOutput;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@WebFluxTest(OrderController.class)
public class OrderControllerWebFluxTests {

    @Autowired
    private WebTestClient webClient;    // 웹 클라이언트의 변형으로 Restful 서비스 테스트를 쉽게 하기 위한 기능을 추가로 가지고 있다.

    @MockBean   // 모의 객체를 스프링 애플리케이션 컨텍스트에 추가한다.
    private OrderService orderService;

    @Test
    void whenBookNotAvailableThenRejectOrder() {
        var orderRequest = new OrderRequest("1234567890", 3);
        var expectedOrder = OrderService.buildRejectedOrder(
                orderRequest.isbn(), orderRequest.quantity()
        );

        given(orderService.submitOrder(
                orderRequest.isbn(), orderRequest.quantity()
        )).willReturn(Mono.just(expectedOrder));    // OrderService 모의 빈이 어떻게 작동해야 하는지 지정한다.


        webClient
                .post()
                .uri("/orders/")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()   // 주문이 성공적으로 생성될 것을 예상한다.
                .expectBody(Order.class).value(actualOrder -> {
                    System.out.println("actualOrder = " + actualOrder);
                    assertThat(actualOrder).isNotNull();
                    assertThat(actualOrder.status()).isEqualTo(OrderStatus.REJECTED);
                });
    }

}

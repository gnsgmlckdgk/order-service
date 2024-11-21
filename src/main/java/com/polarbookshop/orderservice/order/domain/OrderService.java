package com.polarbookshop.orderservice.order.domain;

import com.polarbookshop.orderservice.book.Book;
import com.polarbookshop.orderservice.book.BookClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
    private final BookClient bookClient;
    private final OrderRepository orderRepository;

    public OrderService(BookClient bookClient, OrderRepository orderRepository) {
        this.bookClient = bookClient;
        this.orderRepository = orderRepository;
    }

    /**
     * 주문 전체조회
     * @return
     */
    public Flux<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    /**
     * 주문 제출
     * @param isbn
     * @param quantity
     * @return
     */
    public Mono<Order> submitOrder(String isbn, int quantity) {
        // Mono.just() 로 Mono 객체를 만들고 flatMap() 연산자를 통해 데이터를 OrderRepository에 전달
        return bookClient.getBookByIsbn(isbn)
                .map(book -> buildAcceptedOrder(book, quantity))
                .defaultIfEmpty(    // 책이 카탈로그에 존재하지 않으면 주문을 거부한다.
                        buildRejectedOrder(isbn, quantity)
                )
                .flatMap(orderRepository::save);    // 주문을 저장한다.(접수 혹은 거부 상태로)

        /**
         * flatMap 과 map의 차이
         * map은 두 개의 자바 유형 사이를 매핑하는 반면 flatMap은 자바 유형을 리액티브 스트림으로 매핑한다.
         * flatMap => Mono<Order>
         * map => Mono<Mono<Order>>
         */
    }

    public static Order buildAcceptedOrder(Book book, int quantity) {
        return Order.of(book.isbn(), book.title()+" - "+book.author(), book.price(), quantity, OrderStatus.ACCEPTED);
    }

    /**
     * 주문거부 프로세스
     * @param bookIsbn
     * @param quantity
     * @return
     */
    public static Order buildRejectedOrder(String bookIsbn, int quantity) {
        // 주문이 거부되면 isbn, 수량, 상태만 지정
        // 스프링 데이터가 식별자, 버전, 감사 메타데이터를 알아서 처리해준다.
        return Order.of(bookIsbn, null, null, quantity, OrderStatus.REJECTED);
    }

}

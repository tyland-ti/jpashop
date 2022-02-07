package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.exception.NotEnougthStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void order()  {
        //given
        Member member = createMember();

        Book book = createBook(10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        Order order = orderRepository.findOne(orderId);

        //then
        assertEquals(order.getStatus(), OrderStatus.ORDER); //주문상태체크
        assertEquals(order.getTotalPrice(), book.getPrice() * orderCount); // 주문총가격 체크
        assertEquals(8, book.getStockQuantity()); //재고수량 확인
    }

    @Test
    public void cancelOrder() {
        //given
        Member member = createMember();
        Item item = createBook(3);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order order = orderRepository.findOne(orderId);

        assertEquals(order.getStatus(), OrderStatus.CANCEL);
        assertEquals(3, item.getStockQuantity());
    }

    @Test
    public void orderOverQuantity() {
        //given
        Member member = createMember();
        Item item = createBook(10);

        int orderCount = 12;

        //when
        assertThrows(NotEnougthStockException.class, () ->orderService.order(member.getId(), item.getId(), orderCount));

        //then
    }

    private Book createBook(int stockQuantity) {
        Book book = new Book();
        book.setName("JPA 테스트");
        book.setAuthor("작가1");
        book.setStockQuantity(stockQuantity);
        book.setPrice(20000);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("테스트1");
        member.setAddress(new Address("서울","흑석동","110111"));
        em.persist(member);
        return member;
    }
}
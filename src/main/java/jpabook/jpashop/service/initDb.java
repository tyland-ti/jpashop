package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.Item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class initDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            Member member = createMember("USER1","서울시", "흑석로", "110220");
            em.persist(member);

            Book book1 = createBook("JPA BOOK1", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("JPA BOOK2", 20000, 10);
            em.persist(book2);

            OrderItems orderItem1 = OrderItems.createOrderItem(book1, 10000, 1);
            OrderItems orderItem2 = OrderItems.createOrderItem(book2, 20000, 3);

            Delivery delivery = getDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);

        }

        public void dbInit2() {
            Member member = createMember("USER2","서울시", "상도로", "220330");
            em.persist(member);

            Book book1 = createBook("Spring BOOK1", 10000, 200);
            em.persist(book1);

            Book book2 = createBook("Spring BOOK2", 20000, 10);
            em.persist(book2);

            OrderItems orderItem1 = OrderItems.createOrderItem(book1, 10000, 1);
            OrderItems orderItem2 = OrderItems.createOrderItem(book2, 20000, 3);

            Delivery delivery = getDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);

        }

        private Member createMember(String userName, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(userName);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private Book createBook(String bookName, int price, int count) {
            Book book1 = new Book();
            book1.setName(bookName);
            book1.setPrice(price);
            book1.setStockQuantity(count);
            return book1;
        }

        private Delivery getDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

    }
}

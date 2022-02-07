package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItems;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티조회
        Member findMember = memberRepository.findOne(memberId);
        Item findItem = itemRepository.findItem(itemId);

        //배송정보 조회
        Delivery delivery = new Delivery();
        delivery.setAddress(findMember.getAddress());

        //주문상품 생성
        OrderItems orderItem = OrderItems.createOrderItem(findItem, findItem.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(findMember, delivery, orderItem);

        orderRepository.save(order);
        return order.getId();
    }

    /**
     * 주문취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {

        //주문엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문취소
        order.cancel();

    }

    /**
     * 주문검색
     */
}

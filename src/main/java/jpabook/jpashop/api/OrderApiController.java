package jpabook.jpashop.api;

import jpabook.jpashop.api.dto.OrderQueryDto;
import jpabook.jpashop.api.dto.collectOrderDto;
import jpabook.jpashop.api.dto.orderDto;
import jpabook.jpashop.api.dto.orderFlatDto;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItems;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.query.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("api/v1/orders")
    public List<Order> orderV1() {
        List<Order> allByString = orderRepository.findAllByString(new OrderSearch());
        for(Order order:allByString) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItems> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return allByString;
    }

    @GetMapping("api/v2/orders")
    public List<collectOrderDto> orderV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        return orders.stream()
                .map(o -> new collectOrderDto(o))
                .collect(Collectors.toList());
    }

    @GetMapping("api/v3/orders")
    public List<collectOrderDto> orderV3() {
        List<Order> order =  orderRepository.findAllWithItem();
        return order.stream()
                .map(o -> new collectOrderDto(o))
                .collect(Collectors.toList());
    }

    /**
     * 1. entity to Dto 변환 <br>
     * 2. 페이징 처리
     */
    @GetMapping("api/v3.1/orders")
    public List<collectOrderDto> orderV3_page(@RequestParam(value = "offset") int offset,
                                              @RequestParam(value = "limit") int limit) {
        List<Order> order =  orderRepository.findAllWithMember(offset, limit);
        return order.stream()
                .map(o -> new collectOrderDto(o))
                .collect(Collectors.toList());
    }

    /**
     * JPA to DTO 직접조회
     */
    @GetMapping("api/v4/orders")
    public List<OrderQueryDto> orderV4() {

        return orderQueryRepository.findAll();

    }

    /**
     * JPA to DTO
     * 조회 최적화(컬렉션)
     */
    @GetMapping("api/v5/orders")
    public List<OrderQueryDto> orderV5() {
        return orderQueryRepository.findAll_optimize();
    }

    /**
     * JPA to DTO
     * 플랫 데이터 최적화화     */
    @GetMapping("api/v6/orders")
    public List<orderFlatDto> orderV6() {
        return orderQueryRepository.findAll_flat();
    }
}

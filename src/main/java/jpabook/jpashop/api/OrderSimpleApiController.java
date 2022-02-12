package jpabook.jpashop.api;

import jpabook.jpashop.api.dto.orderDto;
import jpabook.jpashop.api.dto.simpleOrderResponse;
import jpabook.jpashop.api.dto.simpleQueryDto;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Order
 * Order -> Member (many to one)
 * Order -> Delievery (one to one)
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("api/v1/simple-orders")
    public List<Order> orderV1() {

        List<Order> findOrders = orderRepository.findAllByString(new OrderSearch());

        for (Order order:findOrders) {
            order.getMember().getName(); //lazy 강제초기화
            order.getDelivery().getAddress();
        }

        return findOrders;
    }

    @GetMapping("api/v2/simple-orders")
    public simpleOrderResponse orderv2() {

        List<Order> allOrder = orderRepository.findAllByString(new OrderSearch());
        List<orderDto> orderDtos = allOrder.stream()
                .map(o -> new orderDto(o))
                .collect(Collectors.toList());

        return new simpleOrderResponse(orderDtos.size(), orderDtos);

    }

    @GetMapping("api/v3/simple-orders")
    public simpleOrderResponse orderv3() {

        List<Order> all = orderRepository.findAll();
        List<orderDto> orderDtos = all.stream()
                .map(o -> new orderDto(o))
                .collect(Collectors.toList());

        return new simpleOrderResponse(orderDtos.size(), orderDtos);

    }

    /**
     * JPA 에서 DTO로 바로 조회
     */
    @GetMapping("api/v4/simple-orders")
    public simpleOrderResponse orderV4() {
        List<simpleQueryDto>  all = orderRepository.findOrderDto();

        return new simpleOrderResponse(all.size(), all);
    }

}



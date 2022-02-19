package jpabook.jpashop.repository.query;

import jpabook.jpashop.api.dto.OrderItemQueryDto;
import jpabook.jpashop.api.dto.OrderQueryDto;
import jpabook.jpashop.api.dto.orderFlatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findAll() {
        List<OrderQueryDto> orders = findOrders();

        orders.forEach(o -> {
            List<OrderItemQueryDto> orderItem = findOrderItem(o.getOrderId());
            o.setOrderItems(orderItem);
        });
        return orders;
    }

    private List<OrderItemQueryDto> findOrderItem(Long id) {
        return em.createQuery("select new jpabook.jpashop.api.dto.OrderItemQueryDto(oi.order.id,i.name, i.price, oi.count) " +
                        "from OrderItems oi " +
                        "join oi.item i " +
                        "where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", id)
                .getResultList();
    }

    public List<OrderQueryDto> findAll_optimize() {
        List<OrderQueryDto> orders = findOrders();

        List<Long> ids = orders.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());

        List<OrderItemQueryDto> orderItem = findOrderItem_optimize(ids);

        Map<Long, List<OrderItemQueryDto>> listMap = orderItem.stream()
                .collect(Collectors.groupingBy(o -> o.getOrderId()));

        orders.forEach(orderDto -> orderDto.setOrderItems(listMap.get(orderDto.getOrderId())));

        return orders;
    }

    private List<OrderItemQueryDto> findOrderItem_optimize(List<Long> ids) {
        return em.createQuery("select new jpabook.jpashop.api.dto.OrderItemQueryDto(oi.order.id,i.name, i.price, oi.count) " +
                        "from OrderItems oi " +
                        "join oi.item i " +
                        "where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", ids)
                .getResultList();

    }

    public List<OrderQueryDto> findOrders() {

        return em.createQuery("select new jpabook.jpashop.api.dto.OrderQueryDto(o.id, m.name, o.orderDate, o.status, m.address)" +
                        " from Order o " +
                        "join o.member m " +
                        "join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    public List<orderFlatDto> findAll_flat() {

        return em.createQuery("select new jpabook.jpashop.api.dto.orderFlatDto(o.id, m.name, o.orderDate, o.status, m.address, i.name, oi.orderPrice, oi.count) " +
                        "from Order o " +
                        "join o.member m " +
                        "join o.delivery d " +
                        "join o.orderItems oi " +
                        "join oi.item i", orderFlatDto.class)
                .getResultList();
    }
}

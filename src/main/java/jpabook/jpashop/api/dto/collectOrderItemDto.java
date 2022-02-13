package jpabook.jpashop.api.dto;

import jpabook.jpashop.domain.OrderItems;
import lombok.Getter;

@Getter
public class collectOrderItemDto {

    private String itemName;
    private int price;
    private  int count;

    public collectOrderItemDto(OrderItems orderItems) {
        this.itemName = orderItems.getItem().getName();
        this.price = orderItems.getOrderPrice();
        this.count = orderItems.getCount();
    }
}

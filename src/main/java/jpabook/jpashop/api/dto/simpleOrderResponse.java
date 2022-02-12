package jpabook.jpashop.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class simpleOrderResponse<T> {

    private int count;
    private T data;

}

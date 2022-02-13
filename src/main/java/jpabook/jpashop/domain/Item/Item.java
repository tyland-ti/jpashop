package jpabook.jpashop.domain.Item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnougthStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    @JsonIgnore
    private List<Category> categories = new ArrayList<>();

    //비지니스 로직 추가

    /**
     * 재고 수량 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고 수량 감소
     */
    public void removeStock(int quantity) {
        int stock = this.stockQuantity - quantity;

        if (stock < 0) {
            throw new NotEnougthStockException("lack stock");
        }
        this.stockQuantity = stock;

    }
}

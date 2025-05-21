package th.co.priorsolution.training.restaurant.model;

import lombok.Data;
import th.co.priorsolution.training.restaurant.entity.FoodCategory;

@Data
public class OrderItemDtoModel {
    private String menuName;
    private FoodCategory category;
    private double price;
    private String status;
}

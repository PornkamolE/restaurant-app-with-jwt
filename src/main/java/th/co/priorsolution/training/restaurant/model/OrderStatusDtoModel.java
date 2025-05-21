package th.co.priorsolution.training.restaurant.model;

import lombok.Data;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;

import java.util.List;

@Data
public class OrderStatusDtoModel {
    private Long id;
    private int tableNumber;
    private String status;
    private List<OrderItemEntity> items;
}

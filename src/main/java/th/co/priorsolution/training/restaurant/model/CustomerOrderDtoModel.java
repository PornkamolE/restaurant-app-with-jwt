package th.co.priorsolution.training.restaurant.model;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.util.List;

@Data
public class CustomerOrderDtoModel {
    private Integer tableNumber;
    private List<OrderItemDtoModel> items;

}

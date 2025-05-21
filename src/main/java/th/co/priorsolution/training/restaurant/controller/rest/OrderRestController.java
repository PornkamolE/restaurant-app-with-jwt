package th.co.priorsolution.training.restaurant.controller.rest;

import org.springframework.web.bind.annotation.*;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.model.CustomerOrderDtoModel;
import th.co.priorsolution.training.restaurant.model.OrderStatusDtoModel;
import th.co.priorsolution.training.restaurant.model.ResponseModel;
import th.co.priorsolution.training.restaurant.service.OrderService;

@RestController
@RequestMapping("/api")
public class OrderRestController {

    private OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseModel<OrderEntity> createOrder(@RequestBody CustomerOrderDtoModel orderDto) {
        return this.orderService.createOrder(orderDto);
    }

    @GetMapping("/status/{orderId}")
    public OrderStatusDtoModel getOrderStatus(
            @PathVariable Long orderId
    ){
        return this.orderService.getOrderStatus(orderId);
    }
}

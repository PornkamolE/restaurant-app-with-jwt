package th.co.priorsolution.training.restaurant.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.model.OrderItemDtoModel;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final OrderRepository orderRepository;

    @GetMapping("/status")
    public String viewOrderStatus(@RequestParam(value = "orderId", required = false) Long orderId,
                                  @RequestParam(value = "tableNumber", required = false) Integer tableNumber,
                                  Model model) {

        Optional<OrderEntity> optionalOrder = Optional.empty();

        if (orderId != null) {
            optionalOrder = orderRepository.findById(orderId);
        } else if (tableNumber != null) {
            optionalOrder = orderRepository.findTopByTableNumberOrderByCreatedAtDesc(tableNumber);
        }

        if (optionalOrder.isPresent()) {
            OrderEntity order = optionalOrder.get();

            List<OrderItemDtoModel> itemDtos = order.getItems().stream().map(item -> {
                OrderItemDtoModel dto = new OrderItemDtoModel();
                dto.setMenuName(item.getMenuName());
                dto.setCategory(item.getCategory());
                dto.setStatus(item.getStatus().name());
                return dto;
            }).toList();

            model.addAttribute("orderItems", itemDtos);
            model.addAttribute("orderId", order.getId());
            model.addAttribute("tableNumber", order.getTableNumber());
        } else {
            model.addAttribute("notFound", true);
            if (orderId != null) {
                model.addAttribute("orderId", orderId);
            } else {
                model.addAttribute("tableNumber", tableNumber);
            }
        }

        return "customer-status";
    }

}

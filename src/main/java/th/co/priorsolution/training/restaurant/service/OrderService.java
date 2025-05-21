package th.co.priorsolution.training.restaurant.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.component.StationRouterComponent;
import th.co.priorsolution.training.restaurant.entity.*;
import th.co.priorsolution.training.restaurant.model.CustomerOrderDtoModel;
import th.co.priorsolution.training.restaurant.model.OrderStatusDtoModel;
import th.co.priorsolution.training.restaurant.model.ResponseModel;
import th.co.priorsolution.training.restaurant.repository.FoodMenuRepository;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;
import th.co.priorsolution.training.restaurant.repository.TableRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final StationRouterComponent stationRouterComponent;
    private final FoodMenuRepository foodMenuRepository;
    private final TableService tableService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, TableRepository tableRepository, StationRouterComponent stationRouterComponent, FoodMenuRepository foodMenuRepository, TableService tableService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.stationRouterComponent = stationRouterComponent;
        this.foodMenuRepository = foodMenuRepository;
        this.tableService = tableService;
    }

    @Transactional
    public ResponseModel<OrderEntity> createOrder(CustomerOrderDtoModel dto) {
        ResponseModel<OrderEntity> result = new ResponseModel<>();

        try {

            result.setStatus(201);
            result.setDescription("Order Created Successfully");

            tableService.markTableOccupied(dto.getTableNumber());

            OrderEntity order = new OrderEntity();
            order.setTableNumber(dto.getTableNumber());
            order.setStatus(OrderStatus.NEW);
            order = orderRepository.save(order);

            OrderEntity finalOrder = order;
            List<OrderItemEntity> items = dto.getItems().stream().map(itemDto -> {
                OrderItemEntity item = new OrderItemEntity();
                item.setOrder(finalOrder);
                item.setMenuName(itemDto.getMenuName());
                item.setCategory(itemDto.getCategory());
                item.setStatus(OrderItemStatus.NEW);

                // ✅ ดึงราคาจากเมนู
                FoodMenuEntity menu = foodMenuRepository.findByName(itemDto.getMenuName())
                        .orElseThrow(() -> new IllegalArgumentException("Menu not found: " + itemDto.getMenuName()));
                item.setPrice(menu.getPrice());

                return item;
            }).collect(Collectors.toList());


            orderItemRepository.saveAll(items);
            stationRouterComponent.routeToStations(items);

            order.setStatus(OrderStatus.PROCESSING);
            orderRepository.save(order);

            finalOrder.setItems(items);
            result.setData(finalOrder);

        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    public OrderStatusDtoModel getOrderStatus(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        List<OrderItemEntity> items = orderItemRepository.findByOrderId(orderId);

        OrderStatusDtoModel dto = new OrderStatusDtoModel();
        dto.setId(order.getId());
        dto.setTableNumber(order.getTableNumber());
        dto.setStatus(order.getStatus().name());
        dto.setItems(items);

        return dto;
    }

}

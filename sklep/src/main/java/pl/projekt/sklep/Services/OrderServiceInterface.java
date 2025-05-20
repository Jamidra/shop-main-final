package pl.projekt.sklep.Services;

import pl.projekt.sklep.Dtos.OrderDto;
import pl.projekt.sklep.Models.Order;

import java.util.List;

public interface OrderServiceInterface {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
}

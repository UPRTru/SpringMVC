package T2.mapper;

import T2.dto.OrderDto;
import T2.entities.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderMapper {
  public OrderDto orderDto (Order order) {
    OrderDto orderDto = new OrderDto();
    orderDto.setAmount(order.getAllAmount());
    orderDto.setStatus(order.getStatusEnum().name());
    if (order.getItems() != null) {
      orderDto.setItems(order.getItems());
    }
    return orderDto;
  }
}

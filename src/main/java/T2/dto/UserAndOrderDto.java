package T2.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserAndOrderDto {
  private UserDto user;
  private List<OrderDto> orders = new ArrayList<>();

  public void addOrder(OrderDto order) {
    orders.add(order);
  }

  public void removeOrder(OrderDto order) {
    orders.remove(order);
  }
}

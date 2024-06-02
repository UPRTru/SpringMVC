package T2.service;

import T2.dto.UserAndOrderDto;

public interface UserDetails {
  UserAndOrderDto getUserAndOrders(String name);
}

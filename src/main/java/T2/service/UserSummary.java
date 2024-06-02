package T2.service;

import T2.dto.UserDto;
import T2.entities.User;

import java.util.List;

public interface UserSummary {
  List<UserDto> getAllUsersSummary();

  UserDto getUserSummary(String name);

  User getUserSummaryJsonView(String name);
}

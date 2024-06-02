package T2.service;

import T2.dto.UserDto;

public interface EditUsers {
  void createUser(UserDto userDto);

  void updateUser(UserDto userDto);

  void deleteUser(String name);
}

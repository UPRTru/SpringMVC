package T2.mapper;

import T2.dto.UserDto;
import T2.entities.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
  public UserDto userDto (User user) {
    UserDto userDto = new UserDto();
    userDto.setName(user.getName());
    userDto.setEmail(user.getEmail());
    return userDto;
  }
}

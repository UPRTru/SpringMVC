package T2.service;

import T2.dto.UserAndOrderDto;
import T2.dto.UserDto;
import T2.entities.Order;
import T2.entities.User;
import T2.exception.AlreadyExistsException;
import T2.exception.IncorrectDataException;
import T2.exception.NotFoundUserException;
import T2.mapper.OrderMapper;
import T2.mapper.UserMapper;
import T2.repository.OrderRepository;
import T2.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetails, UserSummary, EditUsers {
  UserRepository userRepository;
  OrderRepository orderRepository;
  UserMapper userMapper;
  OrderMapper orderMapper;

  @Autowired
  public UserService(UserRepository userRepository, OrderRepository orderRepository, UserMapper userMapper, OrderMapper orderMapper) {
    this.userRepository = userRepository;
    this.orderRepository = orderRepository;
    this.userMapper = userMapper;
    this.orderMapper = orderMapper;
  }

  @Override
  public void createUser(UserDto userDto) {
    validInput(userDto);
    try {
      getUser(userDto.getName());
      throw new AlreadyExistsException("User already exists: " + userDto.getName());
    } catch (NotFoundUserException e) {
      User user = new User();
      user.setName(userDto.getName());
      user.setEmail(userDto.getEmail());
      userRepository.save(user);
    }
  }

  @Override
  public void updateUser(UserDto userDto) {
    validInput(userDto);
    User user = getUser(userDto.getName());
    user.setEmail(userDto.getEmail());
    userRepository.save(user);
  }

  @Override
  public void deleteUser(String name) {
    getUser(name);
    userRepository.deleteByName(name);
  }


  @Override
  public UserAndOrderDto getUserAndOrders(String name) {
    User user = getUser(name);
    UserAndOrderDto userAndOrderDto = new UserAndOrderDto();
    userAndOrderDto.setUser(userMapper.userDto(user));
    List<Order> orders = orderRepository.findByUserName(user.getName());
    if (!orders.isEmpty()) {
      orders.forEach(order -> userAndOrderDto.addOrder(orderMapper.orderDto(order)));
    }
    return userAndOrderDto;
  }

  @Override
  public ArrayList<UserDto> getAllUsersSummary() {
    ArrayList<UserDto> result = new ArrayList<>();
    Iterable<User> users = userRepository.findAll();
    users.forEach(user -> result.add(userMapper.userDto(user)));
    return result;
  }

  @Override
  public UserDto getUserSummary(String name) {
    User user = getUser(name);
    return userMapper.userDto(user);
  }

  @Override
  public User getUserSummaryJsonView(String name) {
    return getUser(name);
  }

  private User getUser(String name) {
    Optional<User> user = userRepository.findByName(name);
    if (user.isEmpty()) {
      throw new NotFoundUserException("User " + name + " not found");
    }
    return user.get();
  }

  private void validInput(UserDto userDto) {
    String email = userDto.getEmail();
    Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
    Matcher mat = pattern.matcher(email);
    if (userDto.getName().isEmpty() || !mat.matches()) {
      throw new IncorrectDataException("Not a valid email: " + email);
    } else if (userDto.getName().isEmpty() || userDto.getName() == "") {
      throw new IncorrectDataException("Not a valid name: " + userDto.getName());
    }
  }
}

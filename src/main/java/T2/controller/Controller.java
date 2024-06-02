package T2.controller;

import T2.View;
import T2.dto.UserAndOrderDto;
import T2.dto.UserDto;
import T2.entities.User;
import T2.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Controller {
  private final UserService userService;

  @GetMapping("/user/{name}")
  @ResponseBody
  public UserDto getUserByName(@PathVariable("name") String name) {
    return userService.getUserSummary(name);
  }

  @JsonView(View.UI.class)
  @GetMapping("/testJsonView/{name}")
  @ResponseBody()
  public User getUserByNameTestJsonView(@PathVariable("name") String name) {
    return userService.getUserSummaryJsonView(name);
  }

  @GetMapping("/users")
  public List<UserDto> getAllUsersSummary() {
    return userService.getAllUsersSummary();
  }

  @GetMapping("/user_and_orders/{name}")
  public UserAndOrderDto getUserAndOrders(@PathVariable("name") String name) {
    return userService.getUserAndOrders(name);
  }

  @PostMapping("/new_user")
  @ResponseStatus(HttpStatus.CREATED)
  public String createUser(@RequestBody UserDto userDto) {
    userService.createUser(userDto);
    return "User created";
  }

  @PatchMapping("/update_user")
  public void updateUser(@RequestBody UserDto userDto) {
    userService.updateUser(userDto);
  }

  @DeleteMapping("/delete_user/{name}")
  public void deleteUser(@PathVariable("name") String name) {
    userService.deleteUser(name);
  }
}

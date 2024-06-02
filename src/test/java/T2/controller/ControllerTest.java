package T2.controller;

import T2.dto.UserAndOrderDto;
import T2.dto.UserDto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ControllerTest {

  @Test
  void test() throws IOException {
    String json = "{\"name\":\"test\",\"email\":\"test@email.com\"}";
    String jsonUpdate = "{\"name\":\"test\",\"email\":\"update@email.com\"}";
    String errorEmailJson = "{\"name\":\"test\",\"email\":\"email\"}";
    String errorNameJson = "{\"name\":\"\",\"email\":\"test@email.com\"}";
    String jsonUsers = "[{\"name\":\"user\",\"email\":\"user@email.com\"},{\"name\":\"test\",\"email\":\"test@email.com\"}]";
    String jsonUser = "[{\"name\":\"user\",\"email\":\"user@email.com\"}]";
    String jsonUserAndOrders = "{\"user\":{\"name\":\"user\",\"email\":\"user@email.com\"},\"orders\":[{\"amount\":200.0,\"status\":\"UNPAID\",\"items\":[{\"id\":1,\"name\":\"item\",\"price\":100.0}]},{\"amount\":400.0,\"status\":\"UNPAID\",\"items\":[{\"id\":2,\"name\":\"item2\",\"price\":200.0}]}]}";
    UserDto userDto = getUserDto(json);
    Map<String, String> result = createUser("http://localhost:8080/new_user", json);
    assertEquals("HTTP/1.1 201 ", result.get("status"));
    assertEquals("User created", result.get("body"));
    result = createUser("http://localhost:8080/new_user", json);
    assertEquals("HTTP/1.1 208 ", result.get("status"));
    assertTrue(result.get("body").contains("Already Reported"));
    result = createUser("http://localhost:8080/new_user", errorEmailJson);
    assertEquals("HTTP/1.1 400 ", result.get("status"));
    assertTrue(result.get("body").contains("Bad Request"));
    result = createUser("http://localhost:8080/new_user", errorNameJson);
    assertEquals("HTTP/1.1 400 ", result.get("status"));
    assertTrue(result.get("body").contains("Bad Request"));

    result = httpGet("http://localhost:8080/user/test");
    assertEquals("HTTP/1.1 200 ", result.get("status"));
    assertEquals(json, result.get("body"));
    result = httpGet("http://localhost:8080/user/not_found");
    assertEquals("HTTP/1.1 404 ", result.get("status"));
    assertTrue(result.get("body").contains("Not Found"));

    result = httpGet("http://localhost:8080/testJsonView/test");
    assertEquals("HTTP/1.1 200 ", result.get("status"));
    assertEquals(json, result.get("body"));

    result = httpGet("http://localhost:8080/users");
    assertEquals("HTTP/1.1 200 ", result.get("status"));
    assertEquals(jsonUsers, result.get("body"));
    assertEquals(2, getCollectionUserDto(result.get("body")).size());

    result = httpGet("http://localhost:8080/user_and_orders/user");
    assertEquals("HTTP/1.1 200 ", result.get("status"));
    assertEquals(jsonUserAndOrders, result.get("body"));
    assertEquals(2, getUserAndOrderDto(result.get("body")).getOrders().size());

    result = updateUser("http://localhost:8080/update_user", jsonUpdate);
    assertEquals("HTTP/1.1 200 ", result.get("status"));
    result = httpGet("http://localhost:8080/user/test");
    assertEquals("HTTP/1.1 200 ", result.get("status"));
    assertEquals("update@email.com", getUserDto(result.get("body")).getEmail());

    result = deleteUser("http://localhost:8080/delete_user/test");
    assertEquals("HTTP/1.1 200 ", result.get("status"));
    result = httpGet("http://localhost:8080/user/test");
    assertEquals("HTTP/1.1 404 ", result.get("status"));
    assertTrue(result.get("body").contains("Not Found"));

    result = httpGet("http://localhost:8080/users");
    assertEquals("HTTP/1.1 200 ", result.get("status"));
    assertEquals(jsonUser, result.get("body"));
    assertEquals(1, getCollectionUserDto(result.get("body")).size());
  }

  private Map<String, String> createUser(String url, String json) throws IOException {
    Map<String, String> result = new HashMap<>();
    HttpPost httpPost = new HttpPost(url);
    httpPost.setEntity(new StringEntity(json));
    httpPost.setHeader("Content-Type", "application/json");
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);
    String status = httpResponse.getStatusLine().toString();
    result.put("status", status);
    String body = EntityUtils.toString(httpResponse.getEntity());
    result.put("body", body);
    return result;
  }

  private Map<String, String> updateUser(String url, String json) throws IOException {
    Map<String, String> result = new HashMap<>();
    HttpPatch httpPatch = new HttpPatch(url);
    httpPatch.setEntity(new StringEntity(json));
    httpPatch.setHeader("Content-Type", "application/json");
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPatch);
    String status = httpResponse.getStatusLine().toString();
    result.put("status", status);
    String body = EntityUtils.toString(httpResponse.getEntity());
    result.put("body", body);
    return result;
  }

  private Map<String, String> deleteUser(String url) throws IOException {
    Map<String, String> result = new HashMap<>();
    HttpUriRequest request = new HttpDelete(url);
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    result.put("status", httpResponse.getStatusLine().toString());
    result.put("body", EntityUtils.toString(httpResponse.getEntity()));
    return result;
  }

  private Map<String, String> httpGet(String url) throws IOException {
    Map<String, String> result = new HashMap<>();
    HttpUriRequest request = new HttpGet(url);
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
    result.put("status", httpResponse.getStatusLine().toString());
    result.put("body", EntityUtils.toString(httpResponse.getEntity()));
    return result;
  }

  private List<UserDto> getCollectionUserDto(String json) {
    try {
      TypeToken<List<UserDto>> typeToken = new TypeToken<>(){};
      return new Gson().fromJson(json, typeToken.getType());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private UserDto getUserDto(String json) {
    try {
      return new Gson().fromJson(json, UserDto.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private UserAndOrderDto getUserAndOrderDto(String json) {
    try {
      return new Gson().fromJson(json, UserAndOrderDto.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
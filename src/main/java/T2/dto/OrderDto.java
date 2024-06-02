package T2.dto;

import T2.entities.Item;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class OrderDto {
  private Double amount;
  private String status;
  private Collection<Item> items = new ArrayList<>();
}

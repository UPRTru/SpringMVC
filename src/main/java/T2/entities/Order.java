package T2.entities;

import T2.status.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "user_name", nullable = false)
  private String userName;

  @Column(name = "amount")
  private Double amount;

  @Column(name = "status")
  private String status;

  @ManyToMany
  @JoinTable(name = "order_items",
      joinColumns = @JoinColumn(name = "order_id"),
      inverseJoinColumns = @JoinColumn(name = "item_id"))
  private Collection<Item> items = new ArrayList<>();

  public Double getAllAmount() {
    items.forEach(item -> amount += item.getPrice());
    return amount;
  }

  public void addItem(Item item) {
    items.add(item);
  }

  public void removeItem(Item item) {
    items.remove(item);
  }

  public Status getStatusEnum() {
    return Status.valueOf(status);
  }

  public void setStatusEnum(Status status) {
    this.status = status.toString();
  }

}

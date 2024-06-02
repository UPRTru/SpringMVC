package T2.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "items")
public class Item {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "name", unique = true, nullable = false)
  private String name;
  @Column(name = "price", nullable = false)
  private Double price;
}

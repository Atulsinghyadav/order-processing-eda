package com.orderprocessingeda.inventoryservice.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Inventory {

    @Column(nullable = false)
    @Id
    private Long productId;

    @Column(nullable = false)
    private Long quantity;

    @Version
    private Long version;

}

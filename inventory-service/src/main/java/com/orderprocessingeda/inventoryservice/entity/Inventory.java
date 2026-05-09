package com.orderprocessingeda.inventoryservice.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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


}

package daumer.alban.alten.shop.back.products.model;

import daumer.alban.alten.shop.back.products.enums.InventoryStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Entity
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NonNull
    @Column(nullable = false)
    String code;

    @NonNull
    @Column(nullable = false)
    String name;

    @NonNull
    @Column(nullable = false)
    String description;

    @NonNull
    @Column(nullable = false)
    Long price;

    @NonNull
    @Column(nullable = false)
    Long quantity;

    @NonNull
    @Enumerated(EnumType.STRING)
    InventoryStatusEnum inventoryStatus;

    @NonNull
    @Column(nullable = false)
    String category;

    @Column
    String image;
    @Column
    Long rating;
}

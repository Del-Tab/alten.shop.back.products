package daumer.alban.alten.shop.back.products.bean;

import daumer.alban.alten.shop.back.products.enums.CategoryEnum;
import daumer.alban.alten.shop.back.products.enums.InventoryStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductBean {
    Long id;
    @NotNull
    String code;
    @NotNull
    String name;
    @NotNull
    String description;
    @NotNull
    Long price;
    @NotNull
    Long quantity;
    @NotNull
    InventoryStatusEnum inventoryStatus;
    @NotNull
    CategoryEnum category;
    // nullable fonctionellement
    String image;
    Long rating;
}

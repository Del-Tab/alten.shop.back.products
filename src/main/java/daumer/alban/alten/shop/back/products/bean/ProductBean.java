package daumer.alban.alten.shop.back.products.bean;

import daumer.alban.alten.shop.back.products.enums.CategoryEnum;
import daumer.alban.alten.shop.back.products.enums.InventoryStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ProductBean {
    Long id;
    @NonNull
    String code;
    @NonNull
    String name;
    @NonNull
    String description;
    @NonNull
    Long price;
    @NonNull
    Long quantity;
    @NonNull
    InventoryStatusEnum inventoryStatus;
    @NonNull
    CategoryEnum category;
    // nullable fonctionellement
    String image;
    Long rating;
}

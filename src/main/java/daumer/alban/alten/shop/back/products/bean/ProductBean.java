package daumer.alban.alten.shop.back.products.bean;

import daumer.alban.alten.shop.back.products.enums.InventoryStatusEnum;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProductBean {
    @NonNull
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
    String category;
    // nullable fonctionellement
    String image;
    Long rating;
}

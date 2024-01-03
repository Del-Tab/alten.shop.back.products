package daumer.alban.alten.shop.back.products.bean;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DataListBean<T> {
    @NotNull
    List<T> data;
}

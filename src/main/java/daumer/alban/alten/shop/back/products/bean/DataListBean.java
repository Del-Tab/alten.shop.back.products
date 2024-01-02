package daumer.alban.alten.shop.back.products.bean;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DataListBean<T> {
    @NotBlank
    List<T> data;
}

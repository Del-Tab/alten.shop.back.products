package daumer.alban.alten.shop.back.products.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class DataListBean<T> {
    @NonNull
    List<T> data;
}

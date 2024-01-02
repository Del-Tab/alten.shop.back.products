package daumer.alban.alten.shop.back.products.service;

import daumer.alban.alten.shop.back.products.bean.ProductBean;

import java.util.Collection;
import java.util.List;

public interface ProductService {

    Collection<ProductBean> getAll();

    void addAll(List<ProductBean> products);
}

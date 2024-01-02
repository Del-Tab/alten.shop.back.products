package daumer.alban.alten.shop.back.products.controller;

import daumer.alban.alten.shop.back.products.bean.ProductBean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class ProductController {

    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductBean> getProducts() {
        return Collections.emptyList();

    }

}

package daumer.alban.alten.shop.back.products.controller;

import daumer.alban.alten.shop.back.products.bean.DataListBean;
import daumer.alban.alten.shop.back.products.bean.ProductBean;
import daumer.alban.alten.shop.back.products.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/products-bucket")
public class ProductBucketController {

    @Autowired
    ProductService productService;

    @PostMapping(value="/")
    public ResponseEntity<HttpStatus> storeBucket(@Valid @RequestBody DataListBean<ProductBean> body) {
        productService.addAll(body.getData());
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

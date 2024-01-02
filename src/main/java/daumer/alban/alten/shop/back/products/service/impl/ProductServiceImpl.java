package daumer.alban.alten.shop.back.products.service.impl;

import daumer.alban.alten.shop.back.products.bean.ProductBean;
import daumer.alban.alten.shop.back.products.mapper.ProductMapper;
import daumer.alban.alten.shop.back.products.repository.ProductRepository;
import daumer.alban.alten.shop.back.products.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    final ProductMapper productMapper = ProductMapper.INSTANCE;

    @Override
    @Transactional
    public List<ProductBean> getAll() {
        return productMapper.map(productRepository.findAll());
    }
}

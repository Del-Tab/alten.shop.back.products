package daumer.alban.alten.shop.back.products.service.impl;

import daumer.alban.alten.shop.back.products.bean.ProductBean;
import daumer.alban.alten.shop.back.products.bean.ProductBeanNoId;
import daumer.alban.alten.shop.back.products.mapper.ProductMapper;
import daumer.alban.alten.shop.back.products.model.Product;
import daumer.alban.alten.shop.back.products.repository.ProductRepository;
import daumer.alban.alten.shop.back.products.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    final ProductMapper productMapper = ProductMapper.INSTANCE;

    @Override
    @Transactional
    public Collection<ProductBean> getAll() {
        return productMapper.map(productRepository.findAll());
    }

    @Override
    @Transactional
    public void addAll(List<ProductBean> products) {
        productRepository.saveAll(productMapper.mapToEntities(products));
    }

    @Override
    @Transactional
    public ProductBean getById(Long id) {
        return productMapper.map(productRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public Long create(ProductBeanNoId body) {
        return productRepository.save(productMapper.mapNew(body)).getId();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ProductBean update(Long id, ProductBeanNoId body) {
        Product existing = productRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        } else {
            productMapper.merge(existing, body);
            return productMapper.map(productRepository.save(existing));
        }
    }
}

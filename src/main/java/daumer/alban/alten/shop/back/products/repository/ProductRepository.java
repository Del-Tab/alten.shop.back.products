package daumer.alban.alten.shop.back.products.repository;

import daumer.alban.alten.shop.back.products.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}

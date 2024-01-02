package daumer.alban.alten.shop.back.products.mapper;

import daumer.alban.alten.shop.back.products.bean.ProductBean;
import daumer.alban.alten.shop.back.products.bean.ProductBeanNoId;
import daumer.alban.alten.shop.back.products.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductBean map(Product entity);

    List<ProductBean> map(List<Product> entities);

    Product mapToEntity(ProductBean bean);

    @Mapping(target = "id", ignore = true)
    Product mapNew(ProductBeanNoId bean);

    @Mapping(target = "id", ignore = true)
    void merge(@MappingTarget Product target, ProductBeanNoId source);

    List<Product> mapToEntities(List<ProductBean> beans);
}

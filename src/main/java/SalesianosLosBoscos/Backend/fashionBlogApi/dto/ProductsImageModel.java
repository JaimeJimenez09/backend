package SalesianosLosBoscos.Backend.fashionBlogApi.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductsImageModel {
    private Product product;
    private List<Product> products;
}

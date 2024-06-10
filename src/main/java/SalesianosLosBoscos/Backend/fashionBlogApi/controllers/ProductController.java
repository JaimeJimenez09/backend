package SalesianosLosBoscos.Backend.fashionBlogApi.controllers;

import SalesianosLosBoscos.Backend.fashionBlogApi.dto.Product;
import SalesianosLosBoscos.Backend.fashionBlogApi.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "http://localhost:4200")

public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("products/")
    public ResponseEntity<List<Product>> getProducts(){
        List<Product> productos = productRepository.findAll();
        ResponseEntity<List<Product>> responseEntity = null;
        if(!productos.isEmpty()){
            responseEntity = ResponseEntity.ok(productos);
        }else{
            responseEntity = ResponseEntity.notFound().build();
        }
        return responseEntity;
    }

    @GetMapping("brand/{brandId}/products")
    public ResponseEntity<List<Product>> getProductsByBrandId(@PathVariable("brandId") Integer brandId) {
        List<Product> products = productRepository.findByBrandBrandId(brandId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("product/{id}")
    public ResponseEntity<Product> getProductByID(@PathVariable("id") Integer id){
        Product dato = productRepository.findById(id).orElse(null);
        ResponseEntity<Product> responseEntity = null;
        if(dato != null) {
            responseEntity = ResponseEntity.ok(dato);
        } else {
            responseEntity = ResponseEntity.notFound().build();
        }
        return responseEntity;
    }

    @PostMapping("product/")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
        Product savedProduct = productRepository.save(product);
        if (savedProduct.getProductId() != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(value = "product/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable Integer id){
        Product product = productRepository.findById(id).orElse(null);

        if (product != null) {
            productRepository.delete(product);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("product/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @Valid @RequestBody Product productDetails){
        return productRepository.findById(id).map(existingProducts -> {
            existingProducts.setProductName(productDetails.getProductName());
            existingProducts.setColor(productDetails.getColor());
            existingProducts.setSize(productDetails.getSize());
            existingProducts.setPrice(productDetails.getPrice());
            existingProducts.setType(productDetails.getType());
            existingProducts.setQuantity(productDetails.getQuantity());
            existingProducts.setAvailable(productDetails.getAvailable());
            existingProducts.setBrand(productDetails.getBrand());
            existingProducts.setComments(productDetails.getComments());
            existingProducts.setValorations(productDetails.getValorations());
            productRepository.save(existingProducts);
            return ResponseEntity.ok(existingProducts);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("product/{id}")
    public ResponseEntity<Product> editProduct(@PathVariable Integer id, @Valid @RequestBody Product productDetails) {
        if (productDetails == null) {
            return ResponseEntity.badRequest().build();
        }

        Product existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }

        if (productDetails.getProductName() != null) existingProduct.setProductName(productDetails.getProductName());
        if (productDetails.getColor() != null) existingProduct.setColor(productDetails.getColor());
        if (productDetails.getSize() != null) existingProduct.setSize(productDetails.getSize());
        if (productDetails.getPrice() != 0) existingProduct.setPrice(productDetails.getPrice());
        if (productDetails.getType() != null) existingProduct.setType(productDetails.getType());
        if (productDetails.getQuantity() != null) existingProduct.setQuantity(productDetails.getQuantity());
        if (productDetails.getAvailable() != null) existingProduct.setAvailable(productDetails.getAvailable());
        if (productDetails.getBrand() != null) existingProduct.setBrand(productDetails.getBrand());
        if (productDetails.getComments() != null) existingProduct.setComments(productDetails.getComments());
        if (productDetails.getValorations() != null) existingProduct.setValorations(productDetails.getValorations());
        if (productDetails.getStores() != null) existingProduct.setStores(productDetails.getStores());
        productRepository.save(existingProduct);
        return ResponseEntity.ok(existingProduct);
    }
}

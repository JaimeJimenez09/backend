package SalesianosLosBoscos.Backend.fashionBlogApi.controllers;

import SalesianosLosBoscos.Backend.fashionBlogApi.dto.Brand;
import SalesianosLosBoscos.Backend.fashionBlogApi.repositories.BrandRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "http://localhost:4200")
public class BrandController {

    @Autowired
    BrandRepository brandRepository;

    @GetMapping("brands/")
    public ResponseEntity<List<Brand>> getBrands(){
        List<Brand> marcas = brandRepository.findAll();
        if (!marcas.isEmpty()) {
            return ResponseEntity.ok(marcas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("brand/{id}")
    public ResponseEntity<Brand> getBrandByID(@PathVariable("id") Integer id){
        Brand marca = brandRepository.findById(id).orElse(null);
        if (marca != null) {
            return ResponseEntity.ok(marca);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("brand/")
    public ResponseEntity<Brand> addBrand(@RequestBody Brand brand){
        Brand nueva = brandRepository.save(brand);
        if (nueva.getBrandId() != null) {
            return ResponseEntity.ok(nueva);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(value = "brand/{id}")
    public ResponseEntity<Brand> deleteBrandById(@PathVariable Integer id){
        Brand marca = brandRepository.findById(id).orElse(null);
        if (marca != null) {
            brandRepository.delete(marca);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("brand/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable Integer id, @Valid @RequestBody Brand brandDetails){
        return brandRepository.findById(id).map(existingBrand -> {
            existingBrand.setBrandName(brandDetails.getBrandName());
            existingBrand.setProducts(brandDetails.getProducts());
            brandRepository.save(existingBrand);
            return ResponseEntity.ok(existingBrand);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("brand/{id}")
    public ResponseEntity<Brand> editBrand(@PathVariable Integer id, @RequestBody Brand brandDetails) {
        if (brandDetails == null) {
            return ResponseEntity.badRequest().build();
        }

        Brand existingBrand = brandRepository.findById(id).orElse(null);
        if (existingBrand == null) {
            return ResponseEntity.notFound().build();
        }

        if (brandDetails.getBrandName() != null) existingBrand.setBrandName(brandDetails.getBrandName());
        if (brandDetails.getProducts() != null) existingBrand.setProducts(brandDetails.getProducts());
        brandRepository.save(existingBrand);
        return ResponseEntity.ok(existingBrand);
    }
}

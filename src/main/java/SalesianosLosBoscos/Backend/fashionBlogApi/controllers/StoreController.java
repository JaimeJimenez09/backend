package SalesianosLosBoscos.Backend.fashionBlogApi.controllers;

import org.springframework.http.HttpStatus;
import SalesianosLosBoscos.Backend.fashionBlogApi.dto.Store;
import SalesianosLosBoscos.Backend.fashionBlogApi.repositories.StoreRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "http://localhost:4200")
public class StoreController {
    @Autowired
    private StoreRepository storeRepository;

    @GetMapping("store/")
    public ResponseEntity<List<Store>> getStores() {
        List<Store> stores = storeRepository.findAll();
        if (!stores.isEmpty()) {
            return ResponseEntity.ok(stores);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("store/{id}")
    public ResponseEntity<Store> getStoreByID(@PathVariable("id") Integer id) {
        Store store = storeRepository.findById(id).orElse(null);
        if (store != null) {
            return ResponseEntity.ok(store);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("stores/product/{productId}")
    public ResponseEntity<List<Store>> getStoresByProductId(@PathVariable Integer productId) {
        List<Store> stores = storeRepository.findStoresByProductId(productId);
        if (!stores.isEmpty()) {
            return ResponseEntity.ok(stores);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("store/")
    public ResponseEntity<Store> addStore(@Valid @RequestBody Store store) {
        Store newStore = storeRepository.save(store);
        if (newStore.getStoreId() != null) {
            return ResponseEntity.ok(newStore);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(value = "store/{id}")
    public ResponseEntity<Store> deleteStoreById(@PathVariable Integer id) {
        Store store = storeRepository.findById(id).orElse(null);
        if (store != null) {
            storeRepository.delete(store);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("store/{id}")
    public ResponseEntity<Store> updateStore(@PathVariable Integer id, @Valid @RequestBody Store storeDetails){
        return storeRepository.findById(id).map(existingStores -> {
            existingStores.setStoreName(storeDetails.getStoreName());
            existingStores.setLocation(storeDetails.getLocation());
            existingStores.setProducts(storeDetails.getProducts());

            storeRepository.save(existingStores);
            return ResponseEntity.ok(existingStores);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("store/{id}")
    public ResponseEntity<Store> editStore(@PathVariable Integer id, @RequestBody Store storeDetails) {
        if (storeDetails == null) {
            return ResponseEntity.badRequest().build();
        }

        Store existingStore = storeRepository.findById(id).orElse(null);
        if (existingStore == null) {
            return ResponseEntity.notFound().build();
        }

        if (storeDetails.getStoreName() != null) existingStore.setStoreName(storeDetails.getStoreName());
        if (storeDetails.getLocation() != null) existingStore.setLocation(storeDetails.getLocation());
        if (storeDetails.getProducts() != null) existingStore.setProducts(storeDetails.getProducts());

        storeRepository.save(existingStore);
        return ResponseEntity.ok(existingStore);
    }
}

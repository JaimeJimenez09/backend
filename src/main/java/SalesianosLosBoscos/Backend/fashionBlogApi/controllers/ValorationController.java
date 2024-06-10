package SalesianosLosBoscos.Backend.fashionBlogApi.controllers;

import SalesianosLosBoscos.Backend.fashionBlogApi.dto.Comment;
import SalesianosLosBoscos.Backend.fashionBlogApi.dto.User;
import SalesianosLosBoscos.Backend.fashionBlogApi.dto.Valoration;
import SalesianosLosBoscos.Backend.fashionBlogApi.repositories.ValorationRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "http://localhost:4200")
public class ValorationController {

    @Autowired
    private ValorationRepository valorationRepository;

    @GetMapping("valorations/")
    public ResponseEntity<List<Valoration>> getValorations(){
        List<Valoration> valoraciones = valorationRepository.findAll();
        if(!valoraciones.isEmpty()){
            return ResponseEntity.ok(valoraciones);
        }else{
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("valorations/product/{productId}")
    public ResponseEntity<List<Valoration>> getValorationsByProductId(@PathVariable Integer productId) {
        List<Valoration> valorations = valorationRepository.findByProductProductId(productId);
        if (!valorations.isEmpty()) {
            return ResponseEntity.ok(valorations);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("valorations/user/{userId}")
    public ResponseEntity<List<Valoration>> getValorationsByUserId(@PathVariable Integer userId) {
        List<Valoration> valorations = valorationRepository.findByUserUserId(userId);
        if (!valorations.isEmpty()) {
            return ResponseEntity.ok(valorations);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("valoration/{id}")
    public ResponseEntity<Valoration> getValorationById(@PathVariable("id") Integer id){
        return valorationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("valoration/")
    public ResponseEntity<Valoration> addValoration(@Valid @RequestBody Valoration valoration){
        Valoration nuevo = valorationRepository.save(valoration);
        if(nuevo.getId() != null){
            return ResponseEntity.ok(nuevo);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "valoration/{id}")
    public ResponseEntity<Valoration> deleteValorationById(@PathVariable Integer id){
        return valorationRepository.findById(id)
                .map(vaaloration -> {
                    valorationRepository.delete(vaaloration);
                    return ResponseEntity.ok(vaaloration);
                }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("valoration/{id}")
    public ResponseEntity<Valoration> updateValoration(@PathVariable Integer id, @Valid @RequestBody Valoration valorationDetails){
        return valorationRepository.findById(id).map(existingaVal -> {
            existingaVal.setRating(valorationDetails.getRating());
            existingaVal.setCreatedAt(valorationDetails.getCreatedAt());
            existingaVal.setUser(valorationDetails.getUser());
            existingaVal.setProduct(valorationDetails.getProduct());
            // establecer otros campos que son seguros para actualizar
            valorationRepository.save(existingaVal);
            return ResponseEntity.ok(existingaVal);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("user/")
    public ResponseEntity<Valoration> editValoration(@RequestBody Valoration val) {
        if (val == null || val.getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        return valorationRepository.findById(val.getId()).map(old -> {
            if (val.getRating() != 0) old.setRating(val.getRating());
            if (val.getCreatedAt() != null) old.setCreatedAt(val.getCreatedAt());
            if (val.getUser() != null) old.setUser(val.getUser());
            if (val.getProduct() != null) old.setProduct(val.getProduct());
            valorationRepository.save(old);
            return ResponseEntity.ok(old);
        }).orElse(ResponseEntity.notFound().build());
    }
}

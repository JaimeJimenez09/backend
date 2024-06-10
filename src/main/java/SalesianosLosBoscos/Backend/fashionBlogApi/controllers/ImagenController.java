package SalesianosLosBoscos.Backend.fashionBlogApi.controllers;


import SalesianosLosBoscos.Backend.fashionBlogApi.dto.Brand;
import SalesianosLosBoscos.Backend.fashionBlogApi.dto.ImageModel;
import SalesianosLosBoscos.Backend.fashionBlogApi.dto.Product;
import SalesianosLosBoscos.Backend.fashionBlogApi.dto.User;
import SalesianosLosBoscos.Backend.fashionBlogApi.repositories.BrandRepository;
import SalesianosLosBoscos.Backend.fashionBlogApi.repositories.ImagenRepository;
import SalesianosLosBoscos.Backend.fashionBlogApi.repositories.ProductRepository;
import SalesianosLosBoscos.Backend.fashionBlogApi.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "http://localhost:4200")
public class ImagenController {

    @Autowired
    private ImagenRepository imagenRepository;
    @Autowired
    private UserRepository userRespository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductRepository productRepository;

    public void AppController(ImagenRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    @GetMapping("photos/")
    //@Operation(summary = "Muestra el contenido de la base de datos",
            //description = "Obntiene el listado de la base de datos en formato JSON")
    public ResponseEntity<List<ImageModel>> getImages(){
        List<ImageModel> listado = imagenRepository.findAll();
        if(!listado.isEmpty()){
            return ResponseEntity.ok(listado);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("photo/{id}")
    public ResponseEntity<ImageModel> getImageById(@PathVariable("id") Long id){
        ImageModel dato = imagenRepository.findById(id).orElse(null);
        if(dato != null) {
            return ResponseEntity.ok(dato);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("photo/")
    public ResponseEntity<ImageModel> addImage(@Valid @RequestBody ImageModel imagen){
        ImageModel miDato = imagenRepository.save(imagen);
        if(miDato.getImageId() != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(miDato);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "photo/{id}")
    public ResponseEntity<ImageModel> deleteImageById(@PathVariable Long id){
        ImageModel user = imagenRepository.findById(id).orElse(null);
        if(user != null){
            imagenRepository.delete(user);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("photo/{id}")
    public ResponseEntity<ImageModel> updateImage(@PathVariable Long id, @Valid @RequestBody ImageModel imageModel){
        return imagenRepository.findById(id).map(image -> {
            // Aquí actualizas todos los campos, asumiendo que todos los campos necesarios están en userDetails
            image.setNombre(imageModel.getNombre());
            image.setMimeType(imageModel.getMimeType());
            image.setFotoBase64(imageModel.getFotoBase64());
            image.setProduct(imageModel.getProduct());
            image.setUser(imageModel.getUser());
            image.setBrand(imageModel.getBrand());

            // No actualizamos IDs o colecciones vinculadas directamente
            imagenRepository.save(image);
            return ResponseEntity.ok(image);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PatchMapping("photo/{id}")
    public ResponseEntity<ImageModel> editImage(@PathVariable Long id, @Valid @RequestBody ImageModel imagenDetails) {
        if (imagenDetails == null || imagenDetails.getImageId() == null) {
            return ResponseEntity.badRequest().build();
        }

        ImageModel existingImage = imagenRepository.findById(id).orElse(null);
        if (existingImage == null) {
            return ResponseEntity.notFound().build();
        }

        if (imagenDetails.getNombre() != null) existingImage.setNombre(imagenDetails.getNombre());
        if (imagenDetails.getMimeType() != null) existingImage.setMimeType(imagenDetails.getMimeType());
        if (imagenDetails.getFotoBase64() != null) existingImage.setFotoBase64(imagenDetails.getFotoBase64());
        if (imagenDetails.getProduct() != null) existingImage.setProduct(imagenDetails.getProduct());
        if (imagenDetails.getBrand() != null) existingImage.setBrand(imagenDetails.getBrand());
        if (imagenDetails.getUser() != null) existingImage.setUser(imagenDetails.getUser());

        // Nota: Se asume que Brand, Comments, Valorations y Stores no deberían actualizarse aquí.
        // Si es necesario actualizar estas relaciones, se debe hacer con cuidado para evitar efectos no deseados.

        imagenRepository.save(existingImage);
        return ResponseEntity.ok(existingImage);
    }


    @GetMapping("photo/user/{id}")
    public ResponseEntity<ImageModel> getImageByUserId(@PathVariable("id") Integer userId) {
        User user = userRespository.findById(userId).orElse(null);

        if (user != null && user.getProfileImage() != null) {
            return ResponseEntity.ok(user.getProfileImage());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/photo/product/{id}")
    public ResponseEntity<List<ImageModel>> getImagesByProductId(@PathVariable("id") Integer productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            if (product.getImages() != null && !product.getImages().isEmpty()) {
                List<ImageModel> images = new ArrayList<>(product.getImages());
                return ResponseEntity.ok(images);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("photo/brand/{id}")
    public ResponseEntity<ImageModel> getImageByBrandId(@PathVariable("id") Integer brandId) {
        Brand brand = brandRepository.findById(brandId).orElse(null);
        if (brand != null && brand.getProfileImage() != null) {
            return ResponseEntity.ok(brand.getProfileImage());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar imagen de perfil de usuario
    @PatchMapping("photo/user/{userId}")
    public ResponseEntity<ImageModel> actualizarImagenByUserId(@PathVariable Integer userId, @RequestBody ImageModel nuevaImagen) {
        User usuario = userRespository.findById(userId).orElse(null);

        if (usuario != null) {
            ImageModel imagenAnterior = usuario.getProfileImage();
            if (imagenAnterior != null) {
                // Eliminar la imagen anterior si existe
                imagenRepository.delete(imagenAnterior);
            }

            // Guardar la nueva imagen y asociarla al usuario
            ImageModel imagenGuardada = imagenRepository.save(nuevaImagen);
            usuario.setProfileImage(imagenGuardada);
            userRespository.save(usuario);

            return ResponseEntity.ok(imagenGuardada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar imágenes de producto
    @PatchMapping("photo/product/{productId}")
    public ResponseEntity< Set<ImageModel>> actualizarImagenesByProductId(@PathVariable Integer productId, @RequestBody List<ImageModel> nuevasImagenes) {
        Product producto = productRepository.findById(productId).orElse(null);

        if (producto != null) {
            // Eliminar las imágenes anteriores si existen
            if (producto.getImages() != null) {
                imagenRepository.deleteAll(producto.getImages());
            }

            // Guardar las nuevas imágenes y asociarlas al producto
            Set<ImageModel> imagenesGuardadas = new HashSet<>(imagenRepository.saveAll(nuevasImagenes));
            productRepository.save(producto);

            return ResponseEntity.ok(imagenesGuardadas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar imagen de perfil de marca
    @PatchMapping("photo/brand/{brandId}")
    public ResponseEntity<ImageModel> actualizarImagenByBrandId(@PathVariable Integer brandId, @RequestBody ImageModel nuevaImagen) {
        Brand marca = brandRepository.findById(brandId).orElse(null);

        if (marca != null) {
            ImageModel imagenAnterior = marca.getProfileImage();
            if (imagenAnterior != null) {
                // Eliminar la imagen anterior si existe
                imagenRepository.delete(imagenAnterior);
            }

            // Guardar la nueva imagen y asociarla a la marca
            ImageModel imagenGuardada = imagenRepository.save(nuevaImagen);
            marca.setProfileImage(imagenGuardada);
            brandRepository.save(marca);

            return ResponseEntity.ok(imagenGuardada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

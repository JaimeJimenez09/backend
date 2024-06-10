package SalesianosLosBoscos.Backend.fashionBlogApi.repositories;

import SalesianosLosBoscos.Backend.fashionBlogApi.dto.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagenRepository extends JpaRepository<ImageModel, Long> {
}

package SalesianosLosBoscos.Backend.fashionBlogApi.repositories;

import SalesianosLosBoscos.Backend.fashionBlogApi.dto.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByProductProductId(Integer productId);
    List<Comment> findByUserUserId(Integer userId);
}
    
package SalesianosLosBoscos.Backend.fashionBlogApi.controllers;

import SalesianosLosBoscos.Backend.fashionBlogApi.dto.Comment;
import SalesianosLosBoscos.Backend.fashionBlogApi.repositories.CommentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// CommentController.java
@RestController
@RequestMapping("/api/v1/comments")
@CrossOrigin(origins = "http://localhost:4200")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/")
    public ResponseEntity<List<Comment>> getComments() {
        List<Comment> listado = commentRepository.findAll();
        if (!listado.isEmpty()) {
            return ResponseEntity.ok(listado);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Comment>> getCommentsByProductId(@PathVariable Integer productId) {
        List<Comment> comments = commentRepository.findByProductProductId(productId);
        if (!comments.isEmpty()) {
            return ResponseEntity.ok(comments);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getCommentsByUserId(@PathVariable Integer userId) {
        List<Comment> comments = commentRepository.findByUserUserId(userId);
        if (!comments.isEmpty()) {
            return ResponseEntity.ok(comments);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentByID(@PathVariable("id") Integer id) {
        return commentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<Comment> addComment(@Valid @RequestBody Comment comment) {
        Comment savedComment = commentRepository.save(comment);
        if (savedComment.getId() != null) {
            return ResponseEntity.ok(savedComment);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Comment> deleteCommentById(@PathVariable Integer id) {
        return commentRepository.findById(id)
                .map(comment -> {
                    commentRepository.delete(comment);
                    return ResponseEntity.ok(comment);
                }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer id, @Valid @RequestBody Comment commentDetails) {
        return commentRepository.findById(id).map(existingComment -> {
            existingComment.setContent(commentDetails.getContent());
            existingComment.setCommentDate(commentDetails.getCommentDate());
            existingComment.setUser(commentDetails.getUser());
            existingComment.setProduct(commentDetails.getProduct());
            commentRepository.save(existingComment);
            return ResponseEntity.ok(existingComment);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/")
    public ResponseEntity<Comment> editComment(@RequestBody Comment comment) {
        if (comment == null || comment.getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        return commentRepository.findById(comment.getId()).map(existingComment -> {
            if (comment.getContent() != null) {
                existingComment.setContent(comment.getContent());
            }
            if (comment.getCommentDate() != null) {
                existingComment.setCommentDate(comment.getCommentDate());
            }
            if (comment.getUser() != null) {
                existingComment.setUser(comment.getUser());
            }
            if (comment.getProduct() != null) {
                existingComment.setProduct(comment.getProduct());
            }
            commentRepository.save(existingComment);
            return ResponseEntity.ok(existingComment);
        }).orElse(ResponseEntity.notFound().build());
    }
}

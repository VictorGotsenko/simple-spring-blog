package spring.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.blog.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}

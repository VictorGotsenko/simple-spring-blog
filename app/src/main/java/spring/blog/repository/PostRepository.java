package spring.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.blog.model.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByPublishedTrue(Pageable pageable);
}

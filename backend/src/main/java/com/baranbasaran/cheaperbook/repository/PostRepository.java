package com.baranbasaran.cheaperbook.repository;

import com.baranbasaran.cheaperbook.model.Post;
import com.baranbasaran.cheaperbook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

    List<Post> findAllByUser(User user);

}

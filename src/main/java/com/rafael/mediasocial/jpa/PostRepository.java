package com.rafael.mediasocial.jpa;

import com.rafael.mediasocial.user.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}

package com.rafael.mediasocial.jpa;

import com.rafael.mediasocial.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}

package com.alpha.www.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.www.client.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}

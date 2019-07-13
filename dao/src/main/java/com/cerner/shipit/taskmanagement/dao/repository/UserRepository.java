package com.cerner.shipit.taskmanagement.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cerner.shipit.taskmanagement.utility.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}

package com.cerner.shipit.taskmanagement.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cerner.shipit.taskmanagement.utility.entity.Teams;

@Repository
public interface TeamsRepository extends JpaRepository<Teams, Long> {

}

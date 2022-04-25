package com.unpeu.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unpeu.domain.entity.Present;
import com.unpeu.domain.entity.User;

public interface IPresentRepository extends JpaRepository<Present, Long> {
	// @Query(value = "SELECT * FROM present where user_id = :userId", nativeQuery = true)
	// List<Present> getPresentListByUserId(@Param("userId") Long userId);
	List<Present> findPresentByUser_id(Long userId);
}

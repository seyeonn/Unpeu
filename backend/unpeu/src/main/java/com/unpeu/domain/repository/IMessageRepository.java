package com.unpeu.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unpeu.domain.entity.Message;
import com.unpeu.domain.entity.Present;
import com.unpeu.domain.entity.User;

public interface IMessageRepository extends JpaRepository<Message, Long> {
	// Optional<Message> findByPresent(Long presentId);
	Optional<Message> findFirstByPresent(Present present);
	@Query(value = "SELECT SUM(price) FROM message, present where user_id = :userId", nativeQuery = true)
	String sumPeekMoney(@Param("userId") Long userId);
}

package com.audiometria.audiometria.api.repository.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLabelMapRepository extends JpaRepository<UserLabelMap, Long> {

//    @Query("SELECT u FROM users u " +
//            "JOIN UserLabelMap lm ON lm.userName = u.username " +
//            "WHERE lm.labelName = :labelName")
//    Optional<User> findUsuarioByLabel(@Param("labelName") String labelName);

        Optional<User> findByLabelName(String labelName);
}

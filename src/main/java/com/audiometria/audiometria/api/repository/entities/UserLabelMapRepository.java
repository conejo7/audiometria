package com.audiometria.audiometria.api.repository.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLabelMapRepository extends JpaRepository<UserLabelMap, Long> {

        Optional<UserLabelMap> findByLabelName(String labelName);
}

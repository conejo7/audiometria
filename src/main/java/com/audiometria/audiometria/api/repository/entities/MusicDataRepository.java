package com.audiometria.audiometria.api.repository.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MusicDataRepository extends JpaRepository<MusicData, Long> {
    List<MusicData> findAllByUser_Id(UUID userId);
}

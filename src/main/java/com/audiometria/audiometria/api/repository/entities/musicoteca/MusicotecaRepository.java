package com.audiometria.audiometria.api.repository.entities.musicoteca;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicotecaRepository extends JpaRepository<Musicoteca,Integer>, JpaSpecificationExecutor<Musicoteca> {


}

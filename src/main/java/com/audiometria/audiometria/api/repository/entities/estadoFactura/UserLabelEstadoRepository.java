package com.audiometria.audiometria.api.repository.entities.estadoFactura;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLabelEstadoRepository extends JpaRepository<UserLabelEstado, Long>
, JpaSpecificationExecutor<UserLabelEstado> {

    Optional<UserLabelEstado> findByUserNameAndLabelNameAndAnioAndMes(
            String userName,
            String labelName,
            Integer anio,
            Integer mes
    );

    List<UserLabelEstado> findAllByAnioAndMes(Integer anio, Integer mes);

    List<UserLabelEstado> findAllByLabelNameAndAnioAndMes(
            String labelName,
            Integer anio,
            Integer mes
    );

    boolean existsByUserNameAndLabelNameAndAnioAndMes(
            String userName,
            String labelName,
            Integer anio,
            Integer mes
    );

}

package com.audiometria.audiometria.api.service;

import com.audiometria.audiometria.api.repository.dto.MusicExcelDTO;
import com.audiometria.audiometria.api.repository.entities.MusicData;
import com.audiometria.audiometria.api.repository.entities.MusicDataRepository;
import com.audiometria.audiometria.api.repository.entities.User;
import com.audiometria.audiometria.api.repository.entities.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExcelImportService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MusicDataRepository musicDataRepository;

    public void importarDatos(List<MusicExcelDTO> registros) {
        for (MusicExcelDTO dto : registros) {
            User user = userRepository.findByUsername(dto.getUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + dto.getUsuario()));

            MusicData entity = new MusicData();
            entity.setUser(user);
            entity.setTitle(dto.getTitle());
            entity.setArtist(dto.getArtist());
            entity.setAmountEur(dto.getAmountEur());
            entity.setAmountUsd(dto.getAmountEur().multiply(BigDecimal.valueOf(1.08))); // ejemplo de tasa

            musicDataRepository.save(entity);
        }
    }
}

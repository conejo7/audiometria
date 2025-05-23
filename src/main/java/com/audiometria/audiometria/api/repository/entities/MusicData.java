package com.audiometria.audiometria.api.repository.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "music_data")
public class MusicData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;
    private String artist;
    private BigDecimal amountEur;
    private BigDecimal amountUsd;

    // otros campos extra si deseas

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

}

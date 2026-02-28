package com.audiometria.audiometria.api.repository.entities.musicoteca;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@Getter
@Setter
@Entity
@Table(name = "musicoteca")
public class Musicoteca {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "musicoteca_seq")
    @SequenceGenerator(
            name = "musicoteca_seq",
            sequenceName = "musicoteca_seq",
            allocationSize = 1000
    )
    private Long id;
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // private Long id;

    private String title;
    private String artist;
    private String album;
    private String genre;

    @Column(name = "sale_start_date")
    private LocalDateTime  saleStartDate;

    @Column(name = "sale_end_date")
    private LocalDateTime  saleEndDate;

    private String dsp;

    @Column(name = "sale_store_name")
    private String saleStoreName;

    @Column(name = "sale_type")
    private String saleType;

    @Column(name = "sale_user_type")
    private String saleUserType;

    private String territory;

    @Column(name = "product_upc")
    private String productUpc;

    @Column(name = "product_reference")
    private String productReference;

    @Column(name = "product_catalog_number")
    private String productCatalogNumber;

    @Column(name = "product_label")
    private String productLabel;

    @Column(name = "product_artist")
    private String productArtist;

    @Column(name = "product_title")
    private String productTitle;

    @Column(name = "asset_artist")
    private String assetArtist;

    @Column(name = "asset_title")
    private String assetTitle;

    @Column(name = "asset_version")
    private String assetVersion;

    @Column(name = "asset_duration")
    private String assetDuration;

    @Column(name = "asset_isrc")
    private String assetIsrc;

    @Column(name = "asset_reference")
    private String assetReference;

    @Column(name = "asset_product")
    private String assetProduct;

    @Column(name = "product_quantity")
    private Integer productQuantity;

    @Column(name = "asset_quantity")
    private Integer assetQuantity;

    @Column(name = "original_gross_income", precision = 15, scale = 2)
    private BigDecimal originalGrossIncome;

    @Column(name = "original_currency")
    private String originalCurrency;

    @Column(name = "exchange_rate", precision = 10, scale = 4)
    private BigDecimal exchangeRate;

    @Column(name = "converted_gross_income", precision = 15, scale = 2)
    private BigDecimal convertedGrossIncome;

    @Column(name = "contract_deal_term")
    private String contractDealTerm;

    @Column(name = "reported_royalty", precision = 20, scale = 16)
    private BigDecimal reportedRoyalty;

    private String currency;

    @Column(name = "report_run_id")
    private String reportRunId;

    @Column(name = "report_id")
    private String reportId;

    @Column(name = "sale_id")
    private String saleId;

    @Column(name = "amount_eur", precision = 15, scale = 2)
    private BigDecimal amountEur;

    @Column(name = "amount_usd", precision = 15, scale = 2)
    private BigDecimal amountUsd;

    @Column(name = "extra_column_10")
    private String extraColumn10;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "date_user")
    private LocalDateTime dateUser;

    @Column(name = "state")
    private Long state;

    @Column(name = "upload_id")
    private UUID uploadId;

    @Column(name = "euro", precision = 20, scale = 16)
    private BigDecimal euro;


}

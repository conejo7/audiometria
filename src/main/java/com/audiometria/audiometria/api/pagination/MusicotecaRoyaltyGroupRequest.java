package com.audiometria.audiometria.api.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusicotecaRoyaltyGroupRequest {

    private SearchRequest searchRequest;

    /**
     * Valores permitidos:
     * dsp
     * territory
     * productTitle
     * assetTitle
     */
    private String groupBy;
}

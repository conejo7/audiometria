package com.audiometria.audiometria.api.controllers.request;

import com.audiometria.audiometria.api.pagination.SearchRequest;
import lombok.Data;

@Data
public class MusicotecaRoyaltyGroupedExportRequest  {

    private SearchRequest searchRequest;

    private String groupBy;

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }
}

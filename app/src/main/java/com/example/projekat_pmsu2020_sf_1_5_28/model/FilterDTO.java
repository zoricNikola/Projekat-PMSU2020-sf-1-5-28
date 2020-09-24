package com.example.projekat_pmsu2020_sf_1_5_28.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilterDTO implements Serializable {

    private static final long serialVersionUID = 3499048387510708996L;

    private String searchText;

    private List<Tag> filteringTags = new ArrayList<>();

    public FilterDTO () {}

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<Tag> getFilteringTags() {
        return filteringTags;
    }

    public void setFilteringTags(List<Tag> filteringTags) {
        this.filteringTags = filteringTags;
    }

}

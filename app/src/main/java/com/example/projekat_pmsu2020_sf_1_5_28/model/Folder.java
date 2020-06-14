package com.example.projekat_pmsu2020_sf_1_5_28.model;

import java.io.Serializable;
import java.util.List;

public class Folder implements Serializable {

    private String name;

    private List <Folder> FoldersList;

    public Folder () {}

    public Folder(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Folder> getFoldersList() {
        return FoldersList;
    }

    public void setFoldersList(List<Folder> foldersList) {
        FoldersList = foldersList;
    }

}

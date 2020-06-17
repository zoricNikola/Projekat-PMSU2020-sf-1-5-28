package com.example.projekat_pmsu2020_sf_1_5_28.model;

import java.io.Serializable;
import java.util.List;

public class Folder implements Serializable {

    private String name;
    private List<Email> emailsList;
    private Folder parentFolder;
    private List<Folder> foldersList;

    public Folder () {}

    public Folder(String name, List<Email> emailsList, Folder parentFolder, List<Folder> foldersList)
    {
        this.name = name;
        this.emailsList = emailsList;
        this.parentFolder = parentFolder;
        this.foldersList = foldersList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Folder> getFoldersList() {
        return foldersList;
    }

    public void setFoldersList(List<Folder> foldersList) {
        this.foldersList = foldersList;
    }

    public List<Email> getEmailsList() {
        return emailsList;
    }

    public void setEmailsList(List<Email> emailsList) {
        this.emailsList = emailsList;
    }

    public Folder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }

    public int getNumberOfEmails() {
        return this.emailsList.size();
    }

}

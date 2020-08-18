package com.example.projekat_pmsu2020_sf_1_5_28.tools;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.projekat_pmsu2020_sf_1_5_28.model.Contact;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Email;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Folder;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Mokap {

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static List<Email> getEmails() {
//        ArrayList<Email> emails = new ArrayList<>();
//
//        Tag tag1 = new Tag("Urgent", android.R.color.holo_red_dark);
//        Tag tag2 = new Tag("Work", android.R.color.holo_blue_dark);
//        Tag tag3 = new Tag("School", android.R.color.holo_green_dark);
//
//        ArrayList<Tag> tags1 = new ArrayList<>();
//        tags1.add(tag1);
//        tags1.add(tag2);
//
//        ArrayList<Tag> tags2 = new ArrayList<>();
//        tags2.add(tag1);
//        tags2.add(tag3);
//
//        ArrayList<Tag> tags3 = new ArrayList<>();
//
//        Email e1 = new Email("znikoolaa@gmail.com", "nikola.se.zoric@gmail.com",
//                "", "",
//                LocalDateTime.of(2020, 4, 27, 15, 35),
//                "Ovo je subject", "Ovo je content", tags1);
//
//        Email e2 = new Email("stefan.kockar@gmail.com", "nikola.se.zoric@gmail.com",
//                "", "",
//                LocalDateTime.of(2020, 4, 26, 17, 15),
//                "Subject...", "Content content content content", tags3);
//
//        Email e3 = new Email("boris.jankovic@gmail.com", "nikola.se.zoric@gmail.com",
//                "", "",
//                LocalDateTime.of(2019, 4, 26, 9, 47),
//                "No subject", "Bla bla bla", tags2);
//
//        Email e4 = new Email("znikoolaa@gmail.com", "nikola.se.zoric@gmail.com",
//                "", "",
//                LocalDateTime.of(2020, 4, 27, 15, 35),
//                "Ovo je subject", "Ovo je content", tags2);
//
//        Email e5 = new Email("stefan.kockar@gmail.com", "nikola.se.zoric@gmail.com",
//                "", "",
//                LocalDateTime.of(2020, 4, 26, 17, 15),
//                "Subject...", "Content content content content", tags1);
//
//        Email e6 = new Email("boris.jankovic@gmail.com", "nikola.se.zoric@gmail.com",
//                "", "",
//                LocalDateTime.of(2019, 4, 26, 9, 47),
//                "No subject", "Bla bla bla", tags3);
//
//        Email e7 = new Email("znikoolaa@gmail.com", "nikola.se.zoric@gmail.com",
//                "", "",
//                LocalDateTime.of(2020, 4, 27, 15, 35),
//                "Ovo je subject", "Ovo je content", tags1);
//
//        Email e8 = new Email("stefan.kockar@gmail.com", "nikola.se.zoric@gmail.com",
//                "", "",
//                LocalDateTime.of(2020, 4, 26, 17, 15),
//                "Subject...", "Content content content content", tags2);
//
//        Email e9 = new Email("boris.jankovic@gmail.com", "nikola.se.zoric@gmail.com",
//                "", "",
//                LocalDateTime.of(2019, 4, 26, 9, 47),
//                "No subject", "Bla bla bla", tags3);
//
//        Email e10 = new Email("znikoolaa@gmail.com", "nikola.se.zoric@gmail.com",
//                "", "",
//                LocalDateTime.of(2020, 4, 27, 15, 35),
//                "Ovo je subject", "Ovo je content", tags2);
//
//        Email e11 = new Email("stefan.kockar@gmail.com", "nikola.se.zoric@gmail.com",
//                "", "",
//                LocalDateTime.of(2020, 4, 26, 17, 15),
//                "Subject...", "Content content content content", tags1);
//
//        Email e12 = new Email("boris.jankovic@gmail.com", "nikola.se.zoric@gmail.com",
//                "", "",
//                LocalDateTime.of(2019, 4, 26, 9, 47),
//                "No subject", "Bla bla bla", tags3);
//
//        emails.add(e1);
//        emails.add(e2);
//        emails.add(e3);
//        emails.add(e4);
//        emails.add(e5);
//        emails.add(e6);
//        emails.add(e7);
//        emails.add(e8);
//        emails.add(e9);
//        emails.add(e10);
//        emails.add(e11);
//        emails.add(e12);
//
//
//        return emails;
//    }
//
//    public static List<Contact> getContacts() {
//        ArrayList<Contact> contacts = new ArrayList<>();
//
//
//        Contact c1 = new Contact("Nikola","Zoric","Nikola Z.", "nikola.zoric@gmail.com");
//        Contact c2 = new Contact("Stefan", "Kockar","Stefan K.", "stefan.kockar@gmai.com");
//        Contact c3 = new Contact("Boris", "Jankovic", "Boris J.", "boris.jankovic@gmail.com");
//        Contact c4 = new Contact("Nikola","Zoric","Nikola Z.", "nikola.zoric@gmail.com");
//        Contact c5 = new Contact("Stefan", "Kockar","Stefan K.", "stefan.kockar@gmai.com");
//        Contact c6 = new Contact("Boris", "Jankovic", "Boris J.", "boris.jankovic@gmail.com");
//        Contact c7 = new Contact("Nikola","Zoric","Nikola Z.", "nikola.zoric@gmail.com");
//        Contact c8 = new Contact("Stefan", "Kockar","Stefan K.", "stefan.kockar@gmai.com");
//        Contact c9 = new Contact("Boris", "Jankovic", "Boris J.", "boris.jankovic@gmail.com");
//        Contact c10 = new Contact("Nikola","Zoric","Nikola Z.", "nikola.zoric@gmail.com");
//        Contact c11 = new Contact("Stefan", "Kockar","Stefan K.", "stefan.kockar@gmai.com");
//        Contact c12 = new Contact("Boris", "Jankovic", "Boris J.", "boris.jankovic@gmail.com");
//
//        contacts.add(c1);
//        contacts.add(c2);
//        contacts.add(c3);
//        contacts.add(c4);
//        contacts.add(c5);
//        contacts.add(c6);
//        contacts.add(c7);
//        contacts.add(c8);
//        contacts.add(c9);
//        contacts.add(c10);
//        contacts.add(c11);
//        contacts.add(c12);
//
//        return contacts;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static List<Folder> getFolders() {
//        ArrayList<Folder> folders = new ArrayList<>();
//        Folder inbox = new Folder("Inbox", Mokap.getEmails(), null, new ArrayList<Folder>());
//
//        Folder drafts = new Folder("Drafts", new ArrayList<Email>(), null, new ArrayList<Folder>());
//
//        Folder spam = new Folder("Spam", Mokap.getEmails(), inbox, new ArrayList<Folder>());
//        inbox.getFoldersList().add(spam);
//
//        Folder job = new Folder("Work", new ArrayList<Email>(), inbox, new ArrayList<Folder>());
//        inbox.getFoldersList().add(job);
//
//        Folder fullTimeJob = new Folder("Full time job", Mokap.getEmails(), job, new ArrayList<Folder>());
//        job.getFoldersList().add(fullTimeJob);
//        Folder freelanceJob = new Folder("Freelance job", Mokap.getEmails(), job, new ArrayList<Folder>());
//        job.getFoldersList().add(freelanceJob);
//
//        Folder social = new Folder("Social", Mokap.getEmails(), inbox, new ArrayList<Folder>());
//
//        folders.add(inbox);
//        folders.add(drafts);
//        folders.add(spam);
//        folders.add(job);
//        folders.add(fullTimeJob);
//        folders.add(freelanceJob);
//        folders.add(social);
//
//        return folders;
//    }
}

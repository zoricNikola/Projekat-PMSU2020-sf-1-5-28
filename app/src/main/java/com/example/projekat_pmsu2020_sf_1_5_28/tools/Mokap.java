package com.example.projekat_pmsu2020_sf_1_5_28.tools;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.projekat_pmsu2020_sf_1_5_28.model.Email;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Mokap {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Email> getEmails() {
        ArrayList<Email> emails = new ArrayList<>();

        Email e1 = new Email("znikoolaa@gmail.com", "nikola.se.zoric@gmail.com",
                "", "",
                LocalDateTime.of(2020, 4, 27, 15, 35),
                "Ovo je subject", "Ovo je content");

        Email e2 = new Email("stefan.kockar@gmail.com", "nikola.se.zoric@gmail.com",
                "", "",
                LocalDateTime.of(2020, 4, 26, 17, 15),
                "Subject...", "Content content content content");

        Email e3 = new Email("boris.jankovic@gmail.com", "nikola.se.zoric@gmail.com",
                "", "",
                LocalDateTime.of(2019, 4, 26, 9, 47),
                "No subject", "Bla bla bla");

        Email e4 = new Email("znikoolaa@gmail.com", "nikola.se.zoric@gmail.com",
                "", "",
                LocalDateTime.of(2020, 4, 27, 15, 35),
                "Ovo je subject", "Ovo je content");

        Email e5 = new Email("stefan.kockar@gmail.com", "nikola.se.zoric@gmail.com",
                "", "",
                LocalDateTime.of(2020, 4, 26, 17, 15),
                "Subject...", "Content content content content");

        Email e6 = new Email("boris.jankovic@gmail.com", "nikola.se.zoric@gmail.com",
                "", "",
                LocalDateTime.of(2019, 4, 26, 9, 47),
                "No subject", "Bla bla bla");

        Email e7 = new Email("znikoolaa@gmail.com", "nikola.se.zoric@gmail.com",
                "", "",
                LocalDateTime.of(2020, 4, 27, 15, 35),
                "Ovo je subject", "Ovo je content");

        Email e8 = new Email("stefan.kockar@gmail.com", "nikola.se.zoric@gmail.com",
                "", "",
                LocalDateTime.of(2020, 4, 26, 17, 15),
                "Subject...", "Content content content content");

        Email e9 = new Email("boris.jankovic@gmail.com", "nikola.se.zoric@gmail.com",
                "", "",
                LocalDateTime.of(2019, 4, 26, 9, 47),
                "No subject", "Bla bla bla");

        Email e10 = new Email("znikoolaa@gmail.com", "nikola.se.zoric@gmail.com",
                "", "",
                LocalDateTime.of(2020, 4, 27, 15, 35),
                "Ovo je subject", "Ovo je content");

        Email e11 = new Email("stefan.kockar@gmail.com", "nikola.se.zoric@gmail.com",
                "", "",
                LocalDateTime.of(2020, 4, 26, 17, 15),
                "Subject...", "Content content content content");

        Email e12 = new Email("boris.jankovic@gmail.com", "nikola.se.zoric@gmail.com",
                "", "",
                LocalDateTime.of(2019, 4, 26, 9, 47),
                "No subject", "Bla bla bla");

        emails.add(e1);
        emails.add(e2);
        emails.add(e3);
        emails.add(e4);
        emails.add(e5);
        emails.add(e6);
        emails.add(e7);
        emails.add(e8);
        emails.add(e9);
        emails.add(e10);
        emails.add(e11);
        emails.add(e12);


        return emails;
    }
}

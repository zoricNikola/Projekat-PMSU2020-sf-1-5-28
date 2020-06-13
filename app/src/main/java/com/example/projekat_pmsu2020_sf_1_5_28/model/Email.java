package com.example.projekat_pmsu2020_sf_1_5_28.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Email implements Serializable {

    private String from;
    private String to;
    private String cc;
    private String bcc;
    private LocalDateTime dateTime;
    private String subject;
    private String content;

    public Email () {}

    public Email(String from, String to, String cc, String bcc, LocalDateTime dateTime, String subject, String content) {
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.dateTime = dateTime;
        this.subject = subject;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDateTimeString() {
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime emailTime = this.getDateTime();
        String dateTimeOutput = "";

        if (emailTime.getYear() == timeNow.getYear()) {
            if (emailTime.getMonth() == timeNow.getMonth()) {
                if (emailTime.getDayOfMonth() == timeNow.getDayOfMonth()) {
                    dateTimeOutput = emailTime.getHour() +
                            ":" + emailTime.getMinute();
                    return dateTimeOutput;
                }
                else {
                    dateTimeOutput = emailTime.getDayOfMonth() + "." + emailTime.getMonth();
                    return dateTimeOutput;
                }
            }
            else {
                dateTimeOutput = emailTime.getDayOfMonth() + "." + emailTime.getMonth();
                return dateTimeOutput;
            }
        }
        else {
            dateTimeOutput = emailTime.getDayOfMonth() + "." + emailTime.getMonth() + " " +
                    emailTime.getYear() + ".";
            return dateTimeOutput;
        }
    }
}

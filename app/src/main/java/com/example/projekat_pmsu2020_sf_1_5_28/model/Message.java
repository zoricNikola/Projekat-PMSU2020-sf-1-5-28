package com.example.projekat_pmsu2020_sf_1_5_28.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Message implements Serializable {

    private static final long serialVersionUID = -2640990194597541890L;

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("cc")
    @Expose
    private String cc;
    @SerializedName("bcc")
    @Expose
    private String bcc;
    @SerializedName("dateTime")
    @Expose
    private String dateTime;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("unread")
    @Expose
    private Boolean unread;
    @SerializedName("tags")
    @Expose
    private List<Tag> tags = null;

    public Message() {}

    public Message(Long id, String from, String to, String cc, String bcc, String dateTime, String subject,
                   String content, boolean unread) {
        super();
        this.id = id;
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.dateTime = dateTime;
        this.subject = subject;
        this.content = content;
        this.unread = unread;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
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

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDateTimeString() {
        LocalDateTime timeNow = LocalDateTime.now();
        String[] dateTimeSplitted = this.getDateTime().split("T");
        String date = dateTimeSplitted[0];
        String[] dateSplitted = date.split("-");
        int dayOfMonth = Integer.parseInt(dateSplitted[2]);
        int month = Integer.parseInt(dateSplitted[1]);
        int year = Integer.parseInt(dateSplitted[0]);
        String time = dateTimeSplitted[1];
        String[] timeSplitted = time.split(":");
        int hour = Integer.parseInt(timeSplitted[0]);
        int minute = Integer.parseInt(timeSplitted[1]);

        LocalDateTime emailTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
        String dateTimeOutput = "";
        if (emailTime.getYear() == timeNow.getYear()) {
            if (emailTime.getMonth() == timeNow.getMonth()) {
                if (emailTime.getDayOfMonth() == timeNow.getDayOfMonth()) {
                    dateTimeOutput = ( emailTime.getHour() < 10 ? ("0" + emailTime.getHour()) : emailTime.getHour() )
                            + ":" + ( emailTime.getMinute() < 10 ? ("0" + emailTime.getMinute()) : emailTime.getMinute() );
                    return dateTimeOutput;
                }
                else {
                    dateTimeOutput = emailTime.getDayOfMonth() + ". " + emailTime.getMonth();
                    return dateTimeOutput;
                }
            }
            else {
                dateTimeOutput = emailTime.getDayOfMonth() + ". " + emailTime.getMonth();
                return dateTimeOutput;
            }
        }
        else {
            dateTimeOutput = emailTime.getDayOfMonth() + ". " + emailTime.getMonth() + " " +
                    emailTime.getYear() + ".";
            return dateTimeOutput;
        }
    }
}

package io.pivotal.pal.tracker;

import java.time.LocalDate;

public class TimeEntry {
    private long id;
    private long projectId;
    private long userId;
    private LocalDate date;
    private int hours;

    public TimeEntry() {

    }

    public TimeEntry(long projectId, long userId, String date, int hours) {
        this.projectId = projectId;
        this.userId = userId;
        this.date = LocalDate.parse(date);
        this.hours = hours;
    }

    public TimeEntry(long id, long projectId, long userId, String date, int hours) {
        this(projectId, userId, date, hours);
        this.id = id;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TimeEntry &&
                ((TimeEntry) obj).getId() == this.id &&
                ((TimeEntry) obj).getDate().equals(this.date) &&
                ((TimeEntry) obj).getHours() == this.hours &&
                ((TimeEntry) obj).getProjectId() == this.projectId &&
                ((TimeEntry) obj).getUserId() == this.userId);
    }
}

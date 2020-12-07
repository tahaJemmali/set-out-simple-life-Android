package tn.esprit.setoutlife.entities;

import java.util.Date;

public class Task {
    String id;
    String taskName;
    int importance;
    int enjoyment;
    String note;
    Date dateCreation;
    Date deadline;
    Date reminder;
    Date endTime;
    boolean schedule=false;
    public Task(){

    }
    //Schedule
    public Task(String id,String taskName, int importance, int enjoyment, String note, Date dateCreation, Date endTime, boolean schedule) {
        this.id = id;
        this.taskName = taskName;
        this.importance = importance;
        this.enjoyment = enjoyment;
        this.note = note;
        this.dateCreation = dateCreation;
        this.endTime = endTime;
        this.schedule = schedule;
    }

    //Task
    public Task(String id,String taskName, Date dateCreation,Date deadline,Date reminder,int importance, int enjoyment, String note,boolean schedule) {
        this.id = id;
        this.taskName = taskName;
        this.importance = importance;
        this.enjoyment = enjoyment;
        this.note = note;
        this.dateCreation = dateCreation;
        this.deadline = deadline;
        this.reminder = reminder;
        this.schedule = schedule;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public int getEnjoyment() {
        return enjoyment;
    }

    public void setEnjoyment(int enjoyment) {
        this.enjoyment = enjoyment;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getReminder() {
        return reminder;
    }

    public void setReminder(Date reminder) {
        this.reminder = reminder;
    }


    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isSchedule() {
        return schedule;
    }

    public void setSchedule(boolean schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", taskName='" + taskName + '\'' +
                ", importance=" + importance +
                ", enjoyment=" + enjoyment +
                ", note='" + note + '\'' +
                ", dateCreation=" + dateCreation +
                ", deadline=" + deadline +
                ", reminder=" + reminder +
                ", endTime=" + endTime +
                ", schedule=" + schedule +
                '}';
    }

    public String getId() {
        return id;
    }
}

package tn.esprit.setoutlife.entities;

import java.util.ArrayList;
import java.util.Date;

public class Project {
    String id;
    String name;
    String description;
    ArrayList<Task> tasks;
    Date dateCreated;
    Tag tag;

    public Project(){

    }

    public Project(String id, String name, String description, ArrayList<Task> tasks, Date dateCreated, Tag tag) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tasks = tasks;
        this.dateCreated = dateCreated;
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tasks=" + tasks +
                ", dateCreated=" + dateCreated +
                ", tag=" + tag +
                '}';
    }
}

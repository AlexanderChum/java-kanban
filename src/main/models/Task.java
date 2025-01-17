package main.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private int id;
    private String name;
    private String description;
    private Status status;
    private TypesOfTasks type;
    private int duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = 0;
        this.startTime = null;
    }

    public Task(Integer id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, Integer duration) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = duration;
        this.startTime = null;
    }

    public Task(String name, String description, String startTime) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        if (startTime != null) {
            this.startTime = LocalDateTime.parse(startTime, FORMATTER);
        } else this.startTime = null;
    }

    public Task(String name, String description, Integer duration, String startTime) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = duration;
        if (startTime != null) {
            this.startTime = LocalDateTime.parse(startTime, FORMATTER);
        } else this.startTime = null;
    }

    public Task(String name, String description, String duration, String startTime, String endTime) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = Integer.parseInt(duration);
        if (startTime != null) {
            this.startTime = LocalDateTime.parse(startTime, FORMATTER);
        } else this.startTime = null;
        if (endTime != null) {
            this.endTime = LocalDateTime.parse(endTime, FORMATTER);
        } else this.endTime = null;
    }

    //---------------------------------------

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int generatedId) {
        this.id = generatedId;
    }

    public TypesOfTasks getType() {
        return type;
    }

    public void setType(TypesOfTasks type) {
        this.type = type;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        if (this.startTime != null) {
            return startTime.plusMinutes(duration);
        } else return null;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getDuration() {
        return this.duration;
    }

    //--------------------------------------

    @Override
    public String toString() {
        return "Task{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", startTime='" + getStartTime() + '\'' +
                ", endTime=" + getEndTime() +
                '}';
    }
}

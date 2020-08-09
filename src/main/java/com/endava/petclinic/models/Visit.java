package com.endava.petclinic.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Visit {

    private String date;
    private String description;
    private Integer id;
    private Pet pet;

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Visit(String date, String description, Integer id) {
        this.date = date;
        this.description = description;
        this.id = id;
    }

    public Visit() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visit visit = (Visit) o;
        return Objects.equals(date, visit.date) &&
                Objects.equals(description, visit.description) &&
                Objects.equals(id, visit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, description, id);
    }

    @Override
    public String toString() {
        return "Visit{" +
                "date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                '}';
    }
}

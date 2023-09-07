package com.mssecurity.mssecurity.Models;

// Agregamos decoradores
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document

public class Role {
    // Agregamos los atributos
    // Debemos colocar el @Id para identificar cual es la Primary Key en la base de datos.
    @Id
    private String _id;
    private String name;
    private String description;

    // Constructor
    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // getters y setters

    public String get_id() {
        return _id;
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
}

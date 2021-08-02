package com.wherex.appventas.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
@Getter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "producto",uniqueConstraints= @UniqueConstraint(columnNames={"nombre"}))
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @Min(value = 0)
    private Double precio;
    @Min(value = 0)
    private Double cantidad;

    private static final long serialVersionUID = 1L;
}

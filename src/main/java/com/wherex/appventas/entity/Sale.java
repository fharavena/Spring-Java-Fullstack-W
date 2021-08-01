package com.wherex.appventas.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "venta")
public class Sale implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    private Double iva;
    private Double descuento;
    private Double total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Client cliente;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="venta_id")
    private List<Detail> items;

    private static final long serialVersionUID = 1L;
}

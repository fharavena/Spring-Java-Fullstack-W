package com.wherex.appventas.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SaleInputEditDTO {
    private Long id;
    private Date fecha;
    private Double descuento;
    private Long cliente;
    private List<SaleItemInputDTO> item;
}

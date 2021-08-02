package com.wherex.appventas.domain;

import lombok.Data;

import java.util.List;

@Data
public class SaleInputDTO {
    private Double descuento;
    private Long cliente;
    private List<SaleItemInputDTO> item;
}

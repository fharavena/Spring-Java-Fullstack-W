package com.wherex.appventas.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class SaleItemInputDTO {
    private Double cantidad;
    private Long producto;
}

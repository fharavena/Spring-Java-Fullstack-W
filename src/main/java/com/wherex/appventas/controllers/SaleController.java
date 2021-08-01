package com.wherex.appventas.controllers;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.wherex.appventas.domain.SaleInputDTO;
import com.wherex.appventas.domain.SaleSimpleListDTO;
import com.wherex.appventas.entity.Sale;
import com.wherex.appventas.repository.SaleRepository;
import com.wherex.appventas.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.transform.Result;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/sale")
@Validated
public class SaleController {

    @Autowired
    private SaleService saleService;
    @Autowired
    private SaleRepository saleRepository;

    @GetMapping(value = "list")
    public ResponseEntity<?> list() {
        List<SaleSimpleListDTO> response = saleService.findSales();
        return new ResponseEntity<List<SaleSimpleListDTO>>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Sale response = saleRepository.findById(id).orElse(null);
        return new ResponseEntity<Sale>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/insert")
    public ResponseEntity<?> create(@Valid @RequestBody(required = true) SaleInputDTO saleInput) {
        String output = saleService.saveSale(saleInput);
        return new ResponseEntity<String>(output, HttpStatus.CREATED);
    }

    @ExceptionHandler( HttpMessageNotReadableException.class)
    public ResponseEntity<?> unknownKey(JsonMappingException exception) {
        String msg = "Error en la construccion JSON de entrada";
        return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
    }
}

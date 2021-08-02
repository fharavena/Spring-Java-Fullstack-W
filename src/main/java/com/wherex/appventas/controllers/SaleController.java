package com.wherex.appventas.controllers;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.wherex.appventas.domain.SaleInputDTO;
import com.wherex.appventas.domain.SaleSimpleListDTO;
import com.wherex.appventas.entity.Sale;
import com.wherex.appventas.repository.SaleRepository;
import com.wherex.appventas.service.IService.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/sale")
@Validated
public class SaleController {

    @Autowired
    private ISaleService saleService;
    @Autowired
    private SaleRepository saleRepository;

    @GetMapping(value = "")
    public ResponseEntity<?> list() {

        Map<String, Object> response = new HashMap<>();

        response.put("status", "success");
        response.put("data", saleService.findSales());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();

        Sale sale = saleRepository.findById(id).orElse(null);

        if (sale == null) {
            response.put("status", "error");
            response.put("message", "Producto con id no encontrado");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        response.put("status", "success");
        response.put("data", sale);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PostMapping(path = "")
    public ResponseEntity<?> create(@Valid @RequestBody(required = true) SaleInputDTO saleInput) {
        Map<String, Object> response = new HashMap<>();
        if (saleInput == null) {
            response.put("status", "error");
            response.put("message", "Producto con id no encontrado");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> output = saleService.saveSale(saleInput);

        if (output.containsKey("error")) {
            output.put("status", "error");
            return new ResponseEntity<Map<String, Object>>(output, HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<Map<String, Object>>(output, HttpStatus.CREATED);
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody(required = true) Sale saleInput) {
        Map<String, Object> response = new HashMap<>();
        if (saleInput == null) {
            response.put("error", "body vacio");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> output = saleService.editSale(saleInput);

        if (output.containsKey("error")) {
            return new ResponseEntity<Map<String, Object>>(output, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Map<String, Object>>(output, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Map<String, Object> output = saleService.deleteSale(id);

        if (output.containsKey("error")) {
            return new ResponseEntity<Map<String, Object>>(output, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map<String, Object>>(output, HttpStatus.CREATED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> unknownKey(JsonMappingException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", exception.getOriginalMessage().replaceAll("\\(.*?\\)", ""));
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BindException.class)
    protected ResponseEntity<?> handleBindException(
            BindException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler)
            throws IOException {
        Map<String, Object> localResponse = new HashMap<>();
        localResponse.put("error", "error en la entrada JSON");
        return new ResponseEntity<Map<String, Object>>(localResponse, HttpStatus.BAD_REQUEST);
    }
}

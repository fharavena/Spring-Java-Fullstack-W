package com.wherex.appventas.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.wherex.appventas.domain.SaleInputDTO;
import com.wherex.appventas.domain.SaleItemInputDTO;
import com.wherex.appventas.domain.SaleSimpleListDTO;
import com.wherex.appventas.entity.Detail;
import com.wherex.appventas.entity.Product;
import com.wherex.appventas.entity.Sale;
import com.wherex.appventas.repository.ClientRepository;
import com.wherex.appventas.repository.ProductRepository;
import com.wherex.appventas.repository.SaleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SaleService {
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public List<SaleSimpleListDTO> findSales() {
        return saleRepository.findSales();
    }

    @Transactional(readOnly = false)
    public Map<String, Object> saveSale(SaleInputDTO saleInput) {
        Map<String, Object> response = new HashMap<>();

        String error = "";
        Double stock;
        Double total = 0.0;
        Double descuento = 0.0;
        Sale sale = new Sale();
        sale.setItems(new ArrayList<Detail>());
        List<Detail> items = new ArrayList<Detail>();
        String errorJson = "no existe en la entrada JSON";

        if (saleInput.getDescuento() == null) {
            //return "descuento "+errorJson;
            response.put("error", "descuento " + errorJson);
            return response;
        }
        if (saleInput.getCliente() == null) {
//            return "cliente "+errorJson;
            response.put("error", "cliente " + errorJson);
            return response;
        }
        if (saleInput.getItem() == null) {
//            return "Item "+errorJson;
            response.put("error", "Item " + errorJson);
            return response;
        }

        if (saleInput.getDescuento() > 100) {
            response.put("error", "Descuento excede el 100% ");
            return response;
        }

        for (SaleItemInputDTO item : saleInput.getItem()) {
            Detail detail = new Detail();
            Double precio;

            if (item.getProducto() == null) {
//                return "producto "+errorJson;
                response.put("error", "producto " + errorJson);
                return response;
            }
            if (item.getCantidad() == null) {
//                return "cantidad "+errorJson;
                response.put("error", "cantidad " + errorJson);
                return response;
            }

            try {
                Product productTemp = productRepository.getById(item.getProducto());
                stock = productTemp.getCantidad();
                precio = productTemp.getPrecio();
                detail.setProducto(productTemp);
                if (item.getCantidad() <= stock) {
                    detail.setCantidad(item.getCantidad());
                    detail.setSubtotal(item.getCantidad() * precio);
                    productTemp.setCantidad(stock - item.getCantidad());
                    total += item.getCantidad() * precio;
                    detail.setProducto(productTemp);
                    items.add(detail);
                } else {
                    error += "no se puede comprar "
                            + item.getCantidad()
                            + " de producto "
                            + productRepository.getById(item.getProducto()).getNombre()
                            + " solo queda en stock: "
                            + productRepository.getById(item.getProducto()).getCantidad()
                            + "\n";
                }
            } catch (Exception e) {
//                return "producto no encontrado id: " + item.getProducto();
                response.put("error", "producto no encontrado id: " + item.getProducto());
                return response;

            }
        }

        if (!error.equals("")) {
            response.put("error", error);
            return response;
        }


        descuento = ((saleInput.getDescuento() * total) / 100);
        sale.setDescuento(descuento);
        total -= descuento;
        sale.setIva(total * 0.19);
        sale.setTotal(total * 1.19);
        Date date = new Date();
        sale.setFecha(date);
        sale.setItems(items);
        sale.setCliente(clientRepository.getById(saleInput.getCliente()));

        String msg = saleRepository.save(sale).getId().toString();

        response.put("data", saleRepository.save(sale));
        return response;

//        return msg;
    }

    public Map<String, Object> deleteSale(Long id) {
        Map<String, Object> response = new HashMap<>();
        //verificar si id existe
        Sale saleCandidate = saleRepository.getById(id);

        try {
            //loop detalle
            for(Detail detail : saleRepository.getById(id).getItems()){
                //actualizar stock de producto (aumentar)
                detail.getProducto().setCantidad(detail.getProducto().getCantidad()+detail.getCantidad());
                productRepository.save(detail.getProducto());
            }
            //borrar venta
            saleRepository.delete(saleRepository.getById(id));
        } catch (EntityNotFoundException e) {
            response.put("error", "La id no existe");
            return response;
        }

        response.put("action", "deteled sale id: "+id);
        return response;
    }
}

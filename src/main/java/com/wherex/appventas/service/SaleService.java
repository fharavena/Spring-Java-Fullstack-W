package com.wherex.appventas.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public String saveSale(SaleInputDTO saleInput) {
        String error = "";
        Double stock;
        Double total = 0.0;
        Double descuento = 0.0;
        Sale sale = new Sale();
        sale.setItems(new ArrayList<Detail>());
        List<Detail> items = new ArrayList<Detail>();
        String errorJson="no existe en la entrada JSON";

        if(saleInput.getDescuento()== null){
            return "descuento "+errorJson;
        }
        if(saleInput.getCliente()== null){
            return "cliente "+errorJson;
        }
        if(saleInput.getItem()== null){
            return "Item "+errorJson;
        }

        if (saleInput.getDescuento() > 100) {
            return "Descuento excede el 100% ";
        }

        for (SaleItemInputDTO item : saleInput.getItem()) {
            Detail detail = new Detail();
            Double precio;

            if(item.getProducto()== null){
                return "producto "+errorJson;
            }
            if(item.getCantidad()== null){
                return "cantidad "+errorJson;
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
                    error += "no se puede comprar esa cantidad de producto "
                            + productRepository.getById(item.getProducto()).getNombre()
                            + " no queda stock suficente, "
                            + "solo quedan "
                            + productRepository.getById(item.getProducto()).getCantidad()
                            + "\n";
                }
            } catch (Exception e) {
                return "producto no encontrado id: " + item.getProducto();
            }
        }

        if (!error.equals("")) {
            return error;
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

        return msg;
    }


}

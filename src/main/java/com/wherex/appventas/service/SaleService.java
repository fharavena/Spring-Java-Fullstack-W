package com.wherex.appventas.service;

import com.wherex.appventas.domain.SaleInputDTO;
import com.wherex.appventas.domain.SaleItemInputDTO;
import com.wherex.appventas.domain.SaleSimpleListDTO;
import com.wherex.appventas.entity.Detail;
import com.wherex.appventas.entity.Product;
import com.wherex.appventas.entity.Sale;
import com.wherex.appventas.repository.ClientRepository;
import com.wherex.appventas.repository.DetailRepository;
import com.wherex.appventas.repository.ProductRepository;
import com.wherex.appventas.repository.SaleRepository;

import com.wherex.appventas.service.IService.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class SaleService implements ISaleService {
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DetailRepository detailRepository;

    @Transactional(readOnly = true)
    public List<SaleSimpleListDTO> findSales() {
        return saleRepository.findSales();
    }

    @Override
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
            response.put("error", "descuento " + errorJson);
            return response;
        }
        if (saleInput.getCliente() == null) {
            response.put("error", "cliente " + errorJson);
            return response;
        }
        if (saleInput.getItem() == null) {
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
                response.put("error", "producto " + errorJson);
                return response;
            }
            if (item.getCantidad() == null) {
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
//        total -= descuento;
        sale.setIva((total - descuento) * 0.19);
        sale.setTotal(total);
        Date date = new Date();
        sale.setFecha(date);
        sale.setItems(items);
        sale.setCliente(clientRepository.getById(saleInput.getCliente()));

        String msg = saleRepository.save(sale).getId().toString();

        response.put("data", saleRepository.save(sale));
        return response;

    }

    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> editSale(Sale saleInput) {
        Map<String, Object> response = new HashMap<>();
        Sale saleOriginal = saleRepository.getById(saleInput.getId());

        Double total = 0.0;
        Double subtotal = 0.0;
        Double descuento = 0.0;
        Double iva = 0.0;

        for (Detail item : saleInput.getItems()) {
            subtotal = 0.0;
            Detail ItemOriginal = detailRepository.getById(item.getId());
            Product productOriginal = productRepository.getById(item.getProducto().getId());

            //validar cantidad de producto no sobrepase stock
            if (item.getCantidad() > ItemOriginal.getCantidad() + productOriginal.getCantidad()) {
                response.put("error", "la cantidad excede el stock existente ");
                return response;
            }

            subtotal = item.getCantidad() * productOriginal.getPrecio();


            //validar subtotal
            if (Math.abs(item.getSubtotal() - subtotal) > 0.1) {
                response.put("error", "el subtotal del item " + item.getProducto().getNombre() + " no es congruente entrada: " + item.getSubtotal() + ", esperado: " + subtotal);
                return response;
            }

            total += subtotal;
        }

        //validar total
        if (Math.abs(saleInput.getTotal() - total) > 0.1) {
            response.put("error", "el total no es congruente entrada: " + total + ", esperado: " + saleInput.getTotal());
            return response;
        }

        //validar descuento
        if (saleInput.getDescuento() > 100) {
            response.put("error", "El descuento sobrepasa el 100%");
            return response;
        }

        descuento = (total * saleInput.getDescuento()) / 100;
        iva = (total - descuento) * 0.19;

        //validar iva
        if (Math.abs(saleInput.getIva()- iva) > 0.01) {
            response.put("error", "el iva no es congruente entrada: " + saleInput.getIva() + ", esperado: " + iva);
            return response;
        }

        try {
            saleRepository.save(saleInput);
        } catch (
                Exception e) {
            response.put("error", e);
            return response;
        }

        response.put("data", "Success");
        return response;
    }

    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> deleteSale(Long id) {
        Map<String, Object> response = new HashMap<>();
        //verificar si id existe
        Sale saleCandidate = saleRepository.getById(id);

        try {
            //loop detalle
            for (Detail detail : saleRepository.getById(id).getItems()) {
                //actualizar stock de producto (aumentar)
                detail.getProducto().setCantidad(detail.getProducto().getCantidad() + detail.getCantidad());
                productRepository.save(detail.getProducto());
            }
            //borrar venta
            saleRepository.delete(saleRepository.getById(id));
        } catch (EntityNotFoundException e) {
            response.put("error", "La id no existe");
            return response;
        }

        response.put("action", "deteled sale id: " + id);
        return response;
    }


}

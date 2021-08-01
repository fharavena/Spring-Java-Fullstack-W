package com.wherex.appventas.service.IService;

import com.wherex.appventas.domain.SaleInputDTO;
import com.wherex.appventas.domain.SaleInputEditDTO;
import com.wherex.appventas.domain.SaleSimpleListDTO;
import com.wherex.appventas.entity.Sale;

import java.util.List;
import java.util.Map;

public interface ISaleService {
    public List<SaleSimpleListDTO> findSales();
    public Map<String, Object> saveSale(SaleInputDTO saleInput);
    public Map<String, Object> editSaleBorrame(SaleInputEditDTO saleInput);
    public Map<String, Object> editSale(Sale saleInput);
    public Map<String, Object> deleteSale(Long id);
}

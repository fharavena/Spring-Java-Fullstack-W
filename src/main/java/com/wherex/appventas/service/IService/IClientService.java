package com.wherex.appventas.service.IService;

import com.wherex.appventas.entity.Client;

import java.util.List;

public interface IClientService {
    List<Client> findByEstado(Integer state);
}

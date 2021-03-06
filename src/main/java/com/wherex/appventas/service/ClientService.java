package com.wherex.appventas.service;

import com.wherex.appventas.repository.ClientRepository;
import com.wherex.appventas.entity.Client;
import com.wherex.appventas.service.IService.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService implements IClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Client> findByEstado(Integer state) {
        return (List<Client>) clientRepository.findByEstado(state);
    }
}

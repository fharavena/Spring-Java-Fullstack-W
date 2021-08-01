package com.wherex.appventas.controllers;

import com.wherex.appventas.entity.Client;
import com.wherex.appventas.service.IService.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ClientController {
    @Autowired
    private IClientService clientService;

    @GetMapping(value="")
    public List<Client> list(){
        return clientService.findByEstado(1);
    }
}

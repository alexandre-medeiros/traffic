package com.alexmart.traffic.api.controller;

import com.alexmart.traffic.api.domain.model.Owner;
import com.alexmart.traffic.api.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @GetMapping("/owners")
    public List<Owner> getAllOwner() {
        return ownerService.findAll();
    }
}

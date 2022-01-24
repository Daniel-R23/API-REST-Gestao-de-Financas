package com.financas.gestao.controller;

import com.financas.gestao.dto.ResumoDTO;
import com.financas.gestao.service.ResumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resumo")
public class ResumoController {

    @Autowired
    ResumoService resumoService;

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<ResumoDTO> resumo(@PathVariable Long ano, @PathVariable Long mes){
        return resumoService.resumir(ano,mes);
    }
}

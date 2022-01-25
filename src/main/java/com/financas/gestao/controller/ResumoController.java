package com.financas.gestao.controller;

import com.financas.gestao.dto.ResumoDTO;
import com.financas.gestao.exception.DespesaNotFoundException;
import com.financas.gestao.exception.ReceitaNotFoundException;
import com.financas.gestao.service.ResumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resumo")
public class ResumoController {

    @Autowired
    private ResumoService resumoService;

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<ResumoDTO> resumo(@PathVariable Long ano, @PathVariable Long mes) throws ReceitaNotFoundException, DespesaNotFoundException {
        ResumoDTO resumoDTO = resumoService.resumir(ano, mes);
        return ResponseEntity.ok(resumoDTO);
    }
}

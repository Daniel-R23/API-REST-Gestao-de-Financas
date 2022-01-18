package com.financas.gestao.controller;

import com.financas.gestao.dto.ReceitaDTO;
import com.financas.gestao.exception.ReceitaJaCadastradaException;
import com.financas.gestao.repository.ReceitaRepository;
import com.financas.gestao.service.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {

    @Autowired
    ReceitaRepository repository;
    @Autowired
    ReceitaService receitaService;


    @PostMapping
    public ResponseEntity<ReceitaDTO> cadastrar(@RequestBody @Valid ReceitaDTO receitaDTO, UriComponentsBuilder uriBuilder) throws ReceitaJaCadastradaException {
        return receitaService.cadastrar(receitaDTO, uriBuilder);
    }

}

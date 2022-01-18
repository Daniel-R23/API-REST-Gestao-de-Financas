package com.financas.gestao.controller;

import com.financas.gestao.dto.ReceitaDTO;
import com.financas.gestao.exception.ReceitaJaCadastradaException;
import com.financas.gestao.repository.ReceitaRepository;
import com.financas.gestao.service.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {

    @Autowired
    ReceitaRepository repository;
    @Autowired
    ReceitaService receitaService;


    @PostMapping
    @Transactional
    public ResponseEntity<ReceitaDTO> cadastrar(@RequestBody @Valid ReceitaDTO receitaDTO, UriComponentsBuilder uriBuilder) throws ReceitaJaCadastradaException {
        return receitaService.cadastrar(receitaDTO, uriBuilder);
    }

    @GetMapping
    public List<ReceitaDTO> listar(){
        return receitaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaDTO> detalhar(@PathVariable Long id){
        return receitaService.detalhar(id);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ReceitaDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ReceitaDTO receitaDTO){
        return receitaService.atualizar(id, receitaDTO);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable Long id){
        return receitaService.deletar(id);
    }

}

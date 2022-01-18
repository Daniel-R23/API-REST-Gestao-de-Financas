package com.financas.gestao.controller;

import com.financas.gestao.dto.DespesaDTO;
import com.financas.gestao.exception.DespesaJaCadastradaException;
import com.financas.gestao.repository.ReceitaRepository;
import com.financas.gestao.service.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/despesas")
public class DespesaController {

    @Autowired
    ReceitaRepository repository;
    @Autowired
    DespesaService despesaService;


    @PostMapping
    @Transactional
    public ResponseEntity<DespesaDTO> cadastrar(@RequestBody @Valid DespesaDTO despesaDTO, UriComponentsBuilder uriBuilder) throws DespesaJaCadastradaException {
        return despesaService.cadastrar(despesaDTO, uriBuilder);
    }

    @GetMapping
    public List<DespesaDTO> listar(){
        return despesaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaDTO> detalhar(@PathVariable Long id){
        return despesaService.detalhar(id);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DespesaDTO> atualizar(@PathVariable Long id, @RequestBody @Valid DespesaDTO despesaDTO){
        return despesaService.atualizar(id, despesaDTO);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable Long id){
        return despesaService.deletar(id);
    }

}

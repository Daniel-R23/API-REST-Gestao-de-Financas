package com.financas.gestao.controller;

import com.financas.gestao.dto.DespesaDTO;
import com.financas.gestao.dto.DespesaForm;
import com.financas.gestao.dto.ReceitaDTO;
import com.financas.gestao.exception.DespesaJaCadastradaException;
import com.financas.gestao.repository.DespesaRepository;
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
    DespesaService despesaService;


    @PostMapping
    @Transactional
    public ResponseEntity<DespesaDTO> cadastrar(@RequestBody @Valid DespesaForm despesaForm, UriComponentsBuilder uriBuilder) throws DespesaJaCadastradaException {
        return despesaService.cadastrar(despesaForm, uriBuilder);
    }

    @GetMapping
    public List<DespesaDTO> listar(@RequestParam(required = false) String descricao){
        return despesaService.listar(descricao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaForm> detalhar(@PathVariable Long id){
        return despesaService.detalhar(id);
    }

    @GetMapping("/{ano}/{mes}")
    public List<DespesaDTO> listarPorMes(@PathVariable Long ano, @PathVariable Long mes){
        return  despesaService.listarPorMes(ano,mes);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DespesaForm> atualizar(@PathVariable Long id, @RequestBody @Valid DespesaForm despesaForm){
        return despesaService.atualizar(id, despesaForm);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable Long id){
        return despesaService.deletar(id);
    }

}

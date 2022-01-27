package com.financas.gestao.controller;

import com.financas.gestao.dto.ReceitaDTO;
import com.financas.gestao.exception.DespesaNotFoundException;
import com.financas.gestao.exception.ReceitaJaCadastradaException;
import com.financas.gestao.exception.ReceitaNotFoundException;
import com.financas.gestao.model.Receita;
import com.financas.gestao.service.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {

    @Autowired
    private ReceitaService receitaService;


    @PostMapping
    public ResponseEntity<ReceitaDTO> cadastrar(@RequestBody @Valid ReceitaDTO receitaDTO, UriComponentsBuilder uriBuilder) throws ReceitaJaCadastradaException {
        Receita receita = receitaService.cadastrar(receitaDTO);
        URI uri  = uriBuilder.path("/receitas/{id}").buildAndExpand(receita.getId()).toUri();
        return ResponseEntity.created(uri).body(new ReceitaDTO(receita));
    }

    @GetMapping
    public List<ReceitaDTO> listar(@RequestParam(required = false) String descricao){
        return receitaService.listar(descricao);
    }

    @GetMapping("/{id}")
    public ReceitaDTO detalhar(@PathVariable Long id) throws ReceitaNotFoundException {
        return receitaService.detalhar(id);
    }

    @GetMapping("/{ano}/{mes}")
    public List<ReceitaDTO> listarPorMes(@PathVariable Long ano, @PathVariable Long mes) throws ReceitaNotFoundException {
        return  receitaService.listarPorMes(ano,mes);
    }

    @PutMapping("/{id}")
    public ReceitaDTO atualizar(@PathVariable Long id, @RequestBody @Valid ReceitaDTO receitaDTO) throws ReceitaNotFoundException {
        Receita receita = receitaService.atualizar(id, receitaDTO);
        return new ReceitaDTO(receita);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) throws DespesaNotFoundException {
        receitaService.deletar(id);
    }

}

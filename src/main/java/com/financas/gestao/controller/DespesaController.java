package com.financas.gestao.controller;

import com.financas.gestao.dto.DespesaDTO;
import com.financas.gestao.dto.DespesaDetalhes;
import com.financas.gestao.exception.DespesaJaCadastradaException;
import com.financas.gestao.exception.DespesaNotFoundException;
import com.financas.gestao.model.Despesa;
import com.financas.gestao.service.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/despesas")
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

    @PostMapping
    public ResponseEntity<DespesaDTO> cadastrar(@RequestBody @Valid DespesaDetalhes despesaDetalhes, UriComponentsBuilder uriBuilder) throws DespesaJaCadastradaException {
        Despesa despesa = despesaService.cadastrar(despesaDetalhes);
        URI uri  = uriBuilder.path("/despesas/{id}").buildAndExpand(despesa.getId()).toUri();
        return ResponseEntity.created(uri).body(new DespesaDTO(despesa));
    }

    @GetMapping
    public List<DespesaDTO> listar(@RequestParam(required = false) String descricao){
        return despesaService.listar(descricao);
    }

    @GetMapping("/{id}")
    public DespesaDetalhes detalhar(@PathVariable Long id) throws DespesaNotFoundException {
        return despesaService.detalhar(id);
    }

    @GetMapping("/{ano}/{mes}")
    public List<DespesaDTO> listarPorMes(@PathVariable Long ano, @PathVariable Long mes) throws DespesaNotFoundException {
        return DespesaDTO.converterLista(despesaService.listarPorMes(ano,mes));
    }

    @PutMapping("/{id}")
    public DespesaDetalhes atualizar(@PathVariable Long id, @RequestBody @Valid DespesaDetalhes despesaDetalhes) throws DespesaNotFoundException {
        Despesa despesaAtualizada = despesaService.atualizar(id, despesaDetalhes);
        return new DespesaDetalhes(despesaAtualizada);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) throws DespesaNotFoundException {
        despesaService.deletar(id);
    }

}

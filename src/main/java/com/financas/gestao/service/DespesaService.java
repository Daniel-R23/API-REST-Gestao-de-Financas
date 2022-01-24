package com.financas.gestao.service;

import com.financas.gestao.dto.DespesaDTO;
import com.financas.gestao.dto.DespesaForm;
import com.financas.gestao.exception.DespesaJaCadastradaException;
import com.financas.gestao.model.Despesa;
import com.financas.gestao.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class DespesaService {

    @Autowired
    DespesaRepository repository;

    public ResponseEntity<DespesaDTO> cadastrar(DespesaForm despesaForm, UriComponentsBuilder uriBuilder) throws DespesaJaCadastradaException {
        verificaSeJaExiste(despesaForm);
        Despesa despesa = despesaForm.converter();
        repository.save(despesa);
        URI uri  = uriBuilder.path("/despesas/{id}").buildAndExpand(despesa.getId()).toUri();
        return ResponseEntity.created(uri).body(new DespesaDTO(despesa));
    }

    public List<DespesaDTO> listar() {
        List<Despesa> despesas = repository.findAll();
        return DespesaDTO.converterLista(despesas);
    }

    public ResponseEntity<DespesaForm> detalhar(Long id) {
        Optional<Despesa> despesaOptional = repository.findById(id);
        return despesaOptional.map(despesa -> ResponseEntity.ok(new DespesaForm(despesa))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<DespesaForm> atualizar(Long id, DespesaForm despesaForm) {
        Optional<Despesa> despesaOptional = repository.findById(id);
        if(despesaOptional.isPresent()){
            Despesa despesa = despesaForm.atualizar(id, repository);
            return ResponseEntity.ok(new DespesaForm(despesa));
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> deletar(Long id) {
        Optional<Despesa> despesaOptional = repository.findById(id);
        if(despesaOptional.isPresent()){
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    private void verificaSeJaExiste(DespesaForm despesa) throws DespesaJaCadastradaException {
        Optional<Despesa> despesaEncontrada = repository.findByDescricaoAndData(despesa.getDescricao(), despesa.getData());
        if(despesaEncontrada.isPresent()) {
            throw new DespesaJaCadastradaException();
        }
    }
}

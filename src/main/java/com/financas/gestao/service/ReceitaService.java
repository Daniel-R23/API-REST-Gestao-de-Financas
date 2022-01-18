package com.financas.gestao.service;

import com.financas.gestao.dto.ReceitaDTO;
import com.financas.gestao.exception.ReceitaJaCadastradaException;
import com.financas.gestao.model.Receita;
import com.financas.gestao.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@Service
public class ReceitaService {

    @Autowired
    ReceitaRepository repository;

    public ResponseEntity<ReceitaDTO> cadastrar(ReceitaDTO receitaDTO, UriComponentsBuilder uriBuilder) throws ReceitaJaCadastradaException {
        verificaSeJaExiste(receitaDTO);
        Receita receita = receitaDTO.toReceita();
        repository.save(receita);
        URI uri  = uriBuilder.path("/receitas/{id}").buildAndExpand(receita.getId()).toUri();
        return ResponseEntity.created(uri).body(new ReceitaDTO(receita));
    }

    public List<ReceitaDTO> listar() {
        List<Receita> receitas = repository.findAll();
        return ReceitaDTO.converter(receitas);
    }

    public ResponseEntity<ReceitaDTO> detalhar(Long id) {
        Optional<Receita> receitaOptional = repository.findById(id);
        return receitaOptional.map(receita -> ResponseEntity.ok(new ReceitaDTO(receita))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private void verificaSeJaExiste(ReceitaDTO receita) throws ReceitaJaCadastradaException {
        Optional<Receita> receitaEncontrada = repository.findByDescricaoAndData(receita.getDescricao(), receita.getData());
        if(receitaEncontrada.isPresent()) {
            throw new ReceitaJaCadastradaException();
        }
    }
}

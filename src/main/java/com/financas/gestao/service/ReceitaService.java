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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReceitaService {

    @Autowired
    ReceitaRepository repository;

    public ResponseEntity<ReceitaDTO> cadastrar(ReceitaDTO receitaDTO, UriComponentsBuilder uriBuilder) throws ReceitaJaCadastradaException {
        verificaSeJaExiste(receitaDTO);
        Receita receita = receitaDTO.converter();
        repository.save(receita);
        URI uri  = uriBuilder.path("/receitas/{id}").buildAndExpand(receita.getId()).toUri();
        return ResponseEntity.created(uri).body(new ReceitaDTO(receita));
    }

    public List<ReceitaDTO> listar(String descricao) {
        if(descricao == null){
            List<Receita> receitas = repository.findAll();
            return ReceitaDTO.converterLista(receitas);
        }else{
            List<Receita> receitas = repository.findByDescricaoContainingIgnoreCase(descricao);
            return ReceitaDTO.converterLista(receitas);
        }
    }

    public ResponseEntity<ReceitaDTO> detalhar(Long id) {
        Optional<Receita> receitaOptional = repository.findById(id);
        return receitaOptional.map(receita -> ResponseEntity.ok(new ReceitaDTO(receita))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<ReceitaDTO> atualizar(Long id, ReceitaDTO receitaDTO) {
        Optional<Receita> receitaOptional = repository.findById(id);
        if(receitaOptional.isPresent()){
            Receita receita = receitaDTO.atualizar(id, repository);
            return ResponseEntity.ok(new ReceitaDTO(receita));
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> deletar(Long id) {
        Optional<Receita> receitaOptional = repository.findById(id);
        if(receitaOptional.isPresent()){
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    public List<ReceitaDTO> listarPorMes(Long ano, Long mes) {
        List<Receita> receitasPorAno = repository.findByDataContaining(ano);
        List<Receita> receitasPorAnoEMes = new ArrayList<>();
        receitasPorAno.forEach(receita -> {
            if(receita.getData().getMonthValue() == mes){
                receitasPorAnoEMes.add(receita);
            }
        });

        return ReceitaDTO.converterLista(receitasPorAnoEMes);
    }

    private void verificaSeJaExiste(ReceitaDTO receita) throws ReceitaJaCadastradaException {
        Optional<Receita> receitaEncontrada = repository.findByDescricaoAndData(receita.getDescricao(), receita.getData());
        if(receitaEncontrada.isPresent()) {
            throw new ReceitaJaCadastradaException();
        }
    }
}

package com.financas.gestao.service;

import com.financas.gestao.dto.ReceitaDTO;
import com.financas.gestao.exception.ReceitaJaCadastradaException;
import com.financas.gestao.exception.ReceitaNotFoundException;
import com.financas.gestao.model.Receita;
import com.financas.gestao.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository repository;

    @Transactional
    public Receita cadastrar(ReceitaDTO receitaDTO) throws ReceitaJaCadastradaException {
        verificaSeJaExiste(receitaDTO);
        Receita receita = receitaDTO.converter();
        repository.save(receita);
        return receita;
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

    public ReceitaDTO detalhar(Long id) throws ReceitaNotFoundException {
        Optional<Receita> receitaOptional = repository.findById(id);
        if(receitaOptional.isPresent()){
            return new ReceitaDTO(receitaOptional.get());
        }
        throw new ReceitaNotFoundException(id);
    }

    public List<ReceitaDTO> listarPorMes(Long ano, Long mes) throws ReceitaNotFoundException {
        List<Receita> receitasPorAno = repository.findByDataContaining(ano);
        List<Receita> receitasPorAnoEMes = new ArrayList<>();
        receitasPorAno.forEach(receita -> {
            if(receita.getData().getMonthValue() == mes){
                receitasPorAnoEMes.add(receita);
            }
        });
        if(!receitasPorAnoEMes.isEmpty()){
            return ReceitaDTO.converterLista(receitasPorAnoEMes);
        }
        throw new ReceitaNotFoundException();
    }

    @Transactional
    public Receita atualizar(Long id, ReceitaDTO receitaDTO) throws ReceitaNotFoundException {
        Optional<Receita> receitaOptional = repository.findById(id);
        if(receitaOptional.isPresent()){
            return receitaDTO.atualizar(id, repository);
        }
        throw new ReceitaNotFoundException(id);
    }

    @Transactional
    public void deletar(Long id) throws ReceitaNotFoundException {
        Optional<Receita> receitaOptional = repository.findById(id);
        if(receitaOptional.isPresent()){
            repository.deleteById(id);
        }else{
            throw new ReceitaNotFoundException(id);
        }
    }

    private void verificaSeJaExiste(ReceitaDTO receita) throws ReceitaJaCadastradaException {
        List<Receita> receitasComMesmoAno = repository.findByDataContaining((long) receita.getData().getYear());
        List<Receita> receitasComMesmoAnoEMesEDescricao = new ArrayList<>();
        if(!receitasComMesmoAno.isEmpty()){
            receitasComMesmoAno.forEach(receitaComMesmoAno ->{
                if(receitaComMesmoAno.getData().getMonthValue() == receita.getData().getMonthValue() && receitaComMesmoAno.getDescricao().equals(receita.getDescricao())){
                    receitasComMesmoAnoEMesEDescricao.add(receitaComMesmoAno);
                }
            });
            if(!receitasComMesmoAnoEMesEDescricao.isEmpty()){
                throw new ReceitaJaCadastradaException();
            }
        }
    }
}

package com.financas.gestao.service;

import com.financas.gestao.dto.DespesaDTO;
import com.financas.gestao.dto.DespesaDetalhes;
import com.financas.gestao.exception.DespesaJaCadastradaException;
import com.financas.gestao.exception.DespesaNotFoundException;
import com.financas.gestao.model.Despesa;
import com.financas.gestao.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository repository;

    @Transactional
    public Despesa cadastrar(DespesaDetalhes despesaDetalhes) throws DespesaJaCadastradaException {
        verificaSeJaExiste(despesaDetalhes);
        Despesa despesa = despesaDetalhes.converter();
        repository.save(despesa);
        return despesa;
    }

    public List<DespesaDTO> listar(String descricao) {
        if(descricao == null) {
            List<Despesa> despesas = repository.findAll();
            return DespesaDTO.converterLista(despesas);
        }else{
            List<Despesa> despesas = repository.findByDescricaoContainingIgnoreCase(descricao);
            return DespesaDTO.converterLista(despesas);
        }
    }

    public DespesaDetalhes detalhar(Long id) throws DespesaNotFoundException {
        Optional<Despesa> despesaOptional = repository.findById(id);
        if(despesaOptional.isPresent()){
            return new DespesaDetalhes(despesaOptional.get());
        }
        throw new DespesaNotFoundException(id);
    }

    public List<Despesa> listarPorMes(Long ano, Long mes) throws DespesaNotFoundException {
        List<Despesa> despesasPorAno = repository.findByDataContaining(ano);
        List<Despesa> despesasPorAnoEMes = new ArrayList<>();
        despesasPorAno.forEach(despesa -> {
            if(despesa.getData().getMonthValue() == mes){
                despesasPorAnoEMes.add(despesa);
            }
        });
        if(!despesasPorAnoEMes.isEmpty()){
            return despesasPorAnoEMes;
        }
        throw new DespesaNotFoundException();
    }

    @Transactional
    public DespesaDetalhes atualizar(Long id, DespesaDetalhes despesaDetalhes) throws DespesaNotFoundException {
        Optional<Despesa> despesaOptional = repository.findById(id);
        if(despesaOptional.isPresent()){
            return new DespesaDetalhes(despesaDetalhes.atualizar(id, repository));
        }
        throw new DespesaNotFoundException(id);
    }

    @Transactional
    public void deletar(Long id) throws DespesaNotFoundException {
        Optional<Despesa> despesaOptional = repository.findById(id);
        if(despesaOptional.isPresent()){
            repository.deleteById(id);
        }else{
            throw new DespesaNotFoundException(id);
        }
    }

    private void verificaSeJaExiste(DespesaDetalhes despesa) throws DespesaJaCadastradaException {
        List<Despesa> despesasComMesmoAno = repository.findByDataContaining((long) despesa.getData().getYear());
        List<Despesa> despesasComMesmoAnoMesEDescricao = new ArrayList<>();
        if(!despesasComMesmoAno.isEmpty()){
            despesasComMesmoAno.forEach(despesaMesmoAno -> {
                if(despesaMesmoAno.getData().getMonthValue() == despesa.getData().getMonthValue() && despesaMesmoAno.getDescricao().equals(despesa.getDescricao())){
                    despesasComMesmoAnoMesEDescricao.add(despesaMesmoAno);
                }
            });
            if(!despesasComMesmoAnoMesEDescricao.isEmpty()){
                throw new DespesaJaCadastradaException();
            }
        }

    }
}
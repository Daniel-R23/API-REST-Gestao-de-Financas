package com.financas.gestao.service;

import com.financas.gestao.dto.UsuarioDTO;
import com.financas.gestao.exception.UsuarioJaCadastradoException;
import com.financas.gestao.model.Usuario;
import com.financas.gestao.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Transactional
    public Usuario cadastrar(UsuarioDTO userDTO) throws UsuarioJaCadastradoException {
        verificaSeJaExiste(userDTO);
        Usuario user = userDTO.converter();
        repository.save(user);
        return user;
    }

    private void verificaSeJaExiste(UsuarioDTO user) throws UsuarioJaCadastradoException {
        Optional<Usuario> usuarioEncontrado = repository.findByEmail(user.getEmail());
        if(usuarioEncontrado.isPresent()){
            throw new UsuarioJaCadastradoException();
        }
    }

}

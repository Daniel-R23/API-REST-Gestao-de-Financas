package com.financas.gestao.controller;

import com.financas.gestao.dto.UsuarioDTO;
import com.financas.gestao.exception.UsuarioJaCadastradoException;
import com.financas.gestao.model.Usuario;
import com.financas.gestao.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping
    public Usuario cadastrar(@RequestBody @Valid UsuarioDTO user) throws UsuarioJaCadastradoException {
        return service.cadastrar(user);
    }
}

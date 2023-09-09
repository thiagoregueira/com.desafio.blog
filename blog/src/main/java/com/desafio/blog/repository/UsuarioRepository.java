package com.desafio.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desafio.blog.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}

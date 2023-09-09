package com.desafio.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desafio.blog.model.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

}

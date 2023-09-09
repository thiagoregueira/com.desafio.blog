package com.desafio.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desafio.blog.model.Comentario;
import com.desafio.blog.model.Post;

import jakarta.transaction.Transactional;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findByPostagem(Post postagem);

    @Transactional
    void deleteByPostId(Long postId);
}

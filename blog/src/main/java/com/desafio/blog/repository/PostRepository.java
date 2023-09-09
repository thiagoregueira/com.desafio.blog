package com.desafio.blog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desafio.blog.model.Post;
import com.desafio.blog.model.Usuario;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByAutor(Usuario autor);

    Optional<Post> findByIdPost(Long idPost);
}

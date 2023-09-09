package com.desafio.blog.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.blog.dtos.ComentarioDto;
import com.desafio.blog.model.Comentario;
import com.desafio.blog.model.Post;
import com.desafio.blog.repository.ComentarioRepository;
import com.desafio.blog.repository.PostRepository;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private PostRepository postRepository;

    @PostMapping("/{postId}")
    public ResponseEntity<Comentario> criarComentario(@PathVariable Long postId,
            @RequestBody ComentarioDto comentarioDto) {

        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            Comentario novoComentario = new Comentario();
            novoComentario.setTexto(comentarioDto.getTexto());
            novoComentario.setPost(post);
            return ResponseEntity.ok(comentarioRepository.save(novoComentario));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comentario> updateComentario(@PathVariable Long id,
            @RequestBody ComentarioDto comentarioDto) {
        Optional<Comentario> comentarioOptional = comentarioRepository.findById(id);
        if (comentarioOptional.isPresent()) {
            Comentario comentario = comentarioOptional.get();
            comentario.setTexto(comentarioDto.getTexto());
            return ResponseEntity.ok(comentarioRepository.save(comentario));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComentario(@PathVariable Long id) {
        Optional<Comentario> comentarioOptional = comentarioRepository.findById(id);
        if (comentarioOptional.isPresent()) {
            comentarioRepository.delete(comentarioOptional.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

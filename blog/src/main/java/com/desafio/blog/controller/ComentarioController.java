package com.desafio.blog.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.blog.model.Comentario;

import com.desafio.blog.repository.ComentarioRepository;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @PutMapping("/{id}")
    public ResponseEntity<Comentario> updateComment(@PathVariable Long id, @RequestBody Comentario comentarioDetails) {
        Optional<Comentario> comentarioO = comentarioRepository.findById(id);
        if (comentarioO.isPresent()) {
            Comentario comentario = comentarioO.get();
            comentario.setTexto(comentarioDetails.getTexto());
            return ResponseEntity.ok(comentarioRepository.save(comentario));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComent(@PathVariable Long id) {
        Optional<Comentario> comentarioOptional = comentarioRepository.findById(id);
        if (comentarioOptional.isPresent()) {
            comentarioRepository.delete(comentarioOptional.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

package com.desafio.blog.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.blog.dtos.UsuarioDto;
import com.desafio.blog.model.Usuario;
import com.desafio.blog.repository.PostRepository;
import com.desafio.blog.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PostRepository postRepository;

    @PostMapping("")
    public ResponseEntity<Usuario> createAutor(@RequestBody Usuario novoAutor) {
        Usuario autor = usuarioRepository.save(novoAutor);
        return new ResponseEntity<Usuario>(autor, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<UsuarioDto>> getAllAutores() {
        List<Usuario> autores = usuarioRepository.findAll();
        if (autores.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            List<UsuarioDto> autoresDto = new ArrayList<>();
            for (Usuario autor : autores) {
                UsuarioDto autorDto = new UsuarioDto();
                autorDto.setNome(autor.getNome());
                autorDto.setSobrenome(autor.getSobrenome());
                autorDto.setEmail(autor.getEmail());
                autorDto.setSenha(autor.getSenha());
                autorDto.setPosts(postRepository.findByAutor(autor));
                autoresDto.add(autorDto);
            }
            return new ResponseEntity<>(autoresDto, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> getAutor(@PathVariable Long id) {
        Optional<Usuario> autorOptional = usuarioRepository.findById(id);
        if (autorOptional.isPresent()) {
            Usuario autor = autorOptional.get();
            UsuarioDto autorDto = new UsuarioDto();
            autorDto.setNome(autor.getNome());
            autorDto.setSobrenome(autor.getSobrenome());
            autorDto.setEmail(autor.getEmail());
            autorDto.setSenha(autor.getSenha());
            autorDto.setPosts(postRepository.findByAutor(autor));
            return new ResponseEntity<>(autorDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateAutor(@PathVariable Long id, @RequestBody Usuario autorAtualizado) {
        Optional<Usuario> autorOptional = usuarioRepository.findById(id);
        if (autorOptional.isPresent()) {
            Usuario autor = autorOptional.get();
            autor.setNome(autorAtualizado.getNome());
            autor.setSobrenome(autorAtualizado.getSobrenome());
            autor.setEmail(autorAtualizado.getEmail());
            autor.setSenha(autorAtualizado.getSenha());
            usuarioRepository.save(autor);
            return new ResponseEntity<Usuario>(autor, HttpStatus.OK);
        } else {
            return new ResponseEntity<Usuario>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutor(@PathVariable Long id) {
        Optional<Usuario> autorOptional = usuarioRepository.findById(id);
        if (autorOptional.isPresent()) {
            usuarioRepository.delete(autorOptional.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

package com.desafio.blog.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.desafio.blog.dtos.PostDto;
import com.desafio.blog.model.Comentario;
import com.desafio.blog.model.Post;
import com.desafio.blog.repository.ComentarioRepository;
import com.desafio.blog.repository.PostRepository;
import com.desafio.blog.repository.UsuarioRepository;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @GetMapping("")
    public ResponseEntity<List<PostDto>> getAllPostagens() {
        List<Post> postagens = postRepository.findAll();
        if (postagens.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            List<PostDto> postagensDto = new ArrayList<>();
            for (Post postagem : postagens) {
                PostDto postagemDto = new PostDto();
                postagemDto.setTitulo(postagem.getTitulo());
                postagemDto.setConteudo(postagem.getConteudo());
                postagemDto.setComentarios(comentarioRepository.findByPost(postagem));
                postagemDto.setAutor(usuarioRepository.findById(postagem.getAutor().getIdUsuario()).orElse(null));
                postagensDto.add(postagemDto);
            }
            return new ResponseEntity<>(postagensDto, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostagem(@PathVariable Long id) {
        Optional<Post> postagemOptional = postRepository.findById(id);
        if (postagemOptional.isPresent()) {
            Post postagem = postagemOptional.get();
            PostDto postagemDto = new PostDto();
            postagemDto.setTitulo(postagem.getTitulo());
            postagemDto.setConteudo(postagem.getConteudo());
            postagemDto.setComentarios(comentarioRepository.findByPost(postagem));
            postagemDto.setAutor(usuarioRepository.findById(postagem.getAutor().getIdUsuario()).orElse(null));
            return new ResponseEntity<>(postagemDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post savedPost = postRepository.save(post);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post postDetails) {
        Optional<Post> postO = postRepository.findById(id);
        if (postO.isPresent()) {
            Post post = postO.get();
            post.setTitulo(postDetails.getTitulo());
            post.setConteudo(postDetails.getConteudo());

            return new ResponseEntity<>(postRepository.save(post), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deletePost(@PathVariable Long id) {

        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            postRepository.delete(post.get());
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comentario> addCommentToPost(@PathVariable Long id, @RequestBody Comentario comentario) {
        Optional<Post> postO = postRepository.findById(id);
        if (postO.isPresent()) {
            Post post = postO.get();
            comentario.setPost(post);
            Comentario savedComentario = comentarioRepository.save(comentario);
            return new ResponseEntity<>(savedComentario, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}

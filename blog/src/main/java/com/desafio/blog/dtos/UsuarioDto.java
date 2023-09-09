package com.desafio.blog.dtos;

import java.util.List;

import com.desafio.blog.model.Post;

import lombok.Data;

@Data
public class UsuarioDto {

    private String nome;
    private String sobrenome;
    private String email;
    private String senha;

    private List<Post> posts;

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}

package com.ipvc.bll.dto;

public record RegisterRequest(String email, String password, String nome, Integer tipoUtilizadorId) {}

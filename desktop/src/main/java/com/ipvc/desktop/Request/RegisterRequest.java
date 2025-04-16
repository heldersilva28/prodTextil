package com.ipvc.desktop.Request;

public record RegisterRequest(String email, String password, String nome, Integer tipoUtilizadorId) {}

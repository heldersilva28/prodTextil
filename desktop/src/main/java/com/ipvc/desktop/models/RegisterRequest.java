package com.ipvc.desktop.models;

public record RegisterRequest(String email, String password, String nome, Integer tipoUtilizadorId) {}

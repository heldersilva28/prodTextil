package com.ipvc.bll.controllers;

import com.ipvc.bll.dto.AuthResponse;
import com.ipvc.bll.dto.LoginRequest;
import com.ipvc.bll.dto.RegisterRequest;
import com.ipvc.bll.models.Utilizador;
import com.ipvc.bll.models.TiposUtilizador;
import com.ipvc.bll.repos.TipoUtilizadorRepo;
import com.ipvc.bll.repos.UtilizadorRepo;
import com.ipvc.bll.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UtilizadorRepo userRepo;
    private final TipoUtilizadorRepo tipoRepo;

    public AuthController(AuthService authService, UtilizadorRepo userRepo, TipoUtilizadorRepo tipoRepo) {
        this.authService = authService;
        this.userRepo = userRepo;
        this.tipoRepo = tipoRepo;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        if (userRepo.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().body(new AuthResponse("Email já existe", false));
        }

        boolean primeirosRegisto = userRepo.count() == 0;

        // Só permitir criar admin no 1º registo
        if (!primeirosRegisto && request.tipoUtilizadorId() == 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new AuthResponse("Só é possível criar administradores na primeira utilização.", false));
        }

        Utilizador user = new Utilizador();
        user.setUsername(request.nome());
        user.setEmail(request.email());
        user.setHash(authService.hashPassword(request.password()));

        TiposUtilizador tipo = new TiposUtilizador();
        tipo.setId(request.tipoUtilizadorId());
        user.setTipoUtilizador(tipo);

        userRepo.save(user);
        return ResponseEntity.ok(new AuthResponse("Registo feito com sucesso", true));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Optional<Utilizador> user = userRepo.findByEmail(request.email());
        if (user.isPresent() && authService.verifyPassword(request.password(), user.get().getHash())) {
            return ResponseEntity.ok(new AuthResponse("Login bem-sucedido", true));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Credenciais inválidas", false));
        }
    }

    @GetMapping("/existem-utilizadores")
    public boolean existemUtilizadores() {
        return userRepo.count() > 0;
    }

}

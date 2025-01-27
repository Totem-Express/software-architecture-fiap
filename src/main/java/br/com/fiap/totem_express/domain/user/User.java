package br.com.fiap.totem_express.domain.user;

import br.com.fiap.totem_express.shared.invariant.Invariant;

import java.time.LocalDateTime;

import static br.com.fiap.totem_express.shared.invariant.Rule.*;

public class User {

    private String name;
    private String email;
    private CPF cpf;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Role role;
    private Long id;

    public User(String name, String email, String cpf, Role role) {
        Invariant.of(name, notBlank("User name must be not blank"));
        Invariant.of(email, notBlank("User email must be not blank"));
        Invariant.of(cpf, notBlank("User cpf must be not blank"), validCPF("User cpf must be a valid document"));
        Invariant.of(role, notNull("User role must be not null"));

        this.name = name;
        this.email = email;
        this.cpf = new CPF(cpf);
        this.role = role;
    }

    public User(String name, String email, String cpf) {
        this(name, email, cpf, Role.USER);
    }

    public User(Long id, String name, String email, String cpf, LocalDateTime createdAt, Role role) {
        this(name, email, cpf, role);
        Invariant.of(id, notNull("User id must be not null"));
        Invariant.of(createdAt, notNull("User created at must be not null"));

        this.id = id;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public CPF getDocument(){
        return cpf;
    }

    public String getCpf() {
        return cpf.formatted();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Role getRole() {
        return role;
    }

}

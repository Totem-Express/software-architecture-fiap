package br.com.fiap.totem_express.infrastructure.user;

import java.time.LocalDateTime;

import br.com.fiap.totem_express.domain.user.Role;
import br.com.fiap.totem_express.domain.user.User;
import jakarta.persistence.*;

import static jakarta.persistence.EnumType.STRING;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

//TODO: teste
@Entity(name = "user")
@Table(indexes = {
        @Index(columnList = "cpf", name="idx_user_cpf")
})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String cpf;

    @Enumerated(STRING)
    @NotNull
    private Role role;

    @Column(name = "created_at")
    @PastOrPresent
    private LocalDateTime createdAt;

    @Deprecated
    public UserEntity() {
    }

    public UserEntity(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.cpf = user.getCpf();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
    }

    public User toDomain() {
        return new User(id, name, email, cpf, createdAt, role);
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

    public String getCpf() {
        return cpf;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}

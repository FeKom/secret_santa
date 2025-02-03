package com.github.fekom.secret_santa.model;


import com.github.fekom.secret_santa.dtos.LoginRequest;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserModel implements Serializable {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Column(name = "user_id",
            columnDefinition = "uuid",
            updatable = false,
            nullable = false)
    private UUID userId;

    private String name;

    private String email;

    private String password;

    @ManyToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<GroupModel> groups;

    @ManyToMany(cascade = CascadeType.ALL, fetch =  FetchType.EAGER)
    @JoinTable(
            name = "tb_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUserId() { return userId; }

    public void setUserId(UUID userId) { this.userId = userId; }

    public List<Role> getRoles() { return roles; }

    public void setRoles(List<Role> roles) { this.roles = roles; }

    public List<GroupModel> getGroups() { return groups; }

    public void setGroups(List<GroupModel> groups) { this.groups = groups; }

    public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(loginRequest.password(), this.password);
    }

}

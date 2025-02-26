package com.github.fekom.secret_santa.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "groups")
public class GroupEntity {

    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_users_groups",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")

    )
    private List<UserEntity> user;

    @OneToMany(mappedBy = "groups")
    private Set<DrawEntity> draws;

    private String name;

    private String description;

    private String preferences;

    @CreationTimestamp
    private Instant creationTimeStamp;


    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Instant getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public void setCreationTimeStamp(Instant ceationTimeStamp) {
        this.creationTimeStamp = ceationTimeStamp;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public Set<DrawEntity> getDraws() {
        return draws;
    }

    public void setDraws(Set<DrawEntity> draws) {
        this.draws = draws;
    }

    public List<UserEntity> getUser() {return user;}

    public void setUser(List<UserEntity> user) {this.user = user;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}

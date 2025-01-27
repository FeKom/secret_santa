package com.github.fekom.secret_santa.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "groups")
public class GroupModel {

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
    private List<UserModel> user;

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

    public List<UserModel> getUser() {
        return user;
    }

    public void setUser(List<UserModel> user) {
        this.user = user;
    }



}

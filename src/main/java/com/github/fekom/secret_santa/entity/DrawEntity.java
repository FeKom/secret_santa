package com.github.fekom.secret_santa.entity;

import jakarta.persistence.*;



@Entity
@Table(name = "draws")
public class DrawEntity {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "draw_id",
            nullable = false
    )
    private Long drawId;

    @ManyToOne
    @JoinColumn(name = "group_id",
            nullable = false
    )
    private GroupEntity group;

    @ManyToOne
    @JoinColumn(name = "drawer_id")
    private UserEntity drawer;

    @ManyToOne
    @JoinColumn(name = "drawn_id")
    private UserEntity drawn;

    public UserEntity getDrawn() {
        return drawn;
    }

    public void setDrawn(UserEntity drawn) {
        this.drawn = drawn;
    }

    public UserEntity getDrawer() {
        return drawer;
    }

    public void setDrawer(UserEntity drawer) {
        this.drawer = drawer;
    }

    public Long getDrawId() {
        return drawId;
    }

    public void setDrawId(Long drawId) {
        this.drawId = drawId;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }


}

package com.sapo.edu.managetransferslip.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "roles")
public class RolesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    Set<UsersEntity> users;
}

package com.tpsolution.animestore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", length = 11, nullable = false, unique = true)
    private int roleId;

    @Column(name = "role_name", length = 50, nullable = false, unique = true)
    private String roleName;

    @Column(name = "roles_descripion",nullable = true)
    private String rolesDescripion;

    @Column(name = "is_deleted",nullable = true)
    private boolean isDeleted;

    @Column(name = "created_date",nullable = true)
    private Date createdDate;
}

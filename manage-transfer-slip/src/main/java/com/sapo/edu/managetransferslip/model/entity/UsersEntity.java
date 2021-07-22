package com.sapo.edu.managetransferslip.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class    UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code", unique = true)
    @NotNull(message = "Code is not null")
    private String code;

    public UsersEntity(String code, String username, String password, String email, String phone, String address, Date dob, int status, Timestamp createAt, Timestamp updateAt, Timestamp deleteAt,int inventoryId) {
        this.code = code;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
        this.status = status;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.deleteAt = deleteAt;
        this.inventoryId = inventoryId;

    }

    @Column(name = "username")
    @Pattern(regexp = "[a-zA-z0-9]*")
    @NotNull(message = "Username is not null")
    private String username;

    @Column(name = "inventory_id")
    private int inventoryId;

    @Column(name = "password")
//    @Pattern(regexp = "[a-zA-Z0-9]{8,}", message = "Password at least 8 characters")
    @NotNull(message = "Password is not null")
    private String password;

    @Column(name = "email")
    @NotNull(message = "Email is not null")
    @Email(message = "Email is invalid")
    private String email;

    @Column(name = "phone")
    @NotNull(message = "Phone is not null")
    private String phone;

    @Column(name = "address")
    @NotNull(message = "Code is not null")
    private String address;

    @Column(name = "dob")
    @NotNull(message = "Date of birth is not null")
    private Date dob;

    @Column(name = "status")
    @NotNull(message = "Status is not null")
    private int status;

    @Column(name = "create_at")
    @CreationTimestamp
    private Timestamp createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    private Timestamp updateAt;

    @Column(name = "delete_at")
    private Timestamp deleteAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
        joinColumns={@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false, updatable = false)}
    )
    Set<RolesEntity> roles;

    @OneToMany(mappedBy="user")
    private List<TransferEntity> transferEntities;

    @OneToMany(mappedBy = "user")
    private List<HistoryEntity> historyEntities;


}



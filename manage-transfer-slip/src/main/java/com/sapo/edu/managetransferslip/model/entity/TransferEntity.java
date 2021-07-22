package com.sapo.edu.managetransferslip.model.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
@NoArgsConstructor
@Entity
@Table(name = "transfer")
@Getter
@Setter
public class TransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "note")
    private String note;

    @Column(name = "status")
    private int status;

    @Column(name = "create_at")
    @CreationTimestamp
    private Timestamp createAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Column(name = "moving_at")
    private Timestamp movingAt;

    @Column(name = "update_at")
    private Date updateAt;

    @Column(name = "finish_at")
    private Timestamp finishAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inventory_input_id")
    private InventoriesEntity inventoryInput;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inventory_output_id")
    private InventoriesEntity inventoryOutput;

    @OneToMany(mappedBy="transfer")
    private List<TransferDetailEntity> transferDetailEntities;


}

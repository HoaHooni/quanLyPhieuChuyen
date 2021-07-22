package com.sapo.edu.managetransferslip.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transfer_detail")
public class TransferDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transfer_id")
    private TransferEntity transfer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private ProductsEntity product;

    @Column(name = "name")
    private String name;

    @Column(name = "number_pro")
    @Min(0)
    private int total;
}

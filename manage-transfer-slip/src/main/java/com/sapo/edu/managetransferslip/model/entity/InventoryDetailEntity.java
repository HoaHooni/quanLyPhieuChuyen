package com.sapo.edu.managetransferslip.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.sql.Date;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "inventory_detail")
public class InventoryDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private ProductsEntity product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inventory_id")
    private InventoriesEntity inventory;

    @Column(name = "number_pro")
    @Min(0)
    private int numberPro;

    @Column(name = "create_at")
    @CreationTimestamp
    private Timestamp createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    private Timestamp updateAt;

    @Column(name = "_deleted_at")
    private Timestamp deletedAt;

    public InventoryDetailEntity(ProductsEntity product, InventoriesEntity inventory, int numberPro) {

        this.product = product;
        this.inventory = inventory;
        this.numberPro = numberPro;
    }

    @Override
    public String toString() {
        return "InventoryDetailEntity{" +
                "id=" + id +
                ", product=" + product +
                ", inventory=" + inventory +
                ", numberPro=" + numberPro +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", deletedAt=" + deletedAt +
                '}';
    }
}

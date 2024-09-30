package com.loulysoft.moneytransfer.accounting.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nature_service")
public class NatureServiceEntity {

    @Id
    @Column(name = "nat_ser_code")
    private String code;

    @Column(name = "nat_ser_description")
    private String description;

    @Column(name = "nat_ser_composant")
    private String composant;
}

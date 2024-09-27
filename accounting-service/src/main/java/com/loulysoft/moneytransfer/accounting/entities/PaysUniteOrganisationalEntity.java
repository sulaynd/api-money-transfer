package com.loulysoft.moneytransfer.accounting.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "uo_pays")
public class PaysUniteOrganisationalEntity {

    @Id
    @Column(name = "uop_uo_id")
    private Long id;

    @Id
    @Column(name = "uop_ps_code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "uop_uo_id")
    private UniteOrganisationalEntity uoId;

    @ManyToOne
    @JoinColumn(name = "uop_ps_code")
    private PaysEntity psCode;
}

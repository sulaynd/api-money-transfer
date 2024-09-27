package com.loulysoft.moneytransfer.accounting.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compte")
public class CompteEntity {
    @Id
    @Column(name = "cmp_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cmp_id_generator")
    @SequenceGenerator(name = "cmp_id_generator", allocationSize = 1, initialValue = 1000, sequenceName = "cmp_id_seq")
    private Long id;

    @Column(name = "cmp_solde")
    private BigDecimal solde;

    @Column(name = "cmp_min_solde")
    private BigDecimal minSolde;

    @Column(name = "cmp_max_solde")
    private BigDecimal maxSolde;

    @ManyToOne
    @JoinColumn(name = "cmp_tc_code")
    private TypeCompteEntity typeCompte;

    @ManyToOne
    @JoinColumn(name = "cmp_uo_id")
    private UniteOrganisationalEntity owner;

    @Column(name = "cmp_dernier_mouvement")
    private LocalDateTime dernierMouvement;
}

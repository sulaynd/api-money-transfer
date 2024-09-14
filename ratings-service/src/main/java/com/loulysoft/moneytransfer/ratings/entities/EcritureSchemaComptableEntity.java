package com.loulysoft.moneytransfer.ratings.entities;

import com.loulysoft.moneytransfer.ratings.utils.DebitCredit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ecriture_schema_comptable")
public class EcritureSchemaComptableEntity {
    @Id
    @Column(name = "esc_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "esc_id_generator")
    @SequenceGenerator(name = "esc_id_generator", allocationSize = 1, initialValue = 1000, sequenceName = "esc_id_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "esc_ce_code")
    private CodeEcritureEntity writer;

    @JoinColumn(name = "esc_sc_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SchemaComptableEntity schema;

    @JoinColumn(name = "esc_csc_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CompteSchemaComptableEntity account;

    @JoinColumn(name = "esc_msc_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MontantSchemaComptableEntity amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "esc_direction", nullable = false, length = 6)
    private DebitCredit direction;

    @Column(name = "esc_rang", nullable = false)
    private Integer rang;
}

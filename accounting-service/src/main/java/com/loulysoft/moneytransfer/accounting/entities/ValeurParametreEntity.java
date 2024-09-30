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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "valeur_parametre")
public class ValeurParametreEntity {
    @Id
    @Column(name = "vp_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vp_id_generator")
    @SequenceGenerator(name = "vp_id_generator", allocationSize = 1, initialValue = 1000, sequenceName = "vp_id_seq")
    private Long id;

    @Column(name = "vp_value", columnDefinition = "NUMERIC(19,2)")
    private BigDecimal value;

    //    @Column(name = "vp_uo_id")
    //    private Long companyId;
    @ManyToOne
    @JoinColumn(name = "vp_uo_id")
    @ToString.Exclude
    private UniteOrganisationalEntity company;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vp_param_code")
    @ToString.Exclude
    private ParametreEntity param;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vp_gri_id")
    @ToString.Exclude
    private GrilleEntity grille;

    //    @Column(name = "vp_ps_code")
    //    private String paysCode;
    @ManyToOne(optional = false)
    @JoinColumn(name = "vp_ps_code")
    @ToString.Exclude
    private PaysEntity pays;
    //
    //    @ManyToOne
    //    @JoinColumn(name = "vp_dev_code")
    //    private DeviseEntity devise;
}

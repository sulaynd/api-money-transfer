package com.loulysoft.moneytransfer.accounting.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "valeur_parametre",
        indexes = {@Index(name = "idx_company_pays", columnList = "companyId, paysCode")})
public class ValeurParametreEntity {
    @Id
    @Column(name = "vp_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vp_id_generator")
    @SequenceGenerator(name = "vp_id_generator", allocationSize = 1, initialValue = 1000, sequenceName = "vp_id_seq")
    private Long id;

    @Column(name = "vp_value", columnDefinition = "NUMERIC(19,2)")
    private BigDecimal value;

    @Column(name = "vp_uo_id")
    private Long companyId;
    //    @ManyToOne
    //    @JoinColumn(name = "vp_uo_id")
    //    private UniteOrganisationalEntity company;

    @ManyToOne
    @JoinColumn(name = "vp_param_code")
    private ParametreEntity param;

    @ManyToOne
    @JoinColumn(name = "vp_gri_id")
    private GrilleEntity grille;

    @Column(name = "vp_ps_code")
    private String paysCode;
    //    @ManyToOne
    //    @JoinColumn(name = "vp_ps_code")
    //    private PaysEntity pays;
    //
    //    @ManyToOne
    //    @JoinColumn(name = "vp_dev_code")
    //    private DeviseEntity devise;
}

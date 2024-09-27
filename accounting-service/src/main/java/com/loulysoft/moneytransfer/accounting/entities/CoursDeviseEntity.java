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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cours_devise")
public class CoursDeviseEntity {
    @Id
    @Column(name = "cd_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cd_id_generator")
    @SequenceGenerator(name = "cd_id_generator", allocationSize = 1, initialValue = 1000, sequenceName = "cd_id_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cd_source")
    private DeviseEntity source;

    @ManyToOne
    @JoinColumn(name = "cd_cible")
    private DeviseEntity cible;

    @Column(name = "cd_facteur")
    private BigDecimal facteur;

    @Column(name = "cd_marge")
    private BigDecimal marge;

    @Column(name = "cd_cours_paratique")
    private BigDecimal coursPratique;

    @ManyToOne
    @JoinColumn(name = "cd_cdt_id")
    CoursDeviseTemplateEntity templateCoursDevise;
}

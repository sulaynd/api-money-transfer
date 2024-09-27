package com.loulysoft.moneytransfer.accounting.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cours_devise_template")
public class CoursDeviseTemplateEntity {

    @Id
    @Column(name = "cdt_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cdt_id_generator")
    @SequenceGenerator(name = "cdt_id_generator", allocationSize = 1, initialValue = 1000, sequenceName = "cdt_id_seq")
    private Long id;

    @Column(name = "cdt_libelle")
    private String libelle;

    @Column(name = "cdt_description")
    private String description;

    @OneToMany
    @JoinTable(
            name = "uo_cours_devise_template",
            joinColumns = {@JoinColumn(name = "ucdt_cdt_id")},
            inverseJoinColumns = {@JoinColumn(name = "ucdt_uo_id")})
    private Set<UniteOrganisationalEntity> compagnies = new HashSet<>();

    //    @OneToMany(mappedBy = "templateCoursDevise")
    //    private Set<CoursDeviseEntity> items = new HashSet<>();
}

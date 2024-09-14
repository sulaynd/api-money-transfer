package com.loulysoft.moneytransfer.ratings.entities;

import com.loulysoft.moneytransfer.ratings.utils.Category;
import com.loulysoft.moneytransfer.ratings.utils.TypeCompte;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "type_compte")
public class CompteEntity {
    @Id
    @Column(name = "tc_code")
    private TypeCompte code;

    @Column(name = "tc_description")
    private String description;

    @Column(name = "tc_tracked")
    private Character tracked;

    @Enumerated(EnumType.STRING)
    @Column(name = "tc_category")
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "type_unite_org_type_compte",
            schema = "",
            joinColumns = {@JoinColumn(name = "tuotc_tc_code", nullable = false, updatable = false, insertable = false)
            },
            inverseJoinColumns = {
                @JoinColumn(name = "tuotc_tuo_code", nullable = false, updatable = false, insertable = false)
            })
    private Set<TypeUniteOrganisationalEntity> typeUniteOrganisationnelles = new HashSet<>();
}

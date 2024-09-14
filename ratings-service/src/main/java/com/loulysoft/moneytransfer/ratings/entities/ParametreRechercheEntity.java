package com.loulysoft.moneytransfer.ratings.entities;

import com.loulysoft.moneytransfer.ratings.utils.Pivot;
import com.loulysoft.moneytransfer.ratings.utils.Type;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parametre_recherche")
public class ParametreRechercheEntity {

    @Id
    @Column(name = "pr_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pr_id_generator")
    @SequenceGenerator(name = "pr_id_generator", allocationSize = 1, initialValue = 1000, sequenceName = "pr_id_seq")
    private Long id;

    @Max(3)
    @Min(0)
    @Column(name = "pr_niveau", nullable = false)
    private Integer niveau;

    @Enumerated(EnumType.STRING)
    @Column(name = "pr_pivot", nullable = false)
    private Pivot pivot;

    @Enumerated(EnumType.STRING)
    @Column(name = "pr_type", nullable = false)
    private Type type;
}

package com.loulysoft.moneytransfer.accounting.entities;

import com.loulysoft.moneytransfer.accounting.enums.Operateur;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "operateur_arithmetique")
public class OperateurArithmetiqueEntity {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "oa_operateur", length = 25)
    private Operateur operateur;

    @Column(name = "oa_symbole", length = 5)
    private String symbole;

    @Column(name = "oa_description")
    private String description;
}

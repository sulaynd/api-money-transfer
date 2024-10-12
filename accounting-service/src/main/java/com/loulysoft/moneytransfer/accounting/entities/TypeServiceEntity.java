package com.loulysoft.moneytransfer.accounting.entities;

import com.loulysoft.moneytransfer.accounting.enums.OuiNon;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "type_service")
public class TypeServiceEntity {
    @Id
    @Column(name = "ser_code", nullable = false, unique = true)
    @NotEmpty(message = "Service code is required")
    private String code;

    @Column(name = "ser_description")
    private String description;

    @Column(name = "ser_composant")
    private String composant;

    @Enumerated(EnumType.STRING)
    @Column(name = "ser_decouvert_applicable")
    private OuiNon decouvertApplicable;
}

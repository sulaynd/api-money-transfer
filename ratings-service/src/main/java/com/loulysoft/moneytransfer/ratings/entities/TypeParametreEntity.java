package com.loulysoft.moneytransfer.ratings.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "type_parametre")
public class TypeParametreEntity {
    @Id
    @Column(name = "type_param_code", nullable = false, unique = true)
    private String code;

    @Column(name = "type_param_description")
    private String description;
}

package com.loulysoft.moneytransfer.ratings.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parametre")
public class ParametreEntity {
    @Id
    @Column(name = "param_code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "param_description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "param_type_param_code")
    private TypeParametreEntity typeParametre;
}

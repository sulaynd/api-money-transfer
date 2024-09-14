package com.loulysoft.moneytransfer.ratings.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "devise")
public class DeviseEntity {
    @Id
    @Column(name = "dev_code", nullable = false, unique = true, length = 3)
    @NotEmpty(message = "Devise code is required")
    private String code;

    @Column(name = "dev_uniteMonetaire")
    private String uniteMonetaire;

    @Column(name = "dev_uniteComptable")
    private String uniteComptable;

    //    @OneToMany(cascade = CascadeType.ALL, mappedBy = "devise")
    //    private Set<PaysEntity> pays;

}

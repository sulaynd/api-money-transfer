package com.loulysoft.moneytransfer.accounting.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZoneMonetaire {

    private String code;

    private String libelle;

    @JsonIgnore
    private Devise devise;

    @JsonIgnore
    private UniteOrganisational uniteOrganisational;
}

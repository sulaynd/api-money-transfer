package com.loulysoft.moneytransfer.accounting.models;

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

    private Devise devise;

    UniteOrganisational uniteOrganisational;
}

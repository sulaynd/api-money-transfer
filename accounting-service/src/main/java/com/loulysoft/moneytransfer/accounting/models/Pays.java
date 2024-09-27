package com.loulysoft.moneytransfer.accounting.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pays {
    private String code;

    private String libelle;

    private Character statut;

    private String indicatif;

    private String language;

    private ZoneMonetaire zoneMonetaire;
}

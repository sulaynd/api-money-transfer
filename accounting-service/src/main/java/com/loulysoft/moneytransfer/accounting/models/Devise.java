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
public class Devise {
    private String code;

    @JsonIgnore
    private float uniteMonetaire;

    @JsonIgnore
    private float uniteComptable;
}

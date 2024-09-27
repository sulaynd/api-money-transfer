package com.loulysoft.moneytransfer.accounting.models;

import com.loulysoft.moneytransfer.accounting.enums.TypeGrille;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grille {

    private Long id;

    private String description;

    private String value;

    private TypeGrille type;

    //    Set<GrilleItem> paliers = new HashSet<>();
}

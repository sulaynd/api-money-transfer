package com.loulysoft.moneytransfer.accounting.models;

import com.loulysoft.moneytransfer.accounting.enums.Pivot;
import com.loulysoft.moneytransfer.accounting.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParametreRecherche {

    private Long id;

    private Integer niveau;

    private Pivot pivot;

    private Type type;
}

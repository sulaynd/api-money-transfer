package com.loulysoft.moneytransfer.ratings.models;

import com.loulysoft.moneytransfer.ratings.utils.Pivot;
import com.loulysoft.moneytransfer.ratings.utils.Type;
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

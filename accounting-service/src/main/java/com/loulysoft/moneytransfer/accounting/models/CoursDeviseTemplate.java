package com.loulysoft.moneytransfer.accounting.models;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoursDeviseTemplate {

    private Long id;

    private String libelle;

    private String description;

    private Set<UniteOrganisational> compagnies = new HashSet<>();

    //   private Set<CoursDevise> items = new HashSet<>();
}

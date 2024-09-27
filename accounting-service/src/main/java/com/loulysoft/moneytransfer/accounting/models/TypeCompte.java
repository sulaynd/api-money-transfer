package com.loulysoft.moneytransfer.accounting.models;

import com.loulysoft.moneytransfer.accounting.enums.Category;
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
public class TypeCompte {

    private String code;

    private String description;

    private Character tracked;

    private Category category;

    private Set<TypeUniteOrganisational> typeUniteOrganisationals = new HashSet<>();
}

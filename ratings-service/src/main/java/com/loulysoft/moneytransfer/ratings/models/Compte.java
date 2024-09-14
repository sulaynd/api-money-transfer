package com.loulysoft.moneytransfer.ratings.models;

import com.loulysoft.moneytransfer.ratings.utils.Category;
import com.loulysoft.moneytransfer.ratings.utils.TypeCompte;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compte {

    private TypeCompte code;

    private String description;

    private Character tracked;

    private Category category;
}

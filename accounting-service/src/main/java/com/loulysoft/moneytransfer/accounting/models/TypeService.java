package com.loulysoft.moneytransfer.accounting.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeService {
    private String code;
    private String description;
    private String composant;
    private String decouvert_applicable;
}

package com.loulysoft.moneytransfer.ratings.models;

import com.loulysoft.moneytransfer.ratings.utils.Code;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeEcriture {
    private Code code;

    private String description;
}

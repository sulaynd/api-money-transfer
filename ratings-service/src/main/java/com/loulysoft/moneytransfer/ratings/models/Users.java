package com.loulysoft.moneytransfer.ratings.models;

import com.loulysoft.moneytransfer.ratings.utils.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    private Long id;

    private String userName;

    private String password;

    private String route;

    private Roles role;

    private UniteOrganisational uniteOrganisational;
}

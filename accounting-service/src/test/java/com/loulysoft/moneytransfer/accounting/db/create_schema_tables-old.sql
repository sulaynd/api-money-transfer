create sequence zm_id_seq start with 1 increment by 50;
create sequence devise_id_seq start with 1 increment by 50;
create sequence pays_id_seq start with 1 increment by 50;
create sequence ser_id_seq start with 1 increment by 50;
create sequence tuo_id_seq start with 1 increment by 50;
create sequence grid_id_seq start with 1 increment by 50;
create sequence grid_item_id_seq start with 1 increment by 50;
create sequence sc_id_seq start with 1 increment by 50;
create sequence msc_id_seq start with 1 increment by 50;
create sequence typ_param_id_seq start with 1 increment by 50;
create sequence param_id_seq start with 1 increment by 50;
create sequence pr_id_seq start with 1 increment by 50;
create sequence mpsc_id_seq start with 1 increment by 50;
create sequence ce_id_seq start with 1 increment by 50;
--create sequence tc_id_seq start with 1 increment by 50;
create sequence csc_id_seq start with 1 increment by 50;
create sequence esc_id_seq start with 1 increment by 50;


create table zone_monetaire(
    id                      bigint default nextval('zm_id_seq') not null,
    zm_code                 text not null unique,
    zm_libelle              text not null,
    PRIMARY KEY (id)
);
create table devise(
    id                              bigint default nextval('devise_id_seq') not null,
    dev_code                        text not null unique,
    dev_uniteMonetaire              numeric,
    dev_uniteComptable              numeric,
--    dev_zm_code                     text  not null references  zone_monetaire(zm_code),
    PRIMARY KEY (id)
);
create table pays(
    id                      bigint default nextval('pays_id_seq') not null,
    ps_code                 text not null unique,
    ps_libelle              text not null,
    ps_statut               char,
    ps_indicatif            text,
    ps_language             text,
    ps_dev_code             text  not null references devise(dev_code),
    PRIMARY KEY (id)
);

create table  grille(
    id                      bigint default nextval('grid_id_seq') not null,
    grid_code               bigint not null unique,
    grid_description        text,
    grid_valeur             text ,
    grid_type               text,

    PRIMARY KEY (id)
);

create table grille_items(
    id                          bigint default nextval('grid_item_id_seq') not null,
    item_code                   bigint not null unique,
    item_sequence               integer,
    item_borneInf               numeric,
    item_borneSup               numeric,
    item_valeur                 numeric,
    item_marge                  numeric,
    item_pourcentage            char not null,
    item_grid_code              bigint  not null references grille (grid_code),

    PRIMARY KEY (id)
);

create table type_service(
    id                              bigint default nextval('ser_id_seq') not null,
    ser_code                        text not null unique,
    ser_description                 text,
    ser_composant                   text,
    ser_decouvert_applicable        text,

    PRIMARY KEY (id)
);

create table type_unite_organisationnelle(
    id                          bigint default nextval('tuo_id_seq') not null,
    tuo_code                    text not null unique,
    tuo_description             text,
    tuo_libelle                 text,
    tuo_niveau                  text not null,
    tuo_parent_code             text not null references type_unite_organisationnelle(tuo_code),
    tuo_node_type               text not null,
    tuo_autonome                text,
    tuo_gauche                  text,
    tuo_droite                  text,
    tuo_hauteur                 text,

    PRIMARY KEY (id)
);

create table schema_comptable(
    id                          bigint default nextval('sc_id_seq') not null,
    sc_code                     bigint not null unique,
    sc_statut                   char,
    sc_version                  integer,
    sc_variante                 text,
    sc_description              text,
    sc_ser_code                 text  not null references type_service(ser_code),
    sc_tuo_code                 text  not null references type_unite_organisationnelle(tuo_code),
    PRIMARY KEY (id)
);

create table type_parametre(
    id                              bigint default nextval('typ_param_id_seq') not null,
    type_param_code                 text not null unique,
    type_param_description          text ,
    PRIMARY KEY (id)
);

create table parametre(
    id                              bigint default nextval('param_id_seq') not null,
    param_code                      text not null unique,
    param_description               text ,
    param_type_param_code           text  not null references type_parametre(type_param_code),
    PRIMARY KEY (id)
);
create table montant_schema_comptable(
    id                          bigint default nextval('msc_id_seq') not null,
    msc_code                    bigint not null unique,
    msc_nom                     text not null,
    msc_rang                    integer,
    msc_round                   text not null,
    msc_param_code              text  not null references parametre(param_code),
    msc_sc_code                 bigint  not null references schema_comptable(sc_code),
    PRIMARY KEY (id)
);



create table parametre_recherche(
    id                          bigint default nextval('pr_id_seq') not null,
    pr_code                     bigint not null unique,
    pr_niveau                   integer,
    pr_pivot                    text not null,
    pr_type                     text not null,

    PRIMARY KEY (id)
);

create table montant_param_schema_comptable(
    id                          bigint default nextval('mpsc_id_seq') not null,
    mpsc_code                   bigint not null unique,
    mpsc_pr_code                bigint  not null references parametre_recherche(pr_code),
    mpsc_msc_code               bigint  not null references montant_schema_comptable(msc_code),
    PRIMARY KEY (id)
);
create table code_ecriture(
    id                          bigint default nextval('ce_id_seq') not null,
    ce_code                     text not null unique,
    ce_description                 text,
    PRIMARY KEY (id)
);

create table type_compte(
   -- id                          bigint default nextval('tc_id_seq') not null,
    tc_code                     text not null unique,
    tc_category                 text not null,
    tc_description              text,
    cp_tracked                  text,

    PRIMARY KEY (id)
);

create table compte_schema_comptable(
    id                          bigint default nextval('csc_id_seq') not null,
    csc_code                    bigint not null unique,
    csc_sc_code                 bigint  not null references schema_comptable(sc_code),
    csc_pr_code                 bigint  not null references parametre_recherche(pr_code),
    csc_tc_code                 text  not null references type_compte(tc_code),
    PRIMARY KEY (id)
);

create table ecriture_schema_comptable(
    id                          bigint default nextval('esc_id_seq') not null,
    esc_code                    bigint not null unique,
    esc_direction               text  not null,
    esc_rang                    integer,
    esc_ce_code                 text  not null references code_ecriture(ce_code),
    esc_sc_code                 bigint  not null references schema_comptable(sc_code),
    esc_csc_code                bigint  not null references compte_schema_comptable(csc_code),
    esc_msc_code                bigint  not null references montant_schema_comptable(msc_code),
    PRIMARY KEY (id)
);


--type_parametre ---> parametre -->
--type_unite_org ---> type_service ---> schema_comptable ---> montant_shema_comptable
--montant_shema_comptable + parametre_recherche ---> montant_param_shema_comptable

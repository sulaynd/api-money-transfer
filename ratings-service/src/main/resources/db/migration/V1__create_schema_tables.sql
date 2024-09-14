--create sequence zm_id_seq start with 1000 increment by 1;
--create sequence devise_id_seq start with 1 increment by 1;
--create sequence pays_id_seq start with 1000 increment by 1;
--create sequence ser_id_seq start with 1000 increment by 1;
--create sequence tuo_id_seq start with 1000 increment by 1;
create sequence grid_id_seq start with 1000 increment by 1;
create sequence grid_item_id_seq start with 1000 increment by 1;
create sequence sc_id_seq start with 1000 increment by 1;
create sequence msc_id_seq start with 1000 increment by 1;
--create sequence typ_param_id_seq start with 1000 increment by 1;
--create sequence param_id_seq start with 1000 increment by 1;
create sequence pr_id_seq start with 1000 increment by 1;
create sequence mpsc_id_seq start with 1000 increment by 1;
--create sequence ce_id_seq start with 1000 increment by 1;
--create sequence tc_id_seq start with 1000 increment by 1;
create sequence csc_id_seq start with 1000 increment by 1;
create sequence esc_id_seq start with 1000 increment by 1;



create table zone_monetaire(
    zm_code                 text not null unique,
    zm_libelle              text not null,
    PRIMARY KEY (zm_code)
);
create table devise(
    dev_code                        text not null unique,
    dev_uniteMonetaire              numeric,
    dev_uniteComptable              numeric,
--    dev_zm_code                     text  not null references  zone_monetaire(zm_code),
    PRIMARY KEY (dev_code)
);
create table pays(
    ps_code                 text not null unique,
    ps_libelle              text not null,
    ps_statut               char,
    ps_indicatif            text,
    ps_language             text,
    ps_dev_code             text  not null references devise(dev_code),
    PRIMARY KEY (ps_code)
);

create table  grille(
    grid_id                     bigint default nextval('grid_id_seq') not null,
    grid_description            text,
    grid_valeur                 text ,
    grid_type                   text,

    PRIMARY KEY (grid_id)
);

create table grille_items(
    item_id                bigint default nextval('grid_item_id_seq') not null,
    item_sequence               integer,
    item_borneInf               numeric,
    item_borneSup               numeric,
    item_value                  numeric,
    item_marge                  numeric,
    item_pourcentage            char not null,
    item_grid_id                bigint  not null references grille (grid_id),

    PRIMARY KEY (item_id)
);

create table type_service(
    ser_code                        text not null unique,
    ser_description                 text,
    ser_composant                   text,
    ser_decouvert_applicable        text,

    PRIMARY KEY (ser_code)
);

create table type_unite_organisational(
    tuo_code                    text not null unique,
    tuo_description             text,
    tuo_libelle                 text,
    tuo_niveau                  text not null,
    tuo_parent_code             text references type_unite_organisational(tuo_code),
    tuo_node_type               text not null,
    tuo_autonome                text,
    tuo_gauche                  integer,
    tuo_droite                  integer,
    tuo_hauteur                 integer,

    PRIMARY KEY (tuo_code)
);

create table schema_comptable(
    sc_id                       bigint default nextval('sc_id_seq') not null,
    sc_statut                   char,
    sc_version                  integer,
    sc_variante                 text,
    sc_description              text,
    sc_ser_code                 text  not null references type_service(ser_code),
    sc_tuo_code                 text  not null references type_unite_organisational(tuo_code),
    PRIMARY KEY (sc_id)
);

create table type_parametre(
    type_param_code                 text not null unique,
    type_param_description          text ,
    PRIMARY KEY (type_param_code)
);

create table parametre(
    param_code                      text not null unique,
    param_description               text ,
    param_type_param_code           text  not null references type_parametre(type_param_code),
    PRIMARY KEY (param_code)
);
create table montant_schema_comptable(
    msc_id                      bigint default nextval('msc_id_seq') not null,
    msc_nom                     text not null,
    msc_rang                    integer,
    msc_round                   text not null,
    msc_param_code              text  not null references parametre(param_code),
    msc_sc_id                   bigint  not null references schema_comptable(sc_id),
    PRIMARY KEY (msc_id)
);



create table parametre_recherche(
    pr_id                       bigint default nextval('pr_id_seq') not null,
    pr_niveau                   integer,
    pr_pivot                    text not null,
    pr_type                     text not null,

    PRIMARY KEY (pr_id)
);

create table montant_param_schema_comptable(
    mpsc_id                           bigint default nextval('mpsc_id_seq') not null,
    mpsc_pr_id                        bigint  not null references parametre_recherche(pr_id),
    mpsc_msc_id                       bigint  not null references montant_schema_comptable(msc_id),
    PRIMARY KEY (mpsc_id)
);
create table code_ecriture(
    ce_code                     text not null unique,
    ce_description              text,
    PRIMARY KEY (ce_code)
);

create table type_compte(
    tc_code                     text not null unique,
    tc_category                 text not null,
    tc_description              text,
    tc_tracked                  text,

    PRIMARY KEY (tc_code)
);

create table compte_schema_comptable(
    csc_id                          bigint default nextval('csc_id_seq') not null,
    csc_sc_id                       bigint  not null references schema_comptable(sc_id),
    csc_pr_id                       bigint  not null references parametre_recherche(pr_id),
    csc_tc_code                     text  not null references type_compte(tc_code),
    PRIMARY KEY (csc_id)
);

create table ecriture_schema_comptable(
    esc_id                          bigint default nextval('esc_id_seq') not null,
    esc_direction                   text  not null,
    esc_rang                        integer,
    esc_ce_code                     text  not null references code_ecriture(ce_code),
    esc_sc_id                       bigint  not null references schema_comptable(sc_id),
    esc_csc_id                      bigint  not null references compte_schema_comptable(csc_id),
    esc_msc_id                      bigint  not null references montant_schema_comptable(msc_id),
    PRIMARY KEY (esc_id)
);


--type_parametre ---> parametre -->
--type_unite_org ---> type_service ---> schema_comptable ---> montant_shema_comptable
--montant_shema_comptable + parametre_recherche ---> montant_param_shema_comptable

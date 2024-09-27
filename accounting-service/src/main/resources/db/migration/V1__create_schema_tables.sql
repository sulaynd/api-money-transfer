create sequence zm_id_seq start with 1000 increment by 1;
create sequence devise_id_seq start with 1 increment by 1;
create sequence pays_id_seq start with 1000 increment by 1;
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
create sequence vp_id_seq start with 1000 increment by 1;
create sequence cmsc_id_seq start with 1000 increment by 1;
create sequence op_id_seq start with 1000 increment by 1;
create sequence trans_id_seq start with 1000 increment by 1;
create sequence cd_id_seq start with 1000 increment by 1;
create sequence cmp_id_seq start with 1000 increment by 1;
create sequence uo_id_seq start with 1000 increment by 1;
create sequence cdt_id_seq start with 1000 increment by 1;

create table devise(
    dev_code                        text not null unique,
    dev_unite_monetaire              numeric,
    dev_unite_comptable              numeric,
--    dev_zm_code                     text  not null references  zone_monetaire(zm_code),
    PRIMARY KEY (dev_code)
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
    item_borne_inf               numeric,
    item_borne_sup               numeric,
    item_value                  numeric,
    item_marge                  numeric,
    item_pourcentage            char not null,
    item_grid_id                bigint  not null references grille(grid_id),

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


create table type_compte(
    tc_code                     text not null unique,
    tc_desc                     text,
    tc_tracked                  char,
    tc_category                 text,

    PRIMARY KEY (tc_code)
);

create table type_unite_org_type_compte(
    tuotc_tc_code                       text not null references type_compte(tc_code),
    tuotc_tuo_code                     text not null references type_unite_organisational(tuo_code),

    PRIMARY KEY (tuotc_tc_code,tuotc_tuo_code)
);


create table unite_organisational(
    uo_id                       bigint default nextval('uo_id_seq') not null,
    uo_code                     text not null unique,
    uo_libelle                  text,
    uo_statut                   char,
    uo_tuo_code                 text not null references type_unite_organisational(tuo_code),
    uo_parent_id                bigint references unite_organisational(uo_id),
    uo_root_id                  bigint references unite_organisational(uo_id),
   -- uo_pays_code                text not null references pays(ps_code),


    PRIMARY KEY (uo_id)
);




create table zone_monetaire(
    zm_code                 text not null unique,
    zm_libelle              text null,
    zm_dev_code             text not null references devise(dev_code),
    zm_uo_id                bigint not null references unite_organisational(uo_id),
    PRIMARY KEY (zm_code)
);
create table pays(
    ps_code                 text not null unique,
    ps_libelle              text not null,
    ps_statut               char,
    ps_indicatif            text,
    ps_language             text,
    ps_zm_code              text not null references zone_monetaire(zm_code),
    PRIMARY KEY (ps_code)
);

create table uo_pays(
    uop_uo_id                   bigint not null references unite_organisational(uo_id),
    uop_ps_code                 text not null references pays(ps_code),

    PRIMARY KEY (uop_uo_id,uop_ps_code)
);

create table compte(
    cmp_id                          bigint default nextval('cmp_id_seq') not null,
    cmp_solde                       numeric,
    cmp_min_solde                   numeric,
    cmp_max_solde                   numeric,
    cmp_dernier_mouvement           timestamp,
    cmp_tc_code                     text not null references type_compte(tc_code),
    cmp_uo_id                       bigint not null  references unite_organisational(uo_id),

    PRIMARY KEY (cmp_id)
);


create table schema_comptable(
    sc_id                       bigint default nextval('sc_id_seq') not null,
    sc_status                   char,
    sc_version                  integer,
    sc_variant                 text,
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


create table valeur_parametre(
    vp_id                          bigint default nextval('vp_id_seq') not null,
    vp_value                       numeric,
    vp_uo_id                       integer,
    vp_ps_code                     text  ,
    vp_param_code                  text  not null references parametre(param_code),
    vp_gri_id                      bigint  not null references grille(grid_id),
    PRIMARY KEY (vp_id)
);

create table operateur_arithmetique(
    oa_operateur                 text not null unique,
    oa_symbole                   text not null,
    oa_description               text,

    PRIMARY KEY (oa_operateur)
);

create table calcul_montant_schema_comptable(
    cmsc_id                             bigint default nextval('cmsc_id_seq') not null,
    cmsc_constante_param                numeric,
    cmsc_rang                           integer,
    cmsc_montant_param                  bigint,
    cmsc_msc_id                         bigint  not null references montant_schema_comptable(msc_id),
--    cmsc_montant_param                bigint  not null references grille(grid_id),
    cmsc_oa_operateur                   text  not null references operateur_arithmetique(oa_operateur),
    PRIMARY KEY (cmsc_id)
);


create table cours_devise_template(
    cdt_id                        bigint default nextval('cdt_id_seq') not null,
    cdt_libelle                   text null,
    cdt_description               text  null,

    PRIMARY KEY (cdt_id)
);

create table cours_devise(
    cd_id                       bigint default nextval('cd_id_seq') not null,
    cd_source                   text not null,
    cd_cible                    text not null,
    cd_facteur                  numeric,
    cd_marge                    numeric,
    cd_cours_paratique          numeric,
    cd_cdt_id                   bigint references cours_devise_template(cdt_id),

    PRIMARY KEY (cd_id)
);

create table uo_cours_devise_template(
    ucdt_cdt_id                  bigint references cours_devise_template(cdt_id),
    ucdt_uo_id                   bigint references unite_organisational(uo_id),

    PRIMARY KEY (ucdt_cdt_id,ucdt_uo_id)
);

create table transaction(
    trans_id                            bigint default nextval('trans_id_seq') not null,
    trans_user_id                       integer,
    trans_transaction_id                bigint,
    trans_entite_tierce_id              integer,
    trans_company_id                    integer,
    trans_initial_transaction           integer,
    trans_devise                        text,
    trans_pays_destination              text,
    trans_autre_parametre               text,
    trans_nature_service                text,
    trans_pays_source                   text,
    trans_date                          timestamp,
    trans_schema_id                     bigint  not null,

    PRIMARY KEY (trans_id)
);

create table operation(
    op_id                      bigint default nextval('op_id_seq') not null,
    op_montant                 numeric,
    op_msc_id                  bigint  not null references montant_schema_comptable(msc_id),
    op_trans_id                bigint  not null references transaction(trans_id),
    PRIMARY KEY (op_id)
);

create table base_montant_schema_comptable(
    bmsc_msc_id                         bigint  not null references montant_schema_comptable(msc_id) ,
    bmsc_base_id                        bigint ,

    PRIMARY KEY (bmsc_msc_id,bmsc_base_id)
);

--CREATE INDEX INDEX_NAME ON TABLE_NAME(COLUMN);

--type_parametre ---> parametre -->
--type_unite_org ---> type_service ---> schema_comptable ---> montant_shema_comptable
--montant_shema_comptable + parametre_recherche ---> montant_param_shema_comptable

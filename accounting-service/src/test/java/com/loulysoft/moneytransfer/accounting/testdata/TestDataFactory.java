package com.loulysoft.moneytransfer.accounting.testdata;

import static java.time.LocalDateTime.now;

import com.loulysoft.moneytransfer.accounting.enums.Code;
import com.loulysoft.moneytransfer.accounting.enums.DebitCredit;
import com.loulysoft.moneytransfer.accounting.enums.Niveau;
import com.loulysoft.moneytransfer.accounting.enums.NodeType;
import com.loulysoft.moneytransfer.accounting.enums.Pivot;
import com.loulysoft.moneytransfer.accounting.enums.Round;
import com.loulysoft.moneytransfer.accounting.enums.Type;
import com.loulysoft.moneytransfer.accounting.enums.Variant;
import com.loulysoft.moneytransfer.accounting.models.CodeEcriture;
import com.loulysoft.moneytransfer.accounting.models.Compte;
import com.loulysoft.moneytransfer.accounting.models.CompteSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.Devise;
import com.loulysoft.moneytransfer.accounting.models.EcritureSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.MontantParamSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.MontantSchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.Parametre;
import com.loulysoft.moneytransfer.accounting.models.ParametreRecherche;
import com.loulysoft.moneytransfer.accounting.models.Pays;
import com.loulysoft.moneytransfer.accounting.models.SchemaComptable;
import com.loulysoft.moneytransfer.accounting.models.TransactionReport;
import com.loulysoft.moneytransfer.accounting.models.TypeCompte;
import com.loulysoft.moneytransfer.accounting.models.TypeParametre;
import com.loulysoft.moneytransfer.accounting.models.TypeService;
import com.loulysoft.moneytransfer.accounting.models.TypeUniteOrganisational;
import com.loulysoft.moneytransfer.accounting.models.UniteOrganisational;
import com.loulysoft.moneytransfer.accounting.models.Users;
import com.loulysoft.moneytransfer.accounting.models.ZoneMonetaire;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestDataFactory {

    public static Devise getDevise() {

        return Devise.builder().code("XOF").uniteComptable(1).uniteMonetaire(5).build();
    }

    public static Devise getDeviseDestination() {
        return Devise.builder()
                .code("EUR")
                .uniteComptable(0.01f)
                .uniteMonetaire(0.01f)
                .build();
    }

    public static Pays getPays() {

        return Pays.builder()
                .code("SN")
                .zoneMonetaire(getZoneMonetaire())
                .indicatif("221")
                .libelle("SENEGAL")
                .build();
    }

    public static Pays getPaysDestination() {
        return Pays.builder()
                .code("FR")
                .zoneMonetaire(getZoneMonetaire())
                .indicatif("33")
                .libelle("FRANCE")
                .build();
    }

    public static ZoneMonetaire getZoneMonetaire() {

        return ZoneMonetaire.builder()
                .code("XOF")
                .devise(getDevise())
                .uniteOrganisational(getUniteOrganisational())
                .libelle("UEMOA")
                .build();
    }

    public static TypeUniteOrganisational getTypeUniteOrganisational() {
        return TypeUniteOrganisational.builder()
                .code("POS")
                .libelle("")
                .parent(null)
                .nodeType(NodeType.LEAF)
                .niveau(Niveau.QUATRE)
                .description("POS")
                .build();
    }

    public static UniteOrganisational getRootUniteOrganisational() {
        return UniteOrganisational.builder()
                .id(1269L)
                .code("SN0700002531")
                .status('A')
                .root(null)
                // .pays(getPays())
                .libelle("LoulySoft")
                .type(getTypeUniteOrganisational())
                .build();
    }

    public static UniteOrganisational getUniteOrganisational() {
        return UniteOrganisational.builder()
                .id(1001L)
                .code("SN0700000634")
                .status('A')
                .root(null)
                // .pays(getPaysDestination())
                .libelle("ETS KEUR BAMBA")
                .type(getTypeUniteOrganisational())
                .build();
    }

    public static Users getUser() {
        return Users.builder()
                .id(1020L)
                .userName("user")
                .uniteOrganisational(getUniteOrganisational())
                .build();
    }

    public static TypeService getTypeService() {
        return TypeService.builder()
                .code("CASH_TRANSFER")
                .composant("")
                .decouvert_applicable("OUI")
                .description("Envoi cash")
                .build();
    }

    public static SchemaComptable getSchemaComptable() {
        return SchemaComptable.builder()
                .id(1002L)
                .status('A')
                .variant(Variant.VARIANTE_1)
                .description("Cena")
                .service(getTypeService())
                .type(getTypeUniteOrganisational())
                .build();
    }

    public static TypeParametre getTypeParametre() {
        return TypeParametre.builder().code("CORRIDOR").description("CORRIDOR").build();
    }

    public static Parametre getParametre() {
        return Parametre.builder()
                .code("TARIF_TRANSFERT_CASH")
                .description("TARIF TRANSFERT CASH")
                .typeParametre(getTypeParametre())
                .build();
    }

    public static MontantSchemaComptable getMontantSchemaComptable() {
        return MontantSchemaComptable.builder()
                .id(1145L)
                .nom("F")
                .round(Round.UNIT)
                .rang(1)
                .schema(getSchemaComptable())
                .param(getParametre())
                // .montantSchemaComptables()
                .build();
    }

    public static ParametreRecherche getParametreRecherche() {
        return ParametreRecherche.builder()
                .id(1009L)
                .pivot(Pivot.SYSTEM)
                .niveau(0)
                .type(Type.UNITE_ORGANISATIONELLE)
                .build();
    }

    public static MontantParamSchemaComptable getMontantParamSchemaComptable() {
        return MontantParamSchemaComptable.builder()
                .id(1000L)
                .montantSchema(getMontantSchemaComptable())
                .search(getParametreRecherche())
                .build();
    }

    public static List<MontantParamSchemaComptable> getListMontantParamSchemas() {

        List<MontantParamSchemaComptable> montantParamSchemas = new ArrayList<>();
        montantParamSchemas.add(getMontantParamSchemaComptable());

        return montantParamSchemas;
    }

    public static List<MontantSchemaComptable> getListMontantSchemas() {
        MontantSchemaComptable montantSchemaComptable = new MontantSchemaComptable();
        List<MontantSchemaComptable> montantSchemas = new ArrayList<>();
        montantSchemas.add(getMontantSchemaComptable());

        return montantSchemas;
    }

    public static Set<MontantSchemaComptable> getMontantSchemas() {

        MontantSchemaComptable montantSchemaComptable = new MontantSchemaComptable();
        montantSchemaComptable.setId(1145L);
        montantSchemaComptable.setSchema(getSchemaComptable());
        montantSchemaComptable.setRang(1);
        montantSchemaComptable.setParam(getParametre());
        montantSchemaComptable.setNom("F");
        montantSchemaComptable.setRound(Round.UNIT);
        // montantSchemaComptable.getMontantSchemaComptables().add(createMontantSchema());
        Set<MontantSchemaComptable> montantSchemas = new HashSet<>();
        montantSchemas.add(montantSchemaComptable);

        return montantSchemas;
    }

    public static CodeEcriture getCodeEcriture() {
        return CodeEcriture.builder()
                .code(Code.ENCAISSEMENT_CASH)
                .description("ENCAISSEMENT CASH")
                .build();
    }

    public static CompteSchemaComptable getCompteSchemaComptable() {
        return CompteSchemaComptable.builder()
                .id(1000L)
                .typeCompte(getTypeCompte())
                .search(getParametreRecherche())
                .schema(getSchemaComptable())
                .build();
    }

    public static TypeCompte getTypeCompte() {
        Set<TypeUniteOrganisational> typeUniteOrganisationals = new HashSet<>();
        typeUniteOrganisationals.add(getTypeUniteOrganisational());

        return TypeCompte.builder()
                .code("COURANT")
                .description("COMPTE COURANT")
                .typeUniteOrganisationals(typeUniteOrganisationals)
                .build();
    }

    public static Compte getCompte() {
        return Compte.builder()
                .id(1000L)
                .maxSolde(BigDecimal.valueOf(50000))
                .minSolde(BigDecimal.valueOf(500000))
                .dernierMouvement(now())
                .typeCompte(getTypeCompte())
                .owner(getRootUniteOrganisational())
                .build();
    }

    public static EcritureSchemaComptable getEcritureSchemaComptable() {

        return EcritureSchemaComptable.builder()
                .id(1000L)
                .schema(getSchemaComptable())
                .writer(getCodeEcriture())
                .account(getCompteSchemaComptable())
                .direction(DebitCredit.CREDIT)
                .montantSchema(getMontantSchemaComptable())
                .rang(0)
                .build();
    }

    public static List<EcritureSchemaComptable> getListEcritureSchemas() {

        List<EcritureSchemaComptable> ecritureSchemas = new ArrayList<>();
        ecritureSchemas.add(getEcritureSchemaComptable());

        return ecritureSchemas;
    }

    public static TransactionReport getTransactionReport() {
        return TransactionReport.builder()
                .frais(new BigDecimal(50))
                .devise("XOF")
                .taxes(new BigDecimal(0))
                .timbre(new BigDecimal(0))
                .montantAPayer(new BigDecimal(0))
                .montant(new BigDecimal(450))
                .build();
    }
}

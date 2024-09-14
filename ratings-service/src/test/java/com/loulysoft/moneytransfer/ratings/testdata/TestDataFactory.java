package com.loulysoft.moneytransfer.ratings.testdata;

import com.loulysoft.moneytransfer.ratings.models.CodeEcriture;
import com.loulysoft.moneytransfer.ratings.models.Compte;
import com.loulysoft.moneytransfer.ratings.models.CompteSchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.EcritureSchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.MontantParamSchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.MontantSchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.Parametre;
import com.loulysoft.moneytransfer.ratings.models.ParametreRecherche;
import com.loulysoft.moneytransfer.ratings.models.SchemaComptable;
import com.loulysoft.moneytransfer.ratings.models.TypeParametre;
import com.loulysoft.moneytransfer.ratings.models.TypeService;
import com.loulysoft.moneytransfer.ratings.models.TypeUniteOrganisational;
import com.loulysoft.moneytransfer.ratings.utils.Category;
import com.loulysoft.moneytransfer.ratings.utils.Code;
import com.loulysoft.moneytransfer.ratings.utils.DebitCredit;
import com.loulysoft.moneytransfer.ratings.utils.Niveau;
import com.loulysoft.moneytransfer.ratings.utils.NodeType;
import com.loulysoft.moneytransfer.ratings.utils.Pivot;
import com.loulysoft.moneytransfer.ratings.utils.Type;
import com.loulysoft.moneytransfer.ratings.utils.TypeCompte;
import java.util.ArrayList;
import java.util.List;

public class TestDataFactory {

    public static TypeService createTypeService() {
        return TypeService.builder()
                .code("CASH_TRANSFER")
                .composant("")
                .decouvert_applicable("OUI")
                .description("Envoi cash")
                .build();
    }

    public static TypeUniteOrganisational createTypeUniteOrganisational() {
        return TypeUniteOrganisational.builder()
                .code("DISTRIBUTEUR")
                .libelle("")
                .parent(null)
                .nodeType(NodeType.LEAF)
                .niveau(Niveau.TROIS)
                .description("DISTRIBUTEUR")
                .build();
    }

    public static SchemaComptable createSchema() {
        return SchemaComptable.builder()
                .id(1000L)
                .status('A')
                .variant("VARIANTE_1")
                .description("Cena")
                .service(createTypeService())
                .type(createTypeUniteOrganisational())
                .build();
    }

    public static TypeParametre createTypeParametre() {
        return TypeParametre.builder().code("CORRIDOR").description("CORRIDOR").build();
    }

    public static Parametre createParametre() {
        return Parametre.builder()
                .code("TARIF_TRANSFERT_CASH")
                .description("TARIF TRANSFERT CASH")
                .typeParametre(createTypeParametre())
                .build();
    }

    public static MontantSchemaComptable createMontantSchema() {
        return MontantSchemaComptable.builder()
                .id(1145L)
                .nom("F")
                .round("UNIT")
                .rang(1)
                .schema(createSchema())
                .param(createParametre())
                .build();
    }

    public static ParametreRecherche createParametreRecherche() {
        return ParametreRecherche.builder()
                .id(1009L)
                .pivot(Pivot.SYSTEM)
                .niveau(0)
                .type(Type.UNITE_ORGANISATIONELLE)
                .build();
    }

    public static MontantParamSchemaComptable createMontantParamSchemaComptable() {
        return MontantParamSchemaComptable.builder()
                .id(1000L)
                .montantSchema(createMontantSchema())
                .parametreRecherche(createParametreRecherche())
                .build();
    }

    public static List<MontantParamSchemaComptable> getListMontantParamSchemas() {

        List<MontantParamSchemaComptable> montantParamSchemas = new ArrayList<>();
        montantParamSchemas.add(createMontantParamSchemaComptable());

        return montantParamSchemas;
    }

    public static CodeEcriture createCodeEcriture() {
        return CodeEcriture.builder()
                .code(Code.ENCAISSEMENT_CASH)
                .description("ENCAISSEMENT CASH")
                .build();
    }

    public static CompteSchemaComptable createCompteSchema() {
        return CompteSchemaComptable.builder()
                .id(1000L)
                .account(createCompte())
                .search(createParametreRecherche())
                .schema(createSchema())
                .build();
    }

    public static Compte createCompte() {
        return Compte.builder()
                .code(TypeCompte.COURANT)
                .category(Category.PRINCIPAL)
                .description("Compte courant")
                .tracked('Y')
                .build();
    }

    public static EcritureSchemaComptable createEcritureSchema() {

        return EcritureSchemaComptable.builder()
                .id(1000L)
                .schema(createSchema())
                .writer(createCodeEcriture())
                .account(createCompteSchema())
                .direction(DebitCredit.DEBIT)
                .amount(createMontantSchema())
                .rang(0)
                .build();
    }

    public static List<EcritureSchemaComptable> getListEcritureSchemas() {

        List<EcritureSchemaComptable> ecritureSchemas = new ArrayList<>();
        ecritureSchemas.add(createEcritureSchema());

        return ecritureSchemas;
    }
}

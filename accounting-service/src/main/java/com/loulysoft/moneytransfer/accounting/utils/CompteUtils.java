package com.loulysoft.moneytransfer.accounting.utils;

import com.loulysoft.moneytransfer.accounting.enums.Criticite;
import com.loulysoft.moneytransfer.accounting.enums.Niveau;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class CompteUtils {

    public static Criticite getCriticiteByNiveau(Niveau niveau) {
        Criticite criticite = Criticite.HAUT;

        if (niveau.compareTo(Niveau.QUATRE) == 0) {
            criticite = Criticite.BAS;

        } else if (niveau.compareTo(Niveau.TROIS) == 0) {
            criticite = Criticite.MOYEN;
        }

        return criticite;
    }

    public static List<Niveau> getNiveauxByCriticite(Criticite criticite) {
        List<Niveau> niveaux;

        if (criticite.compareTo(Criticite.BAS) == 0) {
            niveaux = List.of(Niveau.QUATRE);
        } else if (criticite.compareTo(Criticite.MOYEN) == 0) {
            niveaux = List.of(Niveau.TROIS);
        } else {
            niveaux = Arrays.asList(Niveau.DEUX, Niveau.UN);
        }

        return niveaux;
    }

    public static LocalDateTime toLocalDateTime(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        TimeZone tz = calendar.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        return LocalDateTime.ofInstant(calendar.toInstant(), zid);
    }
}

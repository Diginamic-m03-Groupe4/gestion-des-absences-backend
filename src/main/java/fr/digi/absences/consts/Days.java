package fr.digi.absences.consts;

import org.springframework.beans.factory.annotation.Value;

public class Days {
    @Value("${consts.nb_jours_conges_payes_max}")
    public static int NB_JOURS_CONGES_PAYES_MAX;
    @Value("${consts.nb_rtt_employee}")
    public static int NB_RTT_EMPLOYEE;
    @Value("${consts.nb_rtt_employeur}")
    public static int NB_RTT_EMPLOYEUR;
}

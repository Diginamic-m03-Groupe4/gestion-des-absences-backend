package fr.digi.absences.service;

import fr.digi.absences.consts.EnumFeries;
import fr.digi.absences.consts.StatutAbsenceEmployeur;
import fr.digi.absences.entity.JourFerie;
import fr.digi.absences.exception.BrokenRuleException;
import fr.digi.absences.repository.JourFerieRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JourFeriesService {

    private final JourFerieRepo jourFerieRepo;

    /**
     * Calcul le nombre de jours en semaine entre [debut ; fin[
     *
     * @param debut inclu
     * @param fin   exclu
     * @return le nombre de jours en semaine
     */
    public long calculJoursSemaine(final LocalDate debut, final LocalDate fin) {
        final DayOfWeek debutS = debut.getDayOfWeek();
        final DayOfWeek finS = fin.getDayOfWeek();

        final long jours = ChronoUnit.DAYS.between(debut, fin);
        final long joursSansWeekEnd = jours - 2 * ((jours + debutS.getValue()) / 7);

        // adjust for starting and ending on a Sunday:
        return joursSansWeekEnd + (debutS == DayOfWeek.SUNDAY ? 1 : 0) + (finS == DayOfWeek.SUNDAY ? 1 : 0);
    }

    /**
     * Calcul le nombre de jours en semaine entre 2 dates
     *
     * @param debut       la date de début
     * @param fin         la date de fin
     * @param debutInclus si la date de début est incluse dans le compte
     * @param finIncluse  si la date de fin est incluse dans le compte
     * @return le nombre de jours en semaine
     */
    public long calculJoursSemaine(final LocalDate debut, final LocalDate fin, final boolean debutInclus,
                                          final boolean finIncluse) {
        return calculJoursSemaine(debut.plusDays(debutInclus ? 0 : 1), fin.plusDays(finIncluse ? 1 : 0));
    }

    /**
     * Calcul le nombre de jours féries en semaine entre [debut ; fin[
     *
     * @param debut la date de début
     * @param fin   la date de fin
     * @return le nombre de jours
     */
    public long calculNombreJoursFeriesSemaine(final LocalDate debut, final LocalDate fin) {
        return calculNombreJoursFeriesSemaine(debut, fin, true, false);
    }

    /**
     * Calcul le nombre de jours féries en semaine entre debut et fin
     *
     * @param debut       la date de début
     * @param fin         la date de fin
     * @param debutInclus si la date de début est incluse
     * @param finIncluse  si la date de fin est incluse
     * @return le nombre de jours
     */
    public long calculNombreJoursFeriesSemaine(final LocalDate debut, final LocalDate fin, final boolean debutInclus,
                                                      final boolean finIncluse) {
        List<LocalDate> joursFeries = new ArrayList<>();
        for (int annee = debut.getYear(); annee <= fin.getYear(); annee++) {
            joursFeries.addAll(joursFeries(annee).stream().map(JourFerie::getDate).toList());
        }
        return joursFeries.stream()
                // Filtre les jours fériés en semaine
                .filter(this::estEnSemaine)
                // Filtre les jours fériés entre les 2 dates
                .filter(localDate -> localDate.isAfter(debut.minusDays(debutInclus ? 1 : 0)) && localDate.isBefore(
                        fin.plusDays(finIncluse ? 1 : 0)))
                .count();
    }

    /**
     * Calcul le nombre de jours ouvrés entre [debut ; fin[
     * C'est à dire les jours travaillés lorsqu'on enlève les week-ends et les jours fériés
     *
     * @param debut la date de début
     * @param fin   la date de fin
     * @return le nombre de jours
     */
    public long calculJoursOuvres(final LocalDate debut, final LocalDate fin) {
        return calculJoursOuvres(debut, fin, true, false);
    }

    /**
     * Calcul le nombre de jours ouvrés entre 2 dates
     * C'est à dire les jours travaillés lorsqu'on enlève les week-ends et les jours fériés
     *
     * @param debut       la date de début
     * @param fin         la date de fin
     * @param debutInclus si la date de debut est à compter
     * @param finIncluse  si la date de fin est à compter
     * @return le nombre de jours
     */
    public long calculJoursOuvres(final LocalDate debut, final LocalDate fin, final boolean debutInclus,
                                         final boolean finIncluse) {
        return calculJoursSemaine(debut, fin, debutInclus, finIncluse) - calculNombreJoursFeriesSemaine(debut, fin, debutInclus,
                finIncluse);
    }

    /**
     * Test si un jour est férié ou non
     *
     * @param date la date
     * @return si le jour est férié
     */
    public boolean estFerie(final LocalDate date) {
        return joursFeries(date.getYear()).contains(date);
    }

    /**
     * Test si un jour est en semaine ou non
     * Autrement dit, si un jour est un Lundi, un Mardi, un Mercredi, un Jeudi ou un Vendredi
     *
     * @param date le jour à tester
     * @return si il s'agit d'un jour de semaine
     */
    public boolean estEnSemaine(LocalDate date) {
        return date.getDayOfWeek() != DayOfWeek.SUNDAY && date.getDayOfWeek() != DayOfWeek.SATURDAY;
    }

    /**
     * Donne la liste des jours féries en France pour une année
     *
     * @param annee l'année
     * @return une liste de taille fixe contenant les dates
     */
    public List<JourFerie> joursFeries(int annee) {
        List<JourFerie> jourFeries = jourFerieRepo.findByAnnee(annee);
        if(jourFeries.isEmpty()){
            jourFeries = Arrays.asList(jourDeLAn(annee), lundiDePaques(annee), feteDuTravail(annee), victoireDesAllies(annee),
                    ascension(annee), lundiDePentecote(annee), feteNationale(annee), assomption(annee), toussaint(annee),
                    armistice(annee), noel(annee));
            jourFerieRepo.saveAll(jourFeries);
        }
        return jourFeries;
    }

    /**
     * Calcul de la date de Pâques
     *
     * @param annee l'année
     * @return la date de Pâques
     * @see <a href=https://fr.wikipedia.org/wiki/Calcul_de_la_date_de_P%C3%A2ques>Article Wikipedia sur le calcul de la date de paques</a>
     */
    public JourFerie paques(int annee) {

        // cycle de Méton
        int n = annee % 19;
        // centaine de l'annee
        int c = annee / 100;
        // rang de l'annee
        int u = annee % 100;
        int s = c / 4;
        int t = c % 4;
        // cycle de proemtose
        int p = (c + 8) / 25;
        // proemptose
        int q = (c - p + 1) / 3;
        // épacte
        int e = (19 * n + c - s - q + 15) % 30;
        int b = u / 4;
        int d = u % 4;
        // lettre dominicale
        int l = (32 + 2 * t + 2 * b - e - d) % 7;
        // correction
        int h = (n + 11 * e + 22 * l) / 451;
        int f = e + l - 7 * h + 114;
        int mois = f / 31;
        int jours = f % 31 + 1;

        return JourFerie.builder()
                .date(LocalDate.of(annee, mois, jours))
                .isWorked(false)
                .statutAbsenceEmployeur(StatutAbsenceEmployeur.VALIDEE)
                .libelle(EnumFeries.PAQUES)
                .build();
    }

    /**
     * Donne la date du lundi de Pâques
     * 1 jour après Pâques
     *
     * @param annee l'année
     * @return une nouvelle instance de {@link LocalDate}
     */
    public JourFerie lundiDePaques(int annee) {
        return JourFerie.builder()
                .date(paques(annee).getDate().plusDays(1))
                .isWorked(false)
                .statutAbsenceEmployeur(StatutAbsenceEmployeur.VALIDEE)
                .libelle(EnumFeries.JEUDI_ASCENSION)
                .build();
    }

    /**
     * Donne la date l'ascension
     * 39 jours après Pâques
     *
     * @param annee l'année
     * @return une nouvelle instance de {@link LocalDate}
     */
    public JourFerie ascension(int annee) {
        return JourFerie.builder()
                .date(paques(annee).getDate().plusDays(39))
                .isWorked(false)
                .statutAbsenceEmployeur(StatutAbsenceEmployeur.VALIDEE)
                .libelle(EnumFeries.JEUDI_ASCENSION)
                .build();
    }

    /**
     * Donne la date du lundi de Pentecote
     * 50 jours après Pâques
     *
     * @param annee l'année
     * @return une nouvelle instance de {@link LocalDate}
     */
    public JourFerie lundiDePentecote(int annee) {
        return JourFerie.builder()
                .date(paques(annee).getDate().plusDays(50))
                .isWorked(false)
                .statutAbsenceEmployeur(StatutAbsenceEmployeur.VALIDEE)
                .libelle(EnumFeries.LUNDI_PENTECOTE)
                .build();
    }

    /**
     * Donne la date du jour de l'an
     * 1er jour de l'année
     *
     * @param annee l'année
     * @return une nouvelle instance de {@link LocalDate}
     */
    public JourFerie jourDeLAn(int annee) {
        return JourFerie.builder()
                .date(LocalDate.of(annee, Month.JANUARY, 1))
                .isWorked(false)
                .statutAbsenceEmployeur(StatutAbsenceEmployeur.VALIDEE)
                .libelle(EnumFeries.JOUR_DE_L_AN)
                .build();
    }

    /**
     * Fête du travail
     * 1 Mai de l'année
     *
     * @param annee demandée
     * @return une nouvelle instance de {@link LocalDate}
     */
    public JourFerie feteDuTravail(int annee) {
        return JourFerie.builder()
                .date(LocalDate.of(annee, Month.MAY, 1))
                .isWorked(false)
                .statutAbsenceEmployeur(StatutAbsenceEmployeur.VALIDEE)
                .libelle(EnumFeries.FETE_DU_TRAVAIL)
                .build();
    }

    /**
     * Victoire des Alliés
     * 8 Mai de l'année
     *
     * @param annee l'année
     * @return une nouvelle instance de {@link LocalDate}
     */
    public JourFerie victoireDesAllies(int annee) {
        return JourFerie.builder()
                .date(LocalDate.of(annee, Month.MAY, 8))
                .isWorked(false)
                .statutAbsenceEmployeur(StatutAbsenceEmployeur.VALIDEE)
                .libelle(EnumFeries.VICTOIRE_1945)
                .build();
    }

    /**
     * Fête Nationale
     * 14 Juillet de l'année
     *
     * @param annee l'année
     * @return une nouvelle instance de {@link LocalDate}
     */
    public JourFerie feteNationale(int annee) {
        return JourFerie.builder()
                .date(LocalDate.of(annee, Month.JULY, 14))
                .isWorked(false)
                .statutAbsenceEmployeur(StatutAbsenceEmployeur.VALIDEE)
                .libelle(EnumFeries.FETE_NATIONALE)
                .build();
    }

    /**
     * Toussaint
     * 1 Novembre de l'année
     *
     * @param annee l'année
     * @return une nouvelle instance de {@link LocalDate}
     */
    public JourFerie toussaint(int annee) {
        return JourFerie.builder()
                .date(LocalDate.of(annee, Month.NOVEMBER, 1))
                .isWorked(false)
                .statutAbsenceEmployeur(StatutAbsenceEmployeur.VALIDEE)
                .libelle(EnumFeries.TOUSSAINT)
                .build();
    }

    /**
     * Assomption
     * 15 Aout de l'année
     *
     * @param annee l'année
     * @return une nouvelle instance de {@link LocalDate}
     */
    public JourFerie assomption(int annee) {
        return JourFerie.builder()
                .date(LocalDate.of(annee, Month.AUGUST, 15))
                .isWorked(false)
                .statutAbsenceEmployeur(StatutAbsenceEmployeur.VALIDEE)
                .libelle(EnumFeries.ASSOMPTION)
                .build();
    }

    /**
     * Armistice
     * 11 Novembre de l'année
     *
     * @param annee l'année
     * @return une nouvelle instance de {@link LocalDate}
     */
    public JourFerie armistice(int annee) {
        return JourFerie.builder()
                .date(LocalDate.of(annee, Month.NOVEMBER, 11))
                .isWorked(false)
                .statutAbsenceEmployeur(StatutAbsenceEmployeur.VALIDEE)
                .libelle(EnumFeries.ARMISTICE)
                .build();
    }

    /**
     * Noël
     * 25 Décembre de l'année
     *
     * @param annee l'année
     * @return une nouvelle instance de {@link LocalDate}
     */
    public JourFerie noel(int annee) {
        return JourFerie.builder()
                .date(LocalDate.of(annee, Month.DECEMBER, 25))
                .isWorked(false)
                .statutAbsenceEmployeur(StatutAbsenceEmployeur.VALIDEE)
                .libelle(EnumFeries.NOEL)
                .build();
    }

    public JourFerie changeJoursFerie(JourFerie jourFerie) {
        List<JourFerie> joursFeriesPourAnnee = joursFeries(jourFerie.getDate().getYear()).stream()
                .filter(jourFerie1 -> jourFerie1.getDate().isEqual(jourFerie.getDate()))
                .toList();
        if(joursFeriesPourAnnee.isEmpty()){
            throw new BrokenRuleException("Le jour férié envoyé ne correspond à aucun jour férié réel");
        }
        JourFerie correspondingJF = joursFeriesPourAnnee.get(0);
        correspondingJF.setWorked(!correspondingJF.isWorked());
        jourFerieRepo.save(correspondingJF);
        return correspondingJF;
    }
}

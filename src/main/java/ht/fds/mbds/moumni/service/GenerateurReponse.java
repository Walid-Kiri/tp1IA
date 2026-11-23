package ht.fds.mbds.moumni.service;

import jakarta.enterprise.context.Dependent;

import java.io.Serializable;
import java.util.Locale;

/**
 * Classe de service pour les modificateurs de question.
 * Un modificateur de question prend en entrée une question et retourne
 * la question modifiée.
 */
@Dependent
public class GenerateurReponse implements Serializable {

    public String genererReponse(String question, String roleSysteme) {

        if (question == null || question.isBlank()) {
            return "Faut poser une question sur lol.";
        }

        String texte = question.toLowerCase(Locale.FRENCH).trim();

        StringBuilder resultat = new StringBuilder();

        resultat.append("Bonjour ! Voici ma réponse :\n\n");

        if (texte.contains("toplane") || texte.contains("top")) {
            resultat.append("TOPLANE :\n");
            resultat.append("Role se jouant dans la lane Superieur de la carte. \n");
            resultat.append("Role oriente solo. \n");
            resultat.append("On y retrouve souvent les personnages de classe Tank ou Combattant. \n");
            resultat.append("Elle est considere comme une lane isole.");

        } else if (texte.contains("jungle") || texte.contains("jungler")) {
            resultat.append("JUNGLE :\n");
            resultat.append("Role sans lane specifique \n");
            resultat.append("Il consiste a recolter des ressources xp et gold dans les zones situe entre les lanes. \n");
            resultat.append("Et apporte de l'aide au differente lane si besoin, \n");
            resultat.append("ou a recolter different objectif majeur tour, dragon ou Baron Nashor. \n");
            resultat.append("C'est un role qui est rime souvent avec defaite.");

        } else if (texte.contains("midlane") || texte.contains("mid")) {
            resultat.append("MIDLANE :\n");
            resultat.append("Role se jouant dans la lane milieu de la carte \n");
            resultat.append("Role oriente solo mais tres susceptible a l'embuscade du jungler, \n");
            resultat.append("Mais en contrepartie ils peuvent eux aussi assister les autres lanes. \n");
            resultat.append("On y retrouve tres souvent des assassins et des mages. \n");
            resultat.append("C'est un role tres populaires pour les nouveaux joueurs. ");

        } else if (texte.contains("botlane") || texte.contains("bot")) {
            resultat.append("BOTLANE :\n");
            resultat.append("Role se jouant dans la lane inferieur de la carte. \n");
            resultat.append("Role oriente duo avec son support. \n");
            resultat.append("On y retrouve tres souvent des tireurs ou dans de rare cas des mages. \n");
            resultat.append("C'est un role tres complique car dependant d'autrui.");

        } else if (texte.contains("support") || texte.contains("supp")) {
            resultat.append("SUPPORT :\n");
            resultat.append("Role se jouant en duo avec le Botlaner ou carry (adc). \n");
            resultat.append("On y trouve plusieur types de champion tel que les mages, les enchanteurs et les tanks. \n");
            resultat.append("Role qui peut tout aussi bien assister le carry que les autres lanes. \n");
            resultat.append("C'est un role tres dependant de son equipe.");
        } else {
            resultat.append("Je suis votre assistant en lolien. \n");
            resultat.append("Je n'ai pas de reponse pour des questions apparetenant a d'autre domaine. \n");
            resultat.append("Essayez de préciser votre sujet : TOPLANE, MIDLANE, JUNGLER etc...");
        }


        return resultat.toString();
    }
}

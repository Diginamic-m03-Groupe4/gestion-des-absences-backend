package fr.digi.absences.utils;

import fr.digi.absences.exception.TechnicalException;

import java.util.Map;

public class TemplateUtils {

    /**
     * Méthode permettant de remplacer tous les instances de {%<parametre>%} par sa valeur correspondante
     * passée dans une map
     * @param parameters Map contenant en clé le nom des paramètres, et en valeur leur valeur
     * @param stringToFormat la chaine de caractère à formater
     * @return une chaine de caractère formattée à partir d'une map
     * */
    public static String formatString(Map<String, String> parameters, String stringToFormat){
        int positionOfLastOpenVar = 0;
        int positionOfLastCloseVar = 0;
        StringBuilder formattedTemplate = new StringBuilder();
        for (int i = 0; i < stringToFormat.length(); i++) {
            if (i > 0 && stringToFormat.charAt(i) ==  '%' && stringToFormat.charAt(i-1) == '{') {
                positionOfLastOpenVar = i-1;
                formattedTemplate.append(stringToFormat, positionOfLastCloseVar, positionOfLastOpenVar);
            } else if (i == stringToFormat.length() - 1) {
                formattedTemplate.append(stringToFormat, positionOfLastCloseVar, stringToFormat.length() -1);
            } else if (stringToFormat.charAt(i) == '%' &&  stringToFormat.charAt(i+1) == '}'){
                positionOfLastCloseVar = i + 2;
                if(!parameters.containsKey(stringToFormat.substring(positionOfLastOpenVar, positionOfLastCloseVar))){
                    throw new TechnicalException("Erreur dans un template : une clé semble ne pas avoir correctement été formattée");
                }
                formattedTemplate.append(parameters.get(stringToFormat.substring(positionOfLastOpenVar, positionOfLastCloseVar)));
            }
        }
        return formattedTemplate.toString();
    }
}

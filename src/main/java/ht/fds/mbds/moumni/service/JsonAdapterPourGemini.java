package ht.fds.mbds.moumni.service;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;
import jakarta.json.stream.JsonGenerator;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

@ViewScoped
public class JsonAdapterPourGemini implements Serializable {

    private String roleSysteme;
    private String historique = "[]";

    @Inject
    private LlmClientPourGemini llmClient;

    public void setRoleSysteme(String roleSysteme) {
        this.roleSysteme = roleSysteme;
    }

    public LlmInteraction envoyerRequete(String question) throws RequeteException {
        JsonObject requete = creerRequeteJson(question);
        String retour = llmClient.envoyerRequete(requete.toString());

        JsonObject reponse;
        try (JsonReader lecteur = Json.createReader(new StringReader(retour))) {
            reponse = lecteur.readObject();
        }

        JsonArray candidats = reponse.getJsonArray("candidates");
        if (candidats == null || candidats.isEmpty()) {
            throw new RequeteException("Gemini n'a renvoyé aucune réponse.");
        }

        JsonObject contenu = candidats.getJsonObject(0).getJsonObject("content");
        String texte = extractReponse(contenu);

        // Conserver la question et la réponse pour le prochain envoi.
        historique = Json.createArrayBuilder(requete.getJsonArray("contents"))
                .add(contenu)
                .build()
                .toString();

        return new LlmInteraction(texte, prettyPrinting(requete), prettyPrinting(reponse));
    }

    private JsonObject creerRequeteJson(String question) {
        JsonObjectBuilder requete = Json.createObjectBuilder();

        if (roleSysteme != null && !roleSysteme.isBlank()) {
            requete.add("systemInstruction", Json.createObjectBuilder()
                    .add("parts", Json.createArrayBuilder()
                            .add(Json.createObjectBuilder().add("text", roleSysteme))));
        }

        return requete.add("contents", ajouteQuestionDansJsonRequete(question)).build();
    }

    private JsonArray ajouteQuestionDansJsonRequete(String question) {
        try (JsonReader lecteur = Json.createReader(new StringReader(historique))) {
            JsonArrayBuilder messages = Json.createArrayBuilder(lecteur.readArray());
            messages.add(Json.createObjectBuilder()
                    .add("role", "user")
                    .add("parts", Json.createArrayBuilder()
                            .add(Json.createObjectBuilder().add("text", question))));
            return messages.build();
        }
    }

    private String extractReponse(JsonObject contenu) throws RequeteException {
        if (contenu == null || contenu.getJsonArray("parts") == null) {
            throw new RequeteException("La réponse de Gemini ne contient pas de texte.");
        }

        StringBuilder texte = new StringBuilder();
        for (JsonObject partie : contenu.getJsonArray("parts").getValuesAs(JsonObject.class)) {
            if (!partie.getBoolean("thought", false)) {
                texte.append(partie.getString("text", ""));
            }
        }

        if (texte.isEmpty()) {
            throw new RequeteException("La réponse de Gemini ne contient pas de texte.");
        }
        return texte.toString();
    }

    private String prettyPrinting(JsonObject json) {
        StringWriter sortie = new StringWriter();
        try (JsonWriter ecrivain = Json.createWriterFactory(
                Map.of(JsonGenerator.PRETTY_PRINTING, true)).createWriter(sortie)) {
            ecrivain.writeObject(json);
        }
        return sortie.toString();
    }
}
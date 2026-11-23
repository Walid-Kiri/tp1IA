package ht.fds.mbds.moumni.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class LlmClientPourGemini {

    private static final String URL =
            "https://generativelanguage.googleapis.com/v1beta/models/"
                    + "gemini-3.5-flash-lite:generateContent";

    private final String cleApi = System.getenv("GEMINI_KEY");

    public String envoyerRequete(String requeteJson) throws RequeteException {
        if (cleApi == null || cleApi.isBlank()) {
            throw new RequeteException("La variable GEMINI_KEY est absente.");
        }

        try (Client client = ClientBuilder.newClient();
             Response reponse = client.target(URL)
                     .request(MediaType.APPLICATION_JSON_TYPE)
                     .header("x-goog-api-key", cleApi)
                     .post(Entity.json(requeteJson))) {

            String contenu = reponse.readEntity(String.class);

            if (reponse.getStatus() != 200) {
                throw new RequeteException(
                        "Gemini (HTTP " + reponse.getStatus() + ") : " + contenu
                );
            }

            return contenu;
        } catch (ProcessingException e) {
            throw new RequeteException("Impossible de communiquer avec Gemini.", e);
        }
    }
}
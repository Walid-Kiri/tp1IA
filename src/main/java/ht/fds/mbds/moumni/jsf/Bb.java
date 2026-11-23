package ht.fds.mbds.moumni.jsf;

import ht.fds.mbds.moumni.service.JsonAdapterPourGemini;
import ht.fds.mbds.moumni.service.LlmInteraction;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class Bb implements Serializable {

    private static final long serialVersionUID = 1L;

    private String roleSysteme;
    private boolean roleSystemeChangeable = true;
    private List<SelectItem> listeRolesSysteme;

    private String question;
    private String reponse;
    private StringBuilder conversation = new StringBuilder();

    private boolean debug;
    private String texteRequeteJson;
    private String texteReponseJson;

    @Inject
    private JsonAdapterPourGemini jsonAdapter;

    @Inject
    private FacesContext facesContext;

    public String envoyer() {
        if (question == null || question.isBlank()) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Texte question vide",
                    "Il manque le texte de la question"
            );
            facesContext.addMessage(null, message);
            return null;
        }

        // Transmettre le rôle choisi au début de la conversation.
        if (conversation.isEmpty()) {
            jsonAdapter.setRoleSysteme(roleSysteme);
        }

        try {
            LlmInteraction interaction = jsonAdapter.envoyerRequete(question);

            reponse = interaction.reponseExtraite();
            texteRequeteJson = interaction.questionJson();
            texteReponseJson = interaction.reponseJson();

            roleSystemeChangeable = false;
            afficherConversation();
        } catch (Exception e) {
            reponse = "Erreur : " + e.getMessage();
            texteRequeteJson = "";
            texteReponseJson = "";

            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Problème de connexion avec Gemini",
                    reponse
            );
            facesContext.addMessage(null, message);
        }

        return null;
    }

    public String nouveauChat() {
        return "index";
    }

    private void afficherConversation() {
        conversation.append("== User:\n")
                .append(question)
                .append("\n== Serveur:\n")
                .append(reponse)
                .append("\n");
    }

    public List<SelectItem> getRolesSysteme() {
        if (listeRolesSysteme == null) {
            listeRolesSysteme = new ArrayList<>();

            String role = """
                    You are a helpful assistant. You help the user to find the information they need.
                    If the user type a question, you answer it.
                    """;
            listeRolesSysteme.add(new SelectItem(role, "Porofessor"));

            role = """
                    You are an interpreter. You translate from English to French and from French to English.
                    If the user type a French text, you translate it into English.
                    If the user type an English text, you translate it into French.
                    If the text contains only one to three words, give some examples of usage of these words in English.
                    """;
            listeRolesSysteme.add(new SelectItem(role, "OPGG"));

            role = """
                    Your are a travel guide. If the user type the name of a country or of a town,
                    you tell them what are the main places to visit in the country or the town
                    are you tell them the average price of a meal.
                    """;
            listeRolesSysteme.add(new SelectItem(role, "Blitz"));

            role = """
                    Tu es une ward de contrôle dans la Faille de l'invocateur de League of Legends.
                    Le joueur te dit où il te pose et quel objectif son équipe veut prendre.
                    Parle à la première personne, comme si tu étais cette ward.
                    En deux phrases, explique quel passage tu surveilles et quel angle reste dangereux.
                    Si le joueur te déplace ensuite, compare ta nouvelle vision à celle que tu viens de perdre.
                    """;
            listeRolesSysteme.add(new SelectItem(role, "La Pink Ward"));
        }

        return listeRolesSysteme;
    }

    public String getRoleSysteme() {
        return roleSysteme;
    }

    public void setRoleSysteme(String roleSysteme) {
        this.roleSysteme = roleSysteme;
    }

    public boolean isRoleSystemeChangeable() {
        return roleSystemeChangeable;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public String getConversation() {
        return conversation.toString();
    }

    public void setConversation(String conversation) {
        this.conversation = new StringBuilder(conversation);
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getTexteRequeteJson() {
        return texteRequeteJson;
    }

    public void setTexteRequeteJson(String texteRequeteJson) {
        this.texteRequeteJson = texteRequeteJson;
    }

    public String getTexteReponseJson() {
        return texteReponseJson;
    }

    public void setTexteReponseJson(String texteReponseJson) {
        this.texteReponseJson = texteReponseJson;
    }
}
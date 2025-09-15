package agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class PersonAgent extends Agent {

    // Attributs de l'agent
    private String nom;
    private int age;
    private double taille;
    private double poids;

    @Override
    protected void setup() {
        // Récupérer les arguments passés depuis l'interface JADE
        Object[] args = getArguments();
        if (args != null && args.length == 4) {
            nom = (String) args[0];
            age = Integer.parseInt((String) args[1]);
            taille = Double.parseDouble((String) args[2]);
            poids = Double.parseDouble((String) args[3]);
        } else {
            // valeurs par défaut si pas d'arguments
            nom = "Inconnu";
            age = 0;
            taille = 0.0;
            poids = 0.0;
        }

        System.out.println("Agent " + getLocalName() + " créé : " +
                nom + ", " + age + " ans, " + taille + "m, " + poids + "kg.");

        // Comportement : répondre à toute requête
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && msg.getPerformative() == ACLMessage.REQUEST) {

                    System.out.println("Message reçu");
                    String contenu = msg.getContent().trim();
                    ACLMessage reply = msg.createReply();

                    if (contenu.toLowerCase().startsWith("set")) {
                        // traiter toutes les commandes de type setXXX
                        if (contenu.toLowerCase().startsWith("setnom:")) {
                            nom = contenu.substring(7).trim();
                            reply.setContent("Nom mis à jour : " + nom);
                        } else if (contenu.toLowerCase().startsWith("setage:")) {
                            try {
                                age = Integer.parseInt(contenu.substring(7).trim());
                                reply.setContent("Âge mis à jour : " + age);
                            } catch (NumberFormatException e) {
                                reply.setContent("Valeur invalide pour l'âge.");
                            }
                        } else if (contenu.toLowerCase().startsWith("settaille:")) {
                            try {
                                taille = Double.parseDouble(contenu.substring(10).trim());
                                reply.setContent("Taille mise à jour : " + taille);
                            } catch (NumberFormatException e) {
                                reply.setContent("Valeur invalide pour la taille.");
                            }
                        } else if (contenu.toLowerCase().startsWith("setpoids:")) {
                            try {
                                poids = Double.parseDouble(contenu.substring(8).trim());
                                reply.setContent("Poids mis à jour : " + poids);
                            } catch (NumberFormatException e) {
                                reply.setContent("Valeur invalide pour le poids.");
                            }
                        }

                    } else {
                        // gestion des requêtes classiques
                        switch (contenu.toLowerCase()) {
                            case "nom":
                                reply.setContent("Mon nom est " + nom);
                                System.out.println("Dans la boucle du get nom");
                                break;
                            case "age":
                                reply.setContent("J'ai " + age + " ans");
                                break;
                            case "taille":
                                reply.setContent("Je mesure " + taille + " m");
                                break;
                            case "poids":
                                reply.setContent("Je pèse " + poids + " kg");
                                break;
                            default:
                                reply.setContent("Je ne comprends pas la question.");
                                break;
                        }
                    }

                    send(reply); // envoyer la réponse
                } else {
                    block(); // attendre un nouveau message
                }
            }
        });
    }
}

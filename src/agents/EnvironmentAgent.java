package agents;

import agents.Etudiant;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.HashSet;
import java.util.Set;

public class EnvironmentAgent extends Agent {

    private Set<Etudiant> etudiants = new HashSet<>();
    int temps_rest = 100;

    @Override
    protected void setup() {
        addBehaviour(
            new CyclicBehaviour() {
                @Override
                public void action() {
                    // attend un message REQUEST
                    ACLMessage msg = myAgent.receive(
                        MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
                    );
                    if (msg == null) {
                        block();
                        return;
                    }

                    String content = msg.getContent(); // le texte envoyé
                    ACLMessage reply = msg.createReply();

                    if (content == null) {
                        reply.setPerformative(ACLMessage.REFUSE);
                        reply.setContent("Message vide");
                    } else {
                        // exemple de traitement simple
                        switch (content.trim().toLowerCase()) {
                            case "sleep" -> {
                                reply.setPerformative(ACLMessage.INFORM);
                                reply.setContent(
                                    "fatigue-5;stress-2;espertanceNote+0;malade=false;tempsRestantQuotient-1;"
                                );
                            }
                            case "work" -> {
                                reply.setPerformative(ACLMessage.INFORM);
                                boolean malade =
                                    new java.util.Random().nextInt(3) == 0; // 1/3
                                reply.setContent(
                                    "fatigue+3;stress+3;espertanceNote+4;malade=" +
                                        malade +
                                        ";tempsRestantQuotient-1;"
                                );
                            }
                            case "sport" -> {
                                reply.setPerformative(ACLMessage.INFORM);
                                boolean malade =
                                    new java.util.Random().nextInt(3) == 0; // 1/3
                                reply.setContent(
                                    "fatigue+2;stress-4;malade=" +
                                        malade +
                                        ";tempsRestantQuotient-1;"
                                );
                            }
                            case "coffee" -> {
                                reply.setPerformative(ACLMessage.INFORM);
                                boolean malade =
                                    new java.util.Random().nextInt(10) == 0; // 1/10
                                reply.setContent(
                                    "fatigue-1;stress-1;malade=" +
                                        malade +
                                        ";tempsRestantQuotient-1;"
                                );
                            }
                            case "eatmom" -> {
                                // manger chez maman : pas de risque de malade ici
                                reply.setPerformative(ACLMessage.INFORM);
                                reply.setContent(
                                    "fatigue-2;stress-3;energie+5;malade=false;tempsRestantQuotient-1;"
                                );
                            }
                            case "cowork" -> {
                                reply.setPerformative(ACLMessage.INFORM);
                                boolean malade =
                                    new java.util.Random().nextInt(3) == 0; // 1/3
                                reply.setContent(
                                    "fatigue+1;stress+1;espertanceNote+2;malade=" +
                                        malade +
                                        ";tempsRestantQuotient-1;"
                                );
                            }
                            default -> {
                                reply.setPerformative(
                                    ACLMessage.NOT_UNDERSTOOD
                                );
                                reply.setContent(
                                    "Commande inconnue : " + content
                                );
                            }
                        }
                    }
                    send(reply);
                    temps_rest -= 1;
                }
            }
        );
    }
}

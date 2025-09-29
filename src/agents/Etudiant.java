package agents;

import java.util.Set;

import bdi.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Etudiant extends Agent {

    // Attributs de l'agent
    private String nom;
    private Croyances croyances;
    private Set<Desir> desirs;
    private Set<Intention> intentions;

    private void perceive() {
        ACLMessage msg = receive();
        if (msg != null) {
            System.out.println("Message reçu : " + msg.getContent());
            Desir.updateFromMessage(msg); // à implémenter
        }
    }


    private void deliberate() {
        for (Intention i : intentions) {
            switch (i.getName()) {
                case "Travailler":
                    i.setPriority(SystemePriorite.prioriteTravailler(
                        croyances.getEsperanceNote(),
                        croyances.getTempsRestantQuotient(),
                        croyances.getFatigue()
                    ));
                    break;

                case "Dormir":
                    i.setPriority(SystemePriorite.prioriteDormir(
                        croyances.getFatigue(),
                        croyances.getStress(),
                        croyances.isMalade()
                    ));
                    break;

                case "Faire du sport avec ses amis":
                    i.setPriority(SystemePriorite.prioriteSport(
                        croyances.getStress(),
                        croyances.getFatigue(),
                        croyances.isMalade()
                    ));
                    break;

                case "Prendre un cafe":
                    i.setPriority(SystemePriorite.prioriteCafe(
                        croyances.getFatigue(),
                        croyances.isMalade()
                    ));
                    break;

                case "Manger avec maman":
                    i.setPriority(SystemePriorite.prioriteMangerMaman(
                        croyances.getStress(),
                        croyances.isMalade()
                    ));
                    break;

                case "Co-work avec copains":
                    i.setPriority(SystemePriorite.prioriteCowork(
                        croyances.getEsperanceNote(),
                        croyances.getStress(),
                        croyances.getFatigue()
                    ));
                    break;

                default:
                    i.setPriority(0); // sécurité
            }
        }
    }

    public void executeIntention() {
    if (intentions.isEmpty()) {
        System.out.println("Pas d'intentions à exécuter.");
        return;
    }

    Intention bestIntention = null;
    int maxPriority = Integer.MIN_VALUE;

    for (Intention i : intentions) {
        if (i.getPriority() > maxPriority) {
            maxPriority = i.getPriority();
            bestIntention = i;
        }
    }

    System.out.println("Exécution de la 1ère intention : " 
                        + bestIntention.getName() 
                        + " (prio=" + bestIntention.getPriority() + ")");

    // Appeler la méthode pour exécuter le plan a!ssocié
    String messageContent;

    // Définir le contenu selon le nom de l'intention
    switch (bestIntention.getName()) {
        case "Travailler":
            messageContent = "work";
            break;
        case "Dormir":
            messageContent = "sleep";
            break;
        case "Faire du sport avec ses amis":
            messageContent = "sport";
            break;
        case "Prendre un cafe":
            messageContent = "coffee";
            break;
        case "Manger avec maman":
            messageContent = "eatmom";
            break;
        case "Co-work avec copains":
            messageContent = "cowork";
            break;
        default:
            messageContent = "unknown";
    }

    // Définir l'AID de l'agent environnement
    AID environnementAID = new AID("Environnement", AID.ISLOCALNAME);

    // Création du message ACL
    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
    msg.setContent(messageContent);

    // Destinataire = agent environnement
    msg.addReceiver(environnementAID);

    // Envoi du message
    send(msg);  // Ici c'est OK, car Etudiant est un Agent JADE

    // Affichage dans la console
    System.out.println("Message ACL envoyé : " + msg.getContent());
}

    @Override
    protected void setup() {
        // Récupérer les arguments passés depuis l'interface JADE
        Object[] args = getArguments();
        if (args != null && args.length == 1) {
            nom = (String) args[0];

        } else {
            // valeurs par défaut si pas d'arguments
            nom = "Inconnu";
        }

        this.desirs.add(new Desir("Augmenter ma santé mentale"));
        this.desirs.add(new Desir("Avoir une bonne note"));
        this.desirs.add(new Desir("Faire plaisir à Maman"));

        this.intentions.add(new Intention("Travailler"));
        this.intentions.add(new Intention("Dormir"));
        this.intentions.add(new Intention("Faire du sport avec ses amis"));
        this.intentions.add(new Intention("Prendre un cafe"));
        this.intentions.add(new Intention("Manger avec maman"));
        this.intentions.add(new Intention("Co-work avec copains"));


        System.out.println("Agent " + getLocalName() + " créé : ");

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                perceive();              // 1. perception
                Croyances.update();         // 2. mise à jour croyances
                deliberate();            // 3. choix intentions
                executeIntention();     // 4. exécution
                block(200); // pour ralentir la boucle (200ms ici)
            }
        });
    }

    
}

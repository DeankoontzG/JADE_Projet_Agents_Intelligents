package agents;

import java.util.Set;
import java.util.HashSet;
import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import bdi.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import bdi.Croyances;
import bdi.Desir;
import bdi.Intention;
import bdi.SystemePriorite;
import bdi.AffichageEtudiant;


public class Etudiant extends Agent {

    // Attributs de l'agent
    private String nom;
    private Croyances croyances;
    private Set<Desir> desirs;
    private Set<Intention> intentions;
    private AffichageEtudiant dashboard;
    private List<String> lastActions = new ArrayList<>();

    private void perceive(ACLMessage msg) {
        if (msg != null) {
            System.out.println("Message reçu : " + msg.getContent());
            croyances.updateFromMessage(msg); // à implémenter
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

    public Intention executeIntention() {
        if (intentions.isEmpty()) {
            System.out.println("Pas d'intentions à exécuter.");
            return null;
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
        AID environnementAID = new AID("EnvironnementAgent", AID.ISLOCALNAME);

        // Création du message ACL
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setContent(messageContent);

        // Destinataire = agent environnement
        msg.addReceiver(environnementAID);

        // Envoi du message
        send(msg);  // Ici c'est OK, car Etudiant est un Agent JADE

        // Affichage dans la console
        System.out.println("Message ACL envoyé : " + msg.getContent());

        return bestIntention;
    }

    private void updateDashboard(Intention bestIntention) {
        // 1. Croyances
        String croyancesText = String.format(
            "Fatigue : %d\nStress : %d\nEspérance note : %d\nMalade : %b\nTemps restant : %.2f",
            croyances.getFatigue(),
            croyances.getStress(),
            croyances.getEsperanceNote(),
            croyances.isMalade(),
            croyances.getTempsRestantQuotient()
        );
        dashboard.updateCroyances(croyancesText);

        // 2. Intentions triées
        String intentionsText = intentions.stream()
            .sorted((i1, i2) -> Integer.compare(i2.getPriority(), i1.getPriority()))
            .map(i -> i.getName() + " (prio: " + i.getPriority() + ")")
            .collect(Collectors.joining("\n"));
        dashboard.updateIntentions(intentionsText);

        // 3. Actions (on garde la liste des 5 dernières)
        if (bestIntention != null) {
            lastActions.add(bestIntention.getName());
            if (lastActions.size() > 5) lastActions.remove(0);
        }
        dashboard.updateActions(lastActions);

        // 4. Temps restant / Avancement (ici j’affiche juste le temps)
        dashboard.updateTempsRestant(String.format("%.2f", (double) croyances.getTempsRestantQuotient()));
    
    }
    
    @Override
    protected void setup() {
        // Récupérer les arguments passés depuis l'interface JADE
        Object[] args = getArguments();
        this.desirs = new HashSet<>();
        this.intentions = new HashSet<>();
        this.croyances = new Croyances(); 
        this.dashboard = new AffichageEtudiant();

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
                // Si plus de temps restant, on termine l’agent proprement.
                if (croyances.getTempsRestantQuotient() <= 0) {
                    System.out.println("⏳ " + getLocalName() + " a terminé son temps. Fin de mission !");
                    doDelete(); // termine l'agent proprement
                    return;
                } 

                ACLMessage msg = receive();
                if (msg != null) {
                    perceive(msg); // mettre à jour croyances
                }
                
                System.out.println("==> État des croyances AVANT délibération :");
                afficherCroyances();
                deliberate();            // 3. choix intentions
                System.out.println("==> Intentions triées par priorité :");
                afficherIntentions();
                Intention bestIntention = executeIntention();  // 4. Execution de la best intention
                updateDashboard(bestIntention); // <-- Ici on met à jour la GUI
                
                try {
                    Thread.sleep(1000); // 1000ms = 1 seconde
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void afficherCroyances() {
        System.out.println("Nom : " + nom);
        System.out.println("Fatigue : " + croyances.getFatigue());
        System.out.println("Stress : " + croyances.getStress());
        System.out.println("Espérance de note : " + croyances.getEsperanceNote());
        System.out.println("Malade : " + croyances.isMalade());
        System.out.println("Temps restant (quotient) : " + croyances.getTempsRestantQuotient());
        System.out.println("--------------------------------------");
    }

    private void afficherIntentions() {
        intentions.stream()
            .sorted((i1, i2) -> Integer.compare(i2.getPriority(), i1.getPriority()))
            .forEach(i ->
                System.out.println("- " + i.getName() + " (priorité : " + i.getPriority() + ")")
            );
        System.out.println("--------------------------------------");
    }

    
}

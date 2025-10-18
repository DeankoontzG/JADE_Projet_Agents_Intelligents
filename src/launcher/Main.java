package launcher;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class Main {
    public static void main(String[] args) {
        // Lancer JADE
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.GUI, "true"); // GUI activée

        ContainerController cc = rt.createMainContainer(p);

        try {
            // Créer agent environnement
            AgentController env = cc.createNewAgent(
                "EnvironnementAgent", // nom visible dans JADE
                "agents.EnvironmentAgent", // package + classe
                null // pas d'arguments
            );
            env.start();

            // Créer agent étudiant (Zizitoune)
            Object[] argsEtudiant = new Object[] { "Zizitoune" };
            AgentController etudiant = cc.createNewAgent(
                "Zizitoune", // nom dans JADE
                "agents.Etudiant", // package + classe
                argsEtudiant
            );
            etudiant.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

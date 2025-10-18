package bdi;
import jade.lang.acl.ACLMessage;

public class Desir {
    String name;
    int priority;
    
  /* Predicate<Set<String>> activationCondition;

    public boolean isActive(Set<String> beliefs) {
        return activationCondition.test(beliefs);
    }
    */
    public Desir(String name) {
      this.name = name;
      this.priority = 0; // ou valeur par défaut
    }
    
    public static void updateFromMessage(ACLMessage msg) {
      System.out.println("Je m'uipdate from message");
    }

    public static void update() {
      System.out.println("J'update mon désir");
    }
}
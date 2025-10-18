package bdi;
import java.util.Map;
import java.util.HashMap;
import jade.lang.acl.ACLMessage;

public class Croyances {

    Map<String, Object> beliefs = new HashMap<>();
    
    public Croyances() {
        this.fatigue = 0 ;
        this.stress = 0 ;
        this.esperanceNote = 0 ; 
        this.malade = false ; 
        this.tempsRestantQuotient = 1 ;
    }

    private int fatigue;                // libre (pas borné explicitement)
    private int stress;                 // entre 0 et 20
    private int esperanceNote;          // entre 0 et 20
    private boolean malade;
    private float tempsRestantQuotient; // entre 0 et 1

    // --- GETTERS ---

    public int getFatigue() {
        return fatigue;
    }

    public int getStress() {
        return stress;
    }

    public int getEsperanceNote() {
        return esperanceNote;
    }

    public boolean isMalade() {
        return malade;
    }

    public float getTempsRestantQuotient() {
        return tempsRestantQuotient;
    }

    // --- SETTERS ---

    public void setFatigue(int fatigue) {
        this.fatigue = Math.max(0, fatigue); // tu peux borner en positif si tu veux
    }

    public void setStress(int stress) {
        if (stress < 0) stress = 0;
        if (stress > 20) stress = 20;
        this.stress = stress;
    }

    public void setEsperanceNote(int esperanceNote) {
        if (esperanceNote < 0) esperanceNote = 0;
        if (esperanceNote > 20) esperanceNote = 20;
        this.esperanceNote = esperanceNote;
    }

    public void setMalade(boolean malade) {
        this.malade = malade;
    }

    public void setTempsRestantQuotient(float tempsRestantQuotient) {
        if (tempsRestantQuotient < 0f) tempsRestantQuotient = 0f;
        if (tempsRestantQuotient > 1f) tempsRestantQuotient = 1f;
        this.tempsRestantQuotient = tempsRestantQuotient;
    }

    public void updateFromMessage(ACLMessage msg) {
        System.out.println("Mise à jour des croyances à partir du message : " + msg.getContent());

        String[] updates = msg.getContent().split(";");
        for (String update : updates) {
            if (update.isEmpty()) continue;

            String[] parts = update.split("(?=[+=-])");  // Sépare nom et opérateur/valeur
            if (parts.length != 2) continue;

            String key = parts[0].trim();
            String operation = parts[1].trim();

            try {
                switch (key) {
                    case "fatigue" -> applyIntUpdate("fatigue", operation);
                    case "stress" -> applyIntUpdate("stress", operation);
                    case "espertanceNote" -> applyIntUpdate("esperanceNote", operation);
                    case "energie" -> System.out.println("Energie (non utilisée ici) : " + operation); // ignore
                    case "malade" -> this.setMalade(Boolean.parseBoolean(operation.replace("=", "")));
                    case "tempsRestantQuotient" -> applyFloatUpdate("tempsRestantQuotient", operation);
                    default -> System.out.println("Clé inconnue : " + key);
                }
            } catch (Exception e) {
                System.out.println("Erreur lors du traitement de : " + update);
                e.printStackTrace();
            }
        }
    }

    private void applyIntUpdate(String field, String operation) {
        int current = switch (field) {
            case "fatigue" -> getFatigue();
            case "stress" -> getStress();
            case "esperanceNote" -> getEsperanceNote();
            default -> 0;
        };

        int value = Integer.parseInt(operation.substring(1));
        char op = operation.charAt(0);
        switch (op) {
            case '+' -> value = current + value;
            case '-' -> value = current - value;
            case '=' -> value = Integer.parseInt(operation.substring(1));
        }

        switch (field) {
            case "fatigue" -> setFatigue(value);
            case "stress" -> setStress(value);
            case "esperanceNote" -> setEsperanceNote(value);
        }
    }

    private void applyFloatUpdate(String field, String operation) {
        float current = getTempsRestantQuotient();
        float value = Float.parseFloat(operation.substring(1));
        char op = operation.charAt(0);
        switch (op) {
            case '+' -> value = current + value;
            case '-' -> value = current - value;
            case '=' -> value = Float.parseFloat(operation.substring(1));
        }

        setTempsRestantQuotient(value);
    }
    
}

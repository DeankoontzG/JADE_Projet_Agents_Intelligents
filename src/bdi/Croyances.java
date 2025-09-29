package bdi;
import java.util.Map;
import java.util.HashMap;

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

    public static void update(){
        System.out.println("J'update mes croyances");
    }



    
}

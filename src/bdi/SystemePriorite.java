package bdi;

public class SystemePriorite {

    // 1. Travailler
    public static int prioriteTravailler(int esperanceNote, float tempsRestantQuotient, int fatigue) {
        int priorite = (20 - esperanceNote) * 2; // mauvaise note attendue -> travailler
        priorite += (int)((1 - tempsRestantQuotient) * 20); // peu de temps -> travailler
        priorite -= fatigue; // fatigue réduit efficacité
        return Math.max(0, Math.min(priorite, 100));
    }

    // 2. Dormir
    public static int prioriteDormir(int fatigue, int stress, boolean malade) {
        int priorite = fatigue * 2; // fatigue = besoin sommeil
        priorite += stress / 2;     // stress fatigue aussi
        if (malade) priorite += 10; // malade -> besoin dormir
        return Math.min(priorite, 100);
    }

    // 3. Faire du sport avec ses amis
    public static int prioriteSport(int stress, int fatigue, boolean malade) {
        int priorite = stress * 2; // sport utile contre stress
        if (fatigue > 15) priorite -= 5; // trop fatigué -> pas envie
        if (malade) priorite -= 20; // malade -> sport déconseillé
        return Math.max(0, Math.min(priorite, 100));
    }

    // 4. Prendre un café
    public static int prioriteCafe(int fatigue, boolean malade) {
        int priorite = fatigue * 2; // fatigue -> besoin café
        if (malade) priorite -= 5;  // malade -> café pas top
        return Math.max(0, Math.min(priorite, 100));
    }

    // 5. Manger avec maman
    public static int prioriteMangerMaman(int stress, boolean malade) {
        int priorite = 10; // base
        priorite += stress; // ça réconforte
        if (malade) priorite += 10; // soutien familial
        return Math.min(priorite, 100);
    }

    // 6. Co-work avec copains
    public static int prioriteCowork(int esperanceNote, int stress, int fatigue) {
        int priorite = (20 - esperanceNote); // besoin travailler mais en mode social
        priorite += stress / 2;  // parler aide au stress
        if (fatigue > 15) priorite -= 5; // trop fatigué -> pas motivé
        return Math.max(0, Math.min(priorite, 100));
    }
}
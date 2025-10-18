package bdi;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AffichageEtudiant extends JFrame {

    private JLabel tempsRestantLabel;
    private JTextArea croyancesArea;
    private JTextArea intentionsArea;
    private DefaultListModel<String> actionsListModel;

    public AffichageEtudiant() {
        super("Dashboard de l'étudiant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout(10, 10));

        // Temps restant et avancement en haut
        tempsRestantLabel = new JLabel("Temps restant : ");
        add(tempsRestantLabel, BorderLayout.NORTH);

        // Croyances à gauche
        croyancesArea = new JTextArea();
        croyancesArea.setEditable(false);
        croyancesArea.setBorder(BorderFactory.createTitledBorder("Croyances"));
        add(new JScrollPane(croyancesArea), BorderLayout.WEST);

        // Intentions à droite
        intentionsArea = new JTextArea();
        intentionsArea.setEditable(false);
        intentionsArea.setBorder(BorderFactory.createTitledBorder("Intentions"));
        add(new JScrollPane(intentionsArea), BorderLayout.EAST);

        // Actions en bas
        actionsListModel = new DefaultListModel<>();
        JList<String> actionsList = new JList<>(actionsListModel);
        actionsList.setBorder(BorderFactory.createTitledBorder("Actions récentes"));
        add(new JScrollPane(actionsList), BorderLayout.SOUTH);

        // Ajuster les tailles des zones (optionnel)
        croyancesArea.setPreferredSize(new Dimension(200, 300));
        intentionsArea.setPreferredSize(new Dimension(200, 300));
        actionsList.setPreferredSize(new Dimension(580, 100));

        setVisible(true);
    }

    // Mise à jour des croyances
    public void updateCroyances(String text) {
        SwingUtilities.invokeLater(() -> croyancesArea.setText(text));
    }

    // Mise à jour des intentions
    public void updateIntentions(String text) {
        SwingUtilities.invokeLater(() -> intentionsArea.setText(text));
    }

    // Mise à jour des actions (dernières 4-5)
    public void updateActions(List<String> actions) {
        SwingUtilities.invokeLater(() -> {
            actionsListModel.clear();
            for (String action : actions) {
                actionsListModel.addElement(action);
            }
        });
    }

    // Mise à jour du temps restant
    public void updateTempsRestant(String text) {
        SwingUtilities.invokeLater(() -> tempsRestantLabel.setText("Temps restant : " + text));
    }
}

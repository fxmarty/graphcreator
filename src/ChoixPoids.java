import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;
 
public class ChoixPoids extends JDialog implements ActionListener {
    JRadioButton avecPoids;
    JRadioButton sansPoids;
    ButtonGroup groupPoidsOuNon;
    
    JRadioButton oriente;
    JRadioButton nonOriente;
    ButtonGroup groupOrienteOuNon;
    
    boolean choixPoidsFait;
    boolean avecPoidsOuNon;
    boolean orienteOuNon;
	Onglet papa;
    
	public ChoixPoids(Onglet parent, String title) {
        groupPoidsOuNon = new ButtonGroup();
        groupOrienteOuNon = new ButtonGroup();
        papa = parent;
        
        setLocation(300, 200);
 
        JPanel choixAvecPoids = new JPanel(new GridLayout(3, 1));
        JLabel mes = new JLabel("<html>Voulez-vous créer un graphe avec des poids personnalisés ?</html>");
        choixAvecPoids.add(mes);
        
    	avecPoids = new JRadioButton("Arêtes avec poids personnalisés");
    	sansPoids = new JRadioButton("Arêtes ayant toutes le même poids");
    	
    	groupPoidsOuNon.add(avecPoids);
    	groupPoidsOuNon.add(sansPoids);
    	choixAvecPoids.add(avecPoids);
    	choixAvecPoids.add(sansPoids);
        getContentPane().add(choixAvecPoids,BorderLayout.NORTH);

        JPanel choixOrientation = new JPanel(new GridLayout(3,1));
        JLabel mesOrientation = new JLabel("Graphe orienté ?");
        oriente = new JRadioButton("Oui");
        nonOriente = new JRadioButton("Non");
        
        groupOrienteOuNon.add(oriente);
        groupOrienteOuNon.add(nonOriente);
        
        choixOrientation.add(mesOrientation);
        choixOrientation.add(oriente);
        choixOrientation.add(nonOriente);
        getContentPane().add(choixOrientation,BorderLayout.CENTER);
        
        JPanel buttonPane = new JPanel();
        JButton button = new JButton("OK");
        buttonPane.add(button);

        button.addActionListener(this);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }
	
	public boolean choixDuPoids() {
		return GroupButtonUtils.getSelectedButtonText(groupPoidsOuNon).equals("Arêtes avec poids personnalisés");
	}
	
	public boolean choixOrientation() {
		return GroupButtonUtils.getSelectedButtonText(groupOrienteOuNon).equals("Oui");
	}
 

    public void actionPerformed(ActionEvent e) {
        if (GroupButtonUtils.getSelectedButtonText(groupPoidsOuNon) != null 
        		&& GroupButtonUtils.getSelectedButtonText(groupOrienteOuNon) != null) {
	    	avecPoidsOuNon = choixDuPoids();
	    	orienteOuNon = choixOrientation();
	        choixPoidsFait = true;
	        setVisible(false);
	        
	        papa.setVisible(true);
	        dispose();
        }
    }

}
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;


public class Onglet extends JPanel implements ItemListener,ActionListener {

	JToggleButton placerSommet;
    JToggleButton placerArete;
    JToggleButton deplacerSommet;
    ButtonGroup groupAreteouSommet;
    JTextArea resultat;
    JTextArea resultInfo;
    JButton genererGraphe;
    Box panelBoutons;
    CanvasDessin dessin;
    AppliAvecCanvas pere;
    JDialog diag;
	
	public Onglet(AppliAvecCanvas parent){
		panelBoutons = Box.createVerticalBox();
		pere = parent;
		groupAreteouSommet = new ButtonGroup();
		
		placerSommet = new JToggleButton("Placer un sommet");
    	placerArete = new JToggleButton("Placer une arête");
    	deplacerSommet = new JToggleButton("Déplacer un sommet");
    	genererGraphe = new JButton("Générer code du graphe");
    	
    	groupAreteouSommet.add(placerSommet);
    	groupAreteouSommet.add(placerArete);
    	groupAreteouSommet.add(deplacerSommet);
    	
    	panelBoutons.add(placerSommet);
    	panelBoutons.add(Box.createRigidArea(new Dimension(0, 10)));
    	panelBoutons.add(placerArete);
    	panelBoutons.add(Box.createRigidArea(new Dimension(0, 10)));
    	panelBoutons.add(deplacerSommet);
    	panelBoutons.add(Box.createRigidArea(new Dimension(0, 40)));
    	panelBoutons.add(genererGraphe);
    	
    	genererGraphe.addActionListener(this);
	
    	resultat = new JTextArea();
    	resultat.setEditable(false);
    	resultat.setWrapStyleWord(true);
    	resultat.setLineWrap(true);
    	resultat.setColumns(40);
    	
    	resultInfo = new JTextArea();
    	resultInfo.setEditable(false);
    	resultInfo.setWrapStyleWord(true);
    	resultInfo.setLineWrap(true);
    	resultInfo.setColumns(40);
    	
    	placerArete.addItemListener(this);
    	setLayout(new FlowLayout());
	}

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
        	dessin.compteur_arete = 0;
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == genererGraphe) {
			if (pere.listeDadjacence == true && pere.separateurEntreElements != null) {
				dessin.afficherListeDadj(pere.separateurEntreElements);
			}
			
			else if (pere.listeDadjacence == false && pere.separateurEntreElements != null) {
				dessin.afficherMatDadj(pere.separateurEntreElements);
			}
			else {
				JLabel mes = new JLabel("<html>Vous devez indiquer le format de sortie dans les paramètres <br>" +
						"pour réaliser cette opération. </html>");
				diag = new JDialog();
				diag.add(mes,BorderLayout.NORTH);
				
				JPanel buttonPane = new JPanel();
		        JButton button = new JButton("OK");
		        buttonPane.add(button);
		        diag.add(buttonPane, BorderLayout.SOUTH);
		        
		        button.addActionListener(
		        		new ActionListener() {
		        			public void actionPerformed(ActionEvent e) {
		        				diag.setVisible(false);
		        				diag.dispose();
		        			}
		        		}
		        );
		        diag.setLocation(200, 200);
		        diag.pack();
		        diag.setVisible(true);
			}
		}
		
	}
	
}

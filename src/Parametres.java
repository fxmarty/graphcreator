import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
 
public class Parametres extends JDialog implements ActionListener {
	JComboBox choixListeOuMatrice;
	JTextField choixSeparateurEntreElements;
	AppliAvecCanvas papa;
	

    boolean choixFait;
    
	public Parametres(AppliAvecCanvas parent) {
		
		String s1[] = {"Liste d'adjacence", "Matrice d'adjacence"};
		choixListeOuMatrice = new JComboBox(s1);
        
		papa = parent;
		
		if (papa.separateurEntreElements != null) {
			choixSeparateurEntreElements = new JTextField(papa.separateurEntreElements);
		}
		else {
			choixSeparateurEntreElements = new JTextField(10);  
		}
      
        setLocation(300, 200);
        
        JPanel choixListeOuMatrice = new JPanel(new GridLayout(3, 1));
        JLabel mes = new JLabel("<html>Forme de la sortie</html>");
        choixListeOuMatrice.add(mes);
        choixListeOuMatrice.add(this.choixListeOuMatrice);

        getContentPane().add(choixListeOuMatrice,BorderLayout.NORTH);

        JPanel choixSeparateurs = new JPanel(new GridLayout(4,1));
        
        JLabel mesChoixSeparateursElements = new JLabel("Entrez les séparateurs entre éléments que vous souhaitez (e.g. \"[,]\")");
        choixSeparateurs.add(mesChoixSeparateursElements);
        choixSeparateurs.add(choixSeparateurEntreElements);
        
        getContentPane().add(choixSeparateurs,BorderLayout.CENTER);
        
        JPanel buttonPane = new JPanel();
        JButton button = new JButton("OK");
        buttonPane.add(button);

        button.addActionListener(this);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
    }
	
	public boolean choixListeAdjacence() {
		return choixListeOuMatrice.getSelectedItem().equals("Liste d'adjacence");
	}
	
    public void actionPerformed(ActionEvent e) {
        if (choixSeparateurEntreElements.getText().length() == 3
        		&& choixListeOuMatrice.getSelectedItem() != null) {
        	
        	papa.separateurEntreElements = choixSeparateurEntreElements.getText();
        	papa.listeDadjacence = choixListeAdjacence();
        	setVisible(false);
        	dispose();
        }

    }
        
}

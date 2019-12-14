import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class DialogAPropos extends JDialog implements ActionListener{

	public DialogAPropos() {
		JLabel mes = new JLabel("<html>Outil réalisé par Félix Marty <br>" +
				"Version 1.0 <br>" +
				"Licence CC BY-SA <br>" +
				"Pour toute remarque : felix.marty@mines-paristech.fr</html>");
		
		getContentPane().add(mes,BorderLayout.NORTH);
		
		JPanel buttonPane = new JPanel();
        JButton button = new JButton("OK");
        buttonPane.add(button);

        button.addActionListener(this);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		setModal(true);
		setLocation(200, 200);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		setVisible(false);
		dispose();
		
	}

}

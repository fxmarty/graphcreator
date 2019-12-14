import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

public class AppliAvecCanvas extends JFrame implements ActionListener {
    JMenuBar menuBar;
    JMenu menu, submenu;
    JMenuItem menuItemCreerGraphe;
    JMenuItem parametres;
    JMenuItem aPropos;
    JTabbedPane onglets;
    Font font;
    static int nb_onglets;
    
    boolean listeDadjacence;
    String separateurEntreElements;

    public AppliAvecCanvas() {
    	nb_onglets = 0;
    	
    	try {
    		
    		InputStream is = AppliAvecCanvas.class.getResourceAsStream("/NotoSans-Regular.ttf");
    		
    		//File font_file = new File(url);
    		
    		font = Font.createFont(Font.TRUETYPE_FONT, is);
    		
			font = font.deriveFont(18f);
			System.out.print(font.getFontName());
		} catch (Exception e) {
			System.out.print(e.getMessage() + "haha");
		}
    	    	
    	UIManager.put("Button.font", font);
    	UIManager.put("ToggleButton.font", font);
    	UIManager.put("RadioButton.font", font);
    	UIManager.put("CheckBox.font", font);
    	UIManager.put("ColorChooser.font", font);
    	UIManager.put("ComboBox.font", font);
    	UIManager.put("Label.font", font);
    	UIManager.put("List.font", font);
    	UIManager.put("MenuBar.font", font);
    	UIManager.put("MenuItem.font", font);
    	UIManager.put("RadioButtonMenuItem.font", font);
    	UIManager.put("CheckBoxMenuItem.font", font);
    	UIManager.put("Menu.font", font);
    	UIManager.put("PopupMenu.font", font);
    	UIManager.put("OptionPane.font", font);
    	UIManager.put("Panel.font", font);
    	UIManager.put("ProgressBar.font", font);
    	UIManager.put("ScrollPane.font", font);
    	UIManager.put("Viewport.font", font);
    	UIManager.put("TabbedPane.font", font);
    	UIManager.put("Table.font", font);
    	UIManager.put("TableHeader.font", font);
    	UIManager.put("TextField.font", font);
    	UIManager.put("PasswordField.font", font);
    	UIManager.put("TextArea.font", font);
    	UIManager.put("TextPane.font", font);
    	UIManager.put("EditorPane.font", font);
    	UIManager.put("TitledBorder.font", font);
    	UIManager.put("ToolBar.font", font);
    	UIManager.put("ToolTip.font", font);
    	UIManager.put("Tree.font", font);
    	
    	menuBar = new JMenuBar();

    	menu = new JMenu("Fichier");
    	menuBar.add(menu);
    	
    	menuItemCreerGraphe = new JMenuItem("Nouveau graphe");
    	menuItemCreerGraphe.addActionListener(this);
    	menu.add(menuItemCreerGraphe);
    	
    	parametres = new JMenuItem("Paramètres");
    	parametres.addActionListener(this);
    	menu.add(parametres);

    	JMenu menu2 = new JMenu("?");
    	aPropos = new JMenuItem("A propos");
    	menu2.add(aPropos);
    	aPropos.addActionListener(this);
    	
    	menuBar.add(menu2);

    	this.setJMenuBar(menuBar);
    	
    	onglets = new JTabbedPane();
    	getContentPane().add(onglets);
    	
        setTitle("GraphCreator");
        setLocation(100, 100);
        setSize(1600, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setLayout(new FlowLayout());
        setVisible(true);
    }
    
    
    public static void main(String[] args) {
        new AppliAvecCanvas();
        
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==menuItemCreerGraphe) {
	    	Onglet tab = new Onglet(this);
	    	
	    	ChoixPoids dialog = new ChoixPoids(tab, "Type de graphe ?");
	    	dialog.pack();
	    	dialog.setVisible(true);
	    	
	    	CanvasDessin dsn = new CanvasDessin(dialog.avecPoidsOuNon,dialog.orienteOuNon,tab);
	    	tab.dessin = dsn;
	    	
	    	tab.add(tab.panelBoutons);
	    	tab.add(tab.dessin);
	    	
	    	Box panelResultats = Box.createVerticalBox();
	    	panelResultats.add(tab.resultat);
	    	panelResultats.add(Box.createRigidArea(new Dimension(0, 10)));
	    	panelResultats.add(tab.resultInfo);
	    	
	    	tab.add(panelResultats);
	    	nb_onglets = nb_onglets + 1;
	    	onglets.add("Graphe n°" + nb_onglets,tab);
	    	
	    	onglets.setSelectedIndex(nb_onglets - 1);

		}
		
		if (e.getSource()== aPropos) {
			DialogAPropos ap = new DialogAPropos();
			ap.pack();
			ap.setVisible(true);
		}
		
		if (e.getSource()== parametres) {
			Parametres par = new Parametres(this);
			par.pack();
			par.setVisible(true);
			
		}
		
	}
    
}

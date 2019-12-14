import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.event.*;

public class InvisibleTextField extends JTextField
    implements ActionListener, FocusListener, MouseListener, DocumentListener
{
	
	CanvasDessin dessinParent;
	
    public InvisibleTextField(CanvasDessin ceDessin) 
    {
        setColumns( 1 );
        setSize( getPreferredSize() );
        setColumns( 0 );
        addActionListener( this );
        addFocusListener( this );
        addMouseListener( this );
        getDocument().addDocumentListener( this );
        dessinParent = ceDessin;
    }

    public void actionPerformed(ActionEvent e)
    {
        setEditable( false );
    }

    public void focusLost(FocusEvent e)
    {
    	InvisibleTextField tf = (InvisibleTextField)e.getSource();
    	String textFieldValue = tf.getText();
        if (textFieldValue.equals("")) {
        	tf.setText("1");
        }

        Arete cple = dessinParent.mapPoidsAretes.get(tf);
        Sommet s1 = cple.renvoyerSommet1();
        Sommet s2 = cple.renvoyerSommet2();
        
        if (!(dessinParent.mapAreteToTextField.containsKey(new Arete(s1,s2)))) {
        	dessinParent.mapAreteToTextField.put(new Arete(s1.clone(),s2.clone()), tf);
        }
        
        if (dessinParent.oriente == true) { // mise à jour des poids
            s1.aPourNouveauVoisin(s2,Integer.parseInt(tf.getText()));
        }
        
        else {
        	s1.aPourNouveauVoisin(s2,Integer.parseInt(tf.getText()));
        	s2.aPourNouveauVoisin(s1,Integer.parseInt(tf.getText()));
        }

        dessinParent.pere.resultat.setText(Arrays.toString(dessinParent.listeSommets.toArray()));
        dessinParent.pere.resultat.setSize( dessinParent.pere.resultat.getPreferredSize() );
        
    	setEditable( false );
    }

    public void focusGained(FocusEvent e) {}

    public void mouseClicked( MouseEvent e )
    {
        if (e.getClickCount() == 2)
            setEditable( true );
    }

    public void mouseEntered( MouseEvent e ) {}

    public void mouseExited( MouseEvent e ) {}

    public void mousePressed( MouseEvent e ) {}

    public void mouseReleased( MouseEvent e ) {}

    public void insertUpdate(DocumentEvent e)
    {
        updateSize();
    }

    public void removeUpdate(DocumentEvent e)
    {
        updateSize();
    }

    public void changedUpdate(DocumentEvent e) {}

    private void updateSize()
    {
        setSize( getPreferredSize() );
    }
}
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class CanvasDessin extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	List<Sommet> listeSommets; // ressemble substantiellement à une liste d'adjacence
	List<Sommet> listeSommetsDansAretes; // pour faciliter l'affichage graphique des arêtes
	HashMap<InvisibleTextField,Arete> mapPoidsAretes; // relier un TextField à une Arete
	
	HashMap<Arete,InvisibleTextField> mapAreteToTextField; // relie l'arête à son TextField
	List<InvisibleTextField> txtFieldEnDeplacement; // txtField à déplacer
	List<Sommet> voisinDuEnDeplacement; // Sommets voisins du point en déplacement
	boolean deplacementEnCours;
	boolean alreadyDragged; // savoir si la souris a été bougé au cours du déplacement
	
	int xCur; // position courante de la souris
	int yCur;
	int nbArete;
	int compteur_arete;
    boolean avecPoids;
    boolean oriente;
    Onglet pere;
    int nb_sommets;
    
    Sommet sommetEnCoursDeDeplacement; // Sommet courant en déplacement
    Sommet sommetAncien; // Lors du déplacement d'un sommet, ce sont ses anciennes coordonnées
    
    public CanvasDessin(boolean avecPds, boolean orienteOuNon, Onglet papa) {
    	avecPoids = avecPds;
    	oriente = orienteOuNon;
    	
    	setBackground(Color.white);
    	this.addMouseListener(this);
    	this.addMouseMotionListener(this);
    	this.addKeyListener(this);
    	
    	listeSommets = new ArrayList();
    	listeSommetsDansAretes = new ArrayList();
    	mapPoidsAretes = new HashMap<>();
    	deplacementEnCours = false;
    	alreadyDragged = false;
    	
    	mapAreteToTextField = new HashMap<>();
    	txtFieldEnDeplacement = new ArrayList();
    	voisinDuEnDeplacement = new ArrayList();
    	
    	xCur = 0;
    	yCur = 0;
    	nbArete = 0;
    	compteur_arete = 0;
    	pere = papa;
    	nb_sommets = 0;
    	this.setLayout(null);
    }
    
	public Dimension getPreferredSize()
    {
        return (new Dimension(600, 600));
    }
	
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
        	    RenderingHints.KEY_ANTIALIASING,
        	    RenderingHints.VALUE_ANTIALIAS_ON);
 
        Sommet currSommet;
        Sommet currSommet2;
        
        g2.setColor(Color.red);
        
        
        if (compteur_arete % 2 == 1) { // création d'arête en cours
        	g2.setStroke(new BasicStroke(2));
        	Sommet dernier = listeSommetsDansAretes.get(listeSommetsDansAretes.size() - 1);
        	g2.drawLine(dernier.getX(), dernier.getY(), xCur, yCur);
        }

        pere.resultat.setText(Arrays.toString(listeSommets.toArray()));
        pere.resultat.setSize(pere.resultat.getPreferredSize() );
        
        g2.setStroke(new BasicStroke(3));
        
        if (oriente == false) { // afficher les arêtes si non-orienté
        	for (int i = 0; i < listeSommetsDansAretes.size(); i+=2) { 
                if (i+1 < listeSommetsDansAretes.size()){
                	currSommet = (Sommet) (listeSommetsDansAretes.get(i));
                	currSommet2 = (Sommet) (listeSommetsDansAretes.get(i + 1));
                	g2.drawLine(currSommet.getX(), currSommet.getY(),currSommet2.getX(), currSommet2.getY());

                }
            }
        }
        
        if (oriente == true) { // afficher les arêtes si orienté
        	for (int i = 0; i < listeSommetsDansAretes.size(); i+=2) { 
                if (i+1 < listeSommetsDansAretes.size()){
                	currSommet = (Sommet) (listeSommetsDansAretes.get(i));
                	currSommet2 = (Sommet) (listeSommetsDansAretes.get(i + 1));
                	
                	double x1 = currSommet.getX();
                	double y1 = currSommet.getY();
                	double x2 = (currSommet2.getX() + x1)/2;
                	double y2 = (currSommet2.getY() + y1)/2;
                	
                	x2 =  x2 + (x2 - x1)*0.4;
                	y2 = y2 + (y2 - y1)*0.4;
                	
                	double dx = x2 - x1, dy = y2 - y1;
                    double D = Math.sqrt(dx*dx + dy*dy);
                    double xm = D - 12, xn = xm, ym = 8, yn = -8, x;
                    double sin = dy / D, cos = dx / D;

                    x = xm*cos - ym*sin + x1;
                    ym = xm*sin + ym*cos + y1;
                    xm = x;

                    x = xn*cos - yn*sin + x1;
                    yn = xn*sin + yn*cos + y1;
                    xn = x;
					g2.drawLine((int)x2, (int)y2, (int)xm, (int)ym);
                	g2.drawLine((int)x2, (int)y2, (int)xn, (int)yn);
                	g2.drawLine(currSommet.getX(), currSommet.getY(),currSommet2.getX(), currSommet2.getY());
                	
                }
            }  	
        }
        
        
        g2.setStroke(new BasicStroke(10));
        g2.setColor(Color.blue);
        
        for (int i = 0; i < listeSommets.size(); i++) { // afficher les sommets
            currSommet = (Sommet) (listeSommets.get(i));
            g2.drawLine(currSommet.getX(), currSommet.getY(),currSommet.getX(), currSommet.getY());
            currSommet.lbl.setLocation(currSommet.getX(), currSommet.getY() - 25);
        }
  
    }
    
    public void remplacer(Sommet aRemplacer, Sommet nouveauSommet) {
    	for(Sommet s : listeSommetsDansAretes) {
    		if (s.equals(aRemplacer)) {
    			s = nouveauSommet;
    		}
    	}
    }
    
    public static double signe(double a) {
    	if (a >= 0) {
    		return 1;
    	}
    	else {
    		return -1;
    	}
    }
    
    
    // 3 méthodes suivante : pour l'affichage sous forme de code
    public static String aretesVoisinesToStringInformatique(Sommet s,String separateurEntreElements) {
    	String separateurEntreElementsGauche = Character.toString(separateurEntreElements.charAt(0));
    	String separateurEntreElementsMilieu = Character.toString(separateurEntreElements.charAt(1));
    	String separateurEntreElementsDroit = Character.toString(separateurEntreElements.charAt(2));
    	List<Arete> listeAretes = s.aretesVoisines;
    	StringBuffer res = new StringBuffer(listeAretes.size()*20);
    	
    	res.append(separateurEntreElementsGauche);
    	int n = listeAretes.size();
    	for (int i = 0 ; i < n ; i++) {
    		res.append(listeAretes.get(i).pas(s).attribut);
    		if (i != n-1) {
        		res.append(separateurEntreElementsMilieu);
    		}
    	}
    	
    	res.append(separateurEntreElementsDroit);
    	
    	return res.toString();
    }
    
    public void afficherListeDadj(String separateurEntreElements) {
    	String separateurEntreElementsGauche = Character.toString(separateurEntreElements.charAt(0));
    	String separateurEntreElementsMilieu = Character.toString(separateurEntreElements.charAt(1));
    	String separateurEntreElementsDroit = Character.toString(separateurEntreElements.charAt(2));
    	StringBuffer res = new StringBuffer(nb_sommets*nb_sommets*20);
    	res.append(separateurEntreElementsGauche);
    	
    	for (int i = 0 ; i < nb_sommets ; i++) {
    		res.append(separateurEntreElementsGauche);
    		res.append(listeSommets.get(i).attribut);
    		res.append(separateurEntreElementsMilieu);
    		res.append(aretesVoisinesToStringInformatique(
    				listeSommets.get(i), separateurEntreElements));
    		res.append(separateurEntreElementsDroit);
    		if (i != nb_sommets - 1) {
    			res.append(separateurEntreElementsMilieu);
    		}
    	}
    	
    	res.append(separateurEntreElementsDroit);
    	pere.resultInfo.setText(res.toString());
    	pere.resultInfo.setSize( pere.resultInfo.getPreferredSize() );
    }
    
    public void afficherMatDadj(String separateurEntreElements) {
    	String separateurEntreElementsGauche = Character.toString(separateurEntreElements.charAt(0));
    	String separateurEntreElementsMilieu = Character.toString(separateurEntreElements.charAt(1));
    	String separateurEntreElementsDroit = Character.toString(separateurEntreElements.charAt(2));
    	StringBuffer res = new StringBuffer(nb_sommets*nb_sommets*20);
    	res.append(separateurEntreElementsGauche);
    	
    	for (int i = 0 ; i < nb_sommets ; i++) {
    		res.append(separateurEntreElementsGauche);
    		int n = listeSommets.get(i).aretesVoisines.size();
    		int[] valeurs = new int[nb_sommets];    		
       		for (int j = 0 ; j < n ; j++) {
    			if (listeSommets.get(i).aretesVoisines.get(j).poids > 0) {
           			valeurs[listeSommets.get(i).aretesVoisines.get(j).pas(listeSommets.get(i)).attribut] = 
        					listeSommets.get(i).aretesVoisines.get(j).poids;
    			}

    		}
       		
       		for (int j = 0 ; j < nb_sommets ; j++) {
       			res.append(valeurs[j]);
       			
       			if (j != nb_sommets - 1) {
       				res.append(separateurEntreElementsMilieu);
       			}
       		}
       		
       		res.append(separateurEntreElementsDroit);
       		
       		if (i != nb_sommets - 1) {
       			res.append(separateurEntreElementsMilieu);
       		}
    	}
   		res.append(separateurEntreElementsDroit);
   		
   		pere.resultInfo.setText(res.toString());
    	pere.resultInfo.setSize( pere.resultInfo.getPreferredSize() );
	
    }
    
    // GESTION DES EVENEMENTS
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }
 
    @Override
    public void mousePressed(MouseEvent e) {
    	if (GroupButtonUtils.getSelectedButtonText(pere.groupAreteouSommet) == null) {
    		
    	}
    	
    	else if (GroupButtonUtils.getSelectedButtonText(pere.groupAreteouSommet).equals("Placer un sommet")) {
            int x = e.getX();
            int y = e.getY();
            
            Sommet p = new Sommet(x,y,nb_sommets);
            nb_sommets = nb_sommets + 1;
            listeSommets.add(p);
            this.add(p.lbl);
            p.lbl.setLocation(x, y - 25);
            repaint();
    	}
    	
    	else if (GroupButtonUtils.getSelectedButtonText(pere.groupAreteouSommet).equals("Placer une arête")) {
    		if (listeSommets.size() > 0){
	    		int x = e.getX();
	            int y = e.getY();
	            Sommet p = new Sommet(x,y);
	            xCur = x;
	            yCur = y;
	            Sommet plusProche = trouverSommetPlusProche(p, listeSommets);
	            if (Sommet.dist(p,plusProche) <= 30) {
		            listeSommetsDansAretes.add(plusProche);
		            compteur_arete = compteur_arete + 1;
		            repaint();
		            
		            if (compteur_arete % 2 == 1) { // si arête en cours de création, boutons plus cliquables
		        		pere.placerSommet.setEnabled(false);
		        		pere.deplacerSommet.setEnabled(false);
		        		pere.placerArete.setEnabled(false);
		            }
		            
		            if (compteur_arete % 2 == 0) { // si deuxième sommet de l'arête vient d'être placé
		            	
		            	// Les trois boutons sont de nouveaux cliquables. Pendant la création d'arête, on interdit de cliquer.
		        		pere.placerSommet.setEnabled(true);
		        		pere.deplacerSommet.setEnabled(true);
		        		pere.placerArete.setEnabled(true);
		        		
		            	if (avecPoids) { // cas avec poids
		            		InvisibleTextField tf = new InvisibleTextField(this);
			            	tf.addKeyListener(this);
			            	Sommet s1 = listeSommetsDansAretes.get(listeSommetsDansAretes.size() - 1);
			            	Sommet s2 = listeSommetsDansAretes.get(listeSommetsDansAretes.size() - 2);
			            	
			            	mapPoidsAretes.put(tf, new Arete(0,s2,s1));
			            	
			            	if (oriente) {
    			        		
			            		double x1 = s2.getX();
			                	double y1 = s2.getY();
			                	double x2 = (s1.getX() + x1)/2;
			                	double y2 = (s1.getY() + y1)/2;
			                	
			                	x2 =  x2 + (x2 - x1)*0.4;
			                	y2 = y2 + (y2 - y1)*0.4;

			                	double angle = Math.toDegrees(Math.atan(Sommet.coefDir(s1,s2)));
			                	tf.setLocation(
					            			new Point((int)(x2 + 0.2*signe(angle)*Math.abs(angle) - 7),(int)y2 + 10));
			                	
			            	}
			            	
			            	
			            	else {
			            			double angle = Math.toDegrees(Math.atan(Sommet.coefDir(s1,s2)));
			            			tf.setLocation(
					            			new Point((int)((s1.getX() + s2.getX())/2 + 0.2*signe(angle)*Math.abs(angle) - 7),
					            					(s1.getY() + s2.getY())/2 + 10));
			            	}
			            	
			            	
			            	this.setLayout(null); // Pour placement des étiquettes des poids si fenêtre redimensionnée
			            	this.add(tf);
		                    tf.requestFocusInWindow();
		                    tf.setVisible(true);
		            	}
		            	
		            	else { // cas sans poids
		            		Sommet s1 = listeSommetsDansAretes.get(listeSommetsDansAretes.size() - 2);
			            	Sommet s2 = listeSommetsDansAretes.get(listeSommetsDansAretes.size() - 1);
			            	
			            	if (oriente == true) {
				            	s1.aPourNouveauVoisin(s2,1);
			            	}
			            	else {
			            		s1.aPourNouveauVoisin(s2,1);
			            		s2.aPourNouveauVoisin(s1,1);
			            	}
			            	
			            	pere.resultat.setText(Arrays.toString(listeSommets.toArray()));
			            	pere.resultat.setSize( pere.resultat.getPreferredSize() );
		            	}
		            	
	                    nbArete = nbArete + 1;
		            }
	            }
	            
	            else { // clique trop loin d'un sommet pour pouvoir créer une arête
	            	this.requestFocusInWindow();
	            }
    		}
    		
            if (e.getClickCount() == 1
            		&& compteur_arete % 2 == 1)
            {
                this.requestFocusInWindow();
            }
    	}
    	
    	// cas où déplacement de sommet en cours (mousePressed)
    	else if (listeSommets.size() > 0 
    			&& GroupButtonUtils.getSelectedButtonText(pere.groupAreteouSommet).equals("Déplacer un sommet")) {
            Sommet p = new Sommet(e.getX(),e.getY());
            Sommet plusProche = trouverSommetPlusProche(p, listeSommets);
            
            if (Sommet.dist(p,plusProche) <= 30) {
                xCur = e.getX();
                yCur = e.getY();
                sommetEnCoursDeDeplacement = plusProche;
                sommetAncien = plusProche.clone();
                deplacementEnCours = true;
                
                for (int i = 0; i < listeSommetsDansAretes.size() ; i = i + 2) {
                	if (i + 1 < listeSommetsDansAretes.size()) {
                    	if (listeSommetsDansAretes.get(i).equals(plusProche)) {
                    		
                    		txtFieldEnDeplacement.add(mapAreteToTextField.get(
                    				new Arete(plusProche.clone(),listeSommetsDansAretes.get(i+1).clone())
                    				)
                    				);
                    		
                    		Sommet s = listeSommetsDansAretes.get(i+1).clone();
                    		
                    		voisinDuEnDeplacement.add(s);
                    		
                    		s.aretesVoisines.add(new Arete(plusProche.clone(),listeSommetsDansAretes.get(i+1).clone()));
                    		
                    	}
                    	
                    	else if (listeSommetsDansAretes.get(i+1).equals(plusProche)) {
                    		
                    		txtFieldEnDeplacement.add(mapAreteToTextField.get(
                    				new Arete(listeSommetsDansAretes.get(i),plusProche)
                    				)
                    				);
                    		
                    		Sommet s = listeSommetsDansAretes.get(i).clone();
                    		
                    		voisinDuEnDeplacement.add(s);
                    		
                    		s.aretesVoisines.add(new Arete(listeSommetsDansAretes.get(i),plusProche));
                    		
                    	}
                	}
                }
            }
    	}

    }
 
    
    /*
     Evènement lorsque souris relachée, particulièrement lorsqu'un sommet a fini d'être déplacé
     pour mettre à jour les nouvelles coordonnées
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    	if (listeSommets.size() > 0 
    			&& GroupButtonUtils.getSelectedButtonText(pere.groupAreteouSommet).equals("Déplacer un sommet")) {
    		
    		// cas où le sommet est déplacé sans mouvement de souris (copie du code de mouseDragged)
    		if (alreadyDragged == false && deplacementEnCours == true) {
		        Sommet p = new Sommet(e.getX(),e.getY());

		        remplacer(sommetAncien, p); // mise à jour de listeSommetsDansAretes
		        
		        sommetEnCoursDeDeplacement.x = e.getX();
		        sommetEnCoursDeDeplacement.y = e.getY();
		        
		        if (avecPoids == true) {
		        	Sommet s1 = new Sommet(e.getX(),e.getY());
		        	
			        for (int i = 0 ; i < txtFieldEnDeplacement.size() ; i++) { // déplacement des txtField
			        	
			        	Sommet s2 = voisinDuEnDeplacement.get(i);
			        	
			        	if (oriente) {
			        		if (s2.aretesVoisines.get(0).s1.equals(sommetAncien) ) {
			        		
		            		double x1 = s2.getX();
		                	double y1 = s2.getY();
		                	double x2 = (s1.getX() + x1)/2;
		                	double y2 = (s1.getY() + y1)/2;
		                	
		                	x2 =  x2 - (x2 - x1)*0.4;
		                	y2 = y2 - (y2 - y1)*0.4;

		                	double angle = Math.toDegrees(Math.atan(Sommet.coefDir(s1,s2)));
		                	txtFieldEnDeplacement.get(i).setLocation(
				            			new Point((int)(x2 + 0.2*signe(angle)*Math.abs(angle) - 7),(int)y2 + 10));
		                	
			        		}
			        		
			        		else {
			        			double x1 = s2.getX();
			                	double y1 = s2.getY();
			                	double x2 = (s1.getX() + x1)/2;
			                	double y2 = (s1.getY() + y1)/2;
			                	
			                	x2 =  x2 + (x2 - x1)*0.4;
			                	y2 = y2 + (y2 - y1)*0.4;
			                	
			                	double angle = Math.toDegrees(Math.atan(Sommet.coefDir(s1,s2)));
			                	txtFieldEnDeplacement.get(i).setLocation(
					            			new Point((int)(x2 + 0.2*signe(angle)*Math.abs(angle) - 7),(int)y2 + 10));
			                	}
			        		}
		            	
		            	
		            	else {
		            			double angle = Math.toDegrees(Math.atan(Sommet.coefDir(s1,s2)));

		            			txtFieldEnDeplacement.get(i).setLocation(
				            			new Point((int)((s1.getX() + s2.getX())/2 + 0.2*signe(angle)*Math.abs(angle) - 7),
				            					(s1.getY() + s2.getY())/2 + 10));
           		
		            	}
			        }
		        }
    		}
			
    		// mise à jour de listeSommets
    		for (Sommet s : listeSommets) {
    			if (s.x == sommetAncien.x && s.y == sommetAncien.y) {
    				s.x = sommetEnCoursDeDeplacement.x;
    				s.y = sommetEnCoursDeDeplacement.y;
    			}
    			for (Arete areteVoisin : s.aretesVoisines) {
    				areteVoisin.remplacer(sommetAncien,sommetEnCoursDeDeplacement);
    			}
    			
    		}
    		
    		// mise à jour de mapAreteToTextField
    		for (Sommet voisin : voisinDuEnDeplacement ){
    			
    			if (mapAreteToTextField.containsKey(new Arete(voisin,sommetAncien))) {
    				InvisibleTextField tf = mapAreteToTextField.get(new Arete(voisin,sommetAncien));
    				mapAreteToTextField.put(
    						new Arete(voisin.clone(),sommetEnCoursDeDeplacement.clone()), tf);
    				mapAreteToTextField.remove(new Arete(voisin,sommetAncien));
    				
    			}
    			
    			else if (mapAreteToTextField.containsKey(new Arete(sommetAncien,voisin))) {
    				InvisibleTextField tf = mapAreteToTextField.get(new Arete(sommetAncien,voisin));
    				mapAreteToTextField.put(
    						new Arete(sommetEnCoursDeDeplacement.clone(),voisin.clone()), tf);
    				mapAreteToTextField.remove(new Arete(sommetAncien,voisin));
    				
    			}
    		
    		
    		}
    		repaint();
    		sommetEnCoursDeDeplacement = null;
    		txtFieldEnDeplacement = new ArrayList();
        	voisinDuEnDeplacement = new ArrayList();
        	deplacementEnCours = false;
        	alreadyDragged = false;
    		
    	}
    }
 
    @Override
    public void mouseEntered(MouseEvent e) {
    }
 
    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    
    Sommet trouverSommetPlusProche(Sommet p, List<Sommet> listeSommets){
    	double distMin = 999999;
    	double distCur;
    	Sommet sommetPlusProche = null;
    	for (int i = 0; i < listeSommets.size(); i++) {
            Sommet currSommet = (Sommet) (listeSommets.get(i));
            distCur = Math.sqrt(Math.pow(p.getX() - currSommet.getX(), 2) + Math.pow(p.getY() - currSommet.getY(), 2));
            if (distCur < distMin) {
            	distMin = distCur;
            	sommetPlusProche = currSommet;
            }
        }
    	return sommetPlusProche;
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
    	if (listeSommets.size() > 0 
    			&& GroupButtonUtils.getSelectedButtonText(pere.groupAreteouSommet).equals("Déplacer un sommet") &&
    			deplacementEnCours == true ){
	        
    		if (e.getX() < getPreferredSize().getHeight() && e.getX() > 0 && e.getY() > 0 
    				&& e.getY() < getPreferredSize().getWidth()) { // si l'on reste dans la fenêtre
		        Sommet p = new Sommet(e.getX(),e.getY());
		        alreadyDragged = true;
		        remplacer(sommetEnCoursDeDeplacement, p);
		        
		        sommetEnCoursDeDeplacement.x = e.getX();
		        sommetEnCoursDeDeplacement.y = e.getY();
		        
		        if (avecPoids == true) {
	        		
		        	Sommet s1 = new Sommet(e.getX(),e.getY());
		        	
		        	for (int i = 0 ; i < txtFieldEnDeplacement.size() ; i++) {

		        	Sommet s2 = voisinDuEnDeplacement.get(i);
		        	
		        	if (oriente) {
		        		if (s2.aretesVoisines.get(0).s1.equals(sommetAncien) ) {
		        		
	            		double x1 = s2.getX();
	                	double y1 = s2.getY();
	                	double x2 = (s1.getX() + x1)/2;
	                	double y2 = (s1.getY() + y1)/2;
	                	
	                	x2 =  x2 - (x2 - x1)*0.4;
	                	y2 = y2 - (y2 - y1)*0.4;

	                	double angle = Math.toDegrees(Math.atan(Sommet.coefDir(s1,s2)));
	                	System.out.println(angle);
	                	txtFieldEnDeplacement.get(i).setLocation(
	    	            			new Point((int)(x2 + 0.2*signe(angle)*Math.abs(angle) - 7),(int)(y2 + 10)));
	                	
		        		}
		        		
		        		else {
		        			double x1 = s2.getX();
		                	double y1 = s2.getY();
		                	double x2 = (s1.getX() + x1)/2;
		                	double y2 = (s1.getY() + y1)/2;
		                	
		                	x2 =  x2 + (x2 - x1)*0.4;
		                	y2 = y2 + (y2 - y1)*0.4;
		                	     	
		                	double angle = Math.toDegrees(Math.atan(Sommet.coefDir(s1,s2)));
		                	System.out.println(angle);   
		                	
			                txtFieldEnDeplacement.get(i).setLocation(
			                		new Point((int)(x2 + 0.2*signe(angle)*Math.abs(angle) - 7),(int)(y2 + 10)));
			        		}
		        		}
	            	
	            	
	            	else {
	            		double angle = Math.toDegrees(Math.atan(Sommet.coefDir(s1,s2)));
	            		System.out.println(angle); 
	                	txtFieldEnDeplacement.get(i).setLocation(
	    	            			new Point((int)((s1.getX() + s2.getX())/2 + 0.2*signe(angle)*Math.abs(angle) - 7),
	    	            					(int)((s1.getY() + s2.getY())/2 + 10)));

	            	}
		        	
		        	}
		        	
		        }
		        repaint();
	        }
	        	
    	}
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
    	// cas de création d'une arête, et qu'un seul sommet n'a été cliqué
    	if (compteur_arete % 2 == 1 
    			&& GroupButtonUtils.getSelectedButtonText(pere.groupAreteouSommet).equals("Placer une arête")) {	
    		xCur = e.getX();
    		yCur = e.getY();
    		repaint();
    	}
    }

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) { // lorsque l'on clique sur Enter dans un txtField, notamment
			this.requestFocusInWindow();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {		
	}

	@Override
	public void keyTyped(KeyEvent e) {		
	}
    
}

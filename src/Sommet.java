import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;


public class Sommet implements Cloneable {
	int x;
    int y;
    List<Arete> aretesVoisines;
    int attribut;
    JLabel lbl;
 
    public Sommet(int x, int y) {
        this.x = x;
        this.y = y;
        aretesVoisines = new ArrayList();
    }
    
    public Sommet(int x, int y, int num) {
        this.x = x;
        this.y = y;
        aretesVoisines = new ArrayList();
    	attribut = num;
    	lbl = new JLabel(String.valueOf(attribut));
    	lbl.setSize(20, 20);
    }
    
    public static double coefDir(Sommet s1, Sommet s2) {
    	return - ((double)(s1.y - s2.y))/(s1.x-s2.x);
    }

    public void ajouterVoisin(Sommet voisin, int pds) {
    	aretesVoisines.add(new Arete(pds, this, voisin));
    }
 
    public int getX() {
        return x;
    }
 
    public int getY() {
        return y;
    }
    
    public void aPourNouveauVoisin(Sommet s2, int pds) {
    	int index_s2 = aretesVoisines.indexOf(s2);
    	if (index_s2 >= 0) {
        	aretesVoisines.get(index_s2).updatePoids(pds);
    	}
    	
    	else {
    		this.ajouterVoisin(s2, pds);
    	}
    	

    }
 
    public void setX(int x) {
        this.x = x;
    }
 
    public void setY(int y) {
        this.y = y;
    }
    
    public static double dist(Sommet s1, Sommet s2) {
    	return Math.sqrt(Math.pow(s1.getX()-s2.getX(), 2) + Math.pow(s1.getY()-s2.getY(), 2));
    }
    
    @Override
    public String toString(){
    	return "[[" + x + ";" + y + "];" + aretesVoisines.toString() + "]";
    }
    
    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Sommet)) {
        	return false;
        }
        else {
        	if (this.x == ((Sommet) other).getX() && this.y == ((Sommet) other).getY()) {
        		return true;
        	}
        	else {
        		return false;
        	}
        }
    }
    
    @Override
    public int hashCode() {
        return (x + y)*(x+y+1)/2;

    }
    
    @Override
    public Sommet clone() {
        return new Sommet(this.getX(),this.getY());
    }
}

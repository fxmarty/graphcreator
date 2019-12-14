
public class Arete {
	Sommet s1;
	Sommet s2;
	int poids;
	
	public Arete(int pds, Sommet s1, Sommet s2) {
		poids = pds;
		this.s1 = s1;
		this.s2 = s2;
	}
	
	public Arete(Sommet destination, int pds) {
		s2 = destination;
		poids = pds;
	}
	
	public Arete(Sommet s1, Sommet s2) {
		this.s1 = s1;
		this.s2 = s2;
	}
	
	void updatePoids(int pds) {
		poids = pds;
	}
	
	Sommet pas(Sommet p) {
		if (s1.equals(p)) {
			return s2;
		}
		else {
			return s1;
		}
	}
	
	Sommet renvoyerSommet1() {
		return s1;
	}
	
	Sommet renvoyerSommet2() {
		return s2;
	}
	
	void remplacer(Sommet s1, Sommet s2) {
		if (this.s1.equals(s1)) {
			this.s1 = s2;
		}
		if (this.s2.equals(s1)) {
			this.s2 = s2;
		}
	}
	
    @Override
    public String toString(){
    	return "((" + s1.getX() + ";" + s1.getY() + ");(" + s2.getX() + ";" + s2.getY() + "); pds:" + poids + "))";
    }
    
    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Arete)) {
        	return false;
        }
        else {
        	if (this.s1.equals( ((Arete) other).s1) && (this.s2.equals( ((Arete) other).s2) )) {
        		return true;
        	}
        	
        	else {
        		return false;
        	}
        }
    }
    
    @Override
    public int hashCode() {
    	int result = 19 * s1.x + s1.y;
    	result = 19 * result + s2.x;
    	result = 19 * result + s2.y;
    	return result;
    }
}

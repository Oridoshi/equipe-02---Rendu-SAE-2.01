/* SAE 2.01 
 * @author  : LEFEVRE Donovan, SA Mateo, DUNET Tom, LE BRETON Kyllian
 * version  : 2.01 
 * date     : 16/06
 */

package metier;

import java.util.*;
import controleur.Controleur;

public class Pioche
{
	private final int NB_CARTE = 10;

	private List<Carte> pioche;
	private int indice;
	private Controleur ctrl;

	public Pioche(Controleur ctrl)
	{
		this();
		this.ctrl = ctrl;
	}

	public Pioche()
	{
		this.pioche = new ArrayList<Carte>();
		this.indice = 0;
	}

	public Carte getSuivant() { return this.pioche.get(indice++); }

	public List<Carte> getPioche() { return this.pioche; }

	public Carte getCarte(int indice) { return this.pioche.get(indice); }

	//Remplir aleatoirement
	public void remplir()
	{
		this.pioche.clear();

		ArrayList<String> image = new ArrayList<String> ();

		image.add("CNoirJaune");
		image.add("CNoirRose");
		image.add("CNoirVert");
		image.add("CNoirBrun");
		image.add("CNoirJoker");
			
		image.add("CBlancJaune");
		image.add("CBlancRose");
		image.add("CBlancVert");
		image.add("CBlancBrun");
		image.add("CBlancJoker");

		Collections.shuffle(image);

		for(int i = 0; i < NB_CARTE; i++)
		{
			this.pioche.add(new Carte(image.get(i)));
		}
	}

	//remplir avec des cartes données
	public void remplir(String[] tab)
	{
		this.pioche.clear();

		for(int cpt=0; cpt<tab.length; cpt++)
		{
			this.pioche.add( new Carte(tab[cpt]));
		}
	}

	public void piocher() 
	{ 
		this.pioche.remove(0);
		this.ctrl.getMetier().incNumTour();
		
		if(this.ctrl.getModeDuo()){this.ctrl.getMetier(2).incNumTour();}
			this.ctrl.event();
		
	}

	//Permet de savoir si le packet est "vide" ou la derniere carte noir a ete pioché
	public boolean estFini()
	{
		if(this.pioche.size() == 1 ) 
			return true;

		int test = 0;
		for(int cpt=0; cpt<this.pioche.size(); cpt++)
		{
			if( this.pioche.get(cpt).getImage().startsWith("CNoir") )
				test++;
			if( test > 1 ) 
				return false;
		}
		if(this.pioche.get(0).getImage().startsWith("CNoir"))
			return true;

		return false;
	}

}
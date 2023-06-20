/* SAE 2.01 
 * @author  : LEFEVRE Donovan, SA Mateo, DUNET Tom, LE BRETON Kyllian
 * version  : 2.01 
 * date     : 16/06
 */

package metier;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

public class Joueur
{
	private static int nbJoueur;
	private int id;
	private List<Ile> lstIleVisitee;
	private List<Region> lstRegionVisitee;
	private int bonus;
	private Color couleur;

	public Joueur(Color couleur)
	{
		this.id = nbJoueur++;
		this.lstIleVisitee = new ArrayList<Ile>();
		this.lstRegionVisitee = new ArrayList<Region>();
		this.bonus = 0;	
		this.couleur = couleur;		
	}

	public int       getBonus()      { return this.bonus; }
	public int       getID()         { return this.id; }
	public List<Ile> getIleVisitee() { return this.lstIleVisitee; }
	public Color     getColor()      { return this.couleur; }

	public void addRegionVisite(Region region) 
	{ 
		if (!this.lstRegionVisitee.contains(region)) 
		{ 
			this.lstRegionVisitee.add(region); 
		} 
	}

	public void addIleVisite(Ile ile)   {  if (!this.lstIleVisitee.contains(ile)) { this.lstIleVisitee.add(ile); } }
	public void addBonus    (int bonus) { this.bonus += bonus;}

	public int compterPoints()
	{
		int ileParRegionMax = 0;
		int ileDansRegion = 0;
		
		for (Region region : this.lstRegionVisitee)
		{
			for (Ile ile : this.lstIleVisitee)
			{
				if (region.aIle(ile) ) 
				{ 
					ileDansRegion += ile.getBonus();
				}			
			}

			if (ileDansRegion > ileParRegionMax) { ileParRegionMax = ileDansRegion;}
			ileDansRegion = 0;
		}
		return this.lstRegionVisitee.size() * ileParRegionMax;
	}
}
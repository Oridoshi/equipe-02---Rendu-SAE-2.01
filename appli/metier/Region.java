/* SAE 2.01 
 * @author  : LEFEVRE Donovan, SA Mateo, DUNET Tom, LE BRETON Kyllian
 * version  : 2.01 
 * date     : 16/06
 */

package metier;

import java.util.ArrayList;

public class Region 
{
	private static int nbRegion = 0;

	private int            id;
	private String         nom;
	private ArrayList<Ile> ensIles;

	public Region(String nom)
	{
		this.id = ++nbRegion;
		this.nom = nom;
		this.ensIles = new ArrayList<Ile>();
	}

	public String         getNom()     { return this.nom; }
	public int            getId()      { return this.id; }
	public ArrayList<Ile> getEnsIles() { return this.ensIles; }

	public boolean aIle(Ile ile)
	{
		if (this.ensIles.contains(ile)) 
			return true;
		return false;
	}

	public void addIle(Ile s) { this.ensIles.add(s); }


}
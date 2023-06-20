/* SAE 2.01 
 * @author  : LEFEVRE Donovan, SA Mateo, DUNET Tom, LE BRETON Kyllian
 * version  : 2.01 
 * date     : 16/06
 */

package metier;

import java.awt.Color;

public class Route
{
	private Ile   s1;
	private Ile   s2;
	private int   valeur;
	private Color couleur;

	public Route(Ile s1, Ile s2)
	{
		this.s1      = s1;
		this.s2      = s2;
		this.valeur  = 0;
		this.couleur = Color.BLACK;
	}

	public Ile    getIle1   () {return this.s1; }
	public Ile    getIle2   () {return this.s2; }
	public int    getValeur () {return this.valeur;  }
	public Color  getColor  () {return this.couleur; }

	// Modification de la couleur de l'arête
	public void setCouleur( Color couleur ) { this.couleur = couleur; }

	// Attribution d'un bonus à l'arête
	public void setBonus(int valeur, Color couleurBonus)
	{
		this.valeur  = valeur;
		this.couleur = couleurBonus;
	}
}
/* SAE 2.01 
 * @author  : LEFEVRE Donovan, SA Mateo, DUNET Tom, LE BRETON Kyllian
 * version  : 2.01 
 * date     : 16/06
 */

package metier;

public class Carte
{
	public static String[] CARTE = new String[] {
	"CNoirJoker",  "CNoirJaune",  "CNoirRose",  "CNoirVert",  "CNoirBrun",
	"CBlancJoker", "CBlancJaune", "CBlancRose", "CBlancVert", "CBlancBrun"
	};
	
	private String couleur;
	private String image;

	public Carte(String image)
	{
		this.image = image;

		switch(this.image)
		{
			case "CBlancBrun"  -> this.couleur = "Brun";
			case "CNoirBrun"   -> this.couleur = "Brun";
			case "CBlancRose"  -> this.couleur = "Rose";
			case "CNoirRose"   -> this.couleur = "Rose";
			case "CBlancVert"  -> this.couleur = "Vert";
			case "CNoirVert"   -> this.couleur = "Vert";
			case "CBlancJaune" -> this.couleur = "Jaune";
			case "CNoirJaune"  -> this.couleur = "Jaune";
			case "CNoirJoker"  -> this.couleur = "Joker";
			case "CBlancJoker" -> this.couleur = "Joker";
		}
	}

	public String getImage() { return this.image; }

	public String getCouleur() { return this.couleur; }

	public boolean equals(Carte carte)
	{
		if( this.image.equals(carte.getImage()) )
			return true;
		return false;
	}
}
/* SAE 2.01 
 * @author  : LEFEVRE Donovan, SA Mateo, DUNET Tom, LE BRETON Kyllian
 * version  : 2.01 
 * date     : 16/06
 */

package metier;

public class Ile
{
	private static int nbIles = 0;

	private int     id;
	private int     posX;
	private int     posY;
	private int     posXImage;
	private int     posYImage;
	private String  nom;
	private boolean isSelected;
	private String  couleur;
	private boolean mouvementPossible;
	private int     bonus;

	public Ile(String nom, String couleur, int posX, int posY, int posXImage, int posYImage)
	{
		this.id         = ++nbIles;
		this.nom        = nom;
		this.couleur    = couleur;
		this.posX       = posX;
		this.posY       = posY;
		this.posXImage  = posXImage;
		this.posYImage  = posYImage;
		this.bonus      = 1;

		this.isSelected = false;
	}
	
	public int     getId  ()       {return this.id         ; }
	public int     getPosX()       {return this.posX       ; }
	public int     getPosY()       {return this.posY       ; }
	public int     getPosXImage()  {return this.posXImage  ; }
	public int     getPosYImage()  {return this.posYImage  ; }
	public boolean getIsSelected() {return this.isSelected ; }
	public String  getNom()        {return this.nom        ; }
	public String  getCouleurIle() {return this.couleur    ; }
	public int     getBonus()      {return this.bonus      ; }

	public void setIsSelected(boolean select) { this.isSelected = select; }
	public void setBonus     (int     bonus ) { this.bonus = bonus; }

	public boolean     getMouvementPossible ()                {return this.mouvementPossible;}
	public void        setMouvementPossible (boolean possible){this.mouvementPossible = possible;}
	public static void autoIncrementeRemise0()                {Ile.nbIles = 0;}

}

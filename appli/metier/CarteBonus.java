/* SAE 2.01 
 * @author  : LEFEVRE Donovan, SA Mateo, DUNET Tom, LE BRETON Kyllian
 * version  : 2.01 
 * date     : 16/06
 */

package metier;

public class CarteBonus
{
	private boolean estActivee;
	private boolean isSelected;
	private String  image;

	public CarteBonus(String image)
	{
		this.image      = image;
		this.estActivee = false;
		this.isSelected = false;
	}

	public String  getImage()      { return this.image; }
	public boolean getEstActivee() { return this.estActivee; }
	public boolean getIsSelected() { return this.isSelected; }

	public void setIsSelected(boolean isSelected) { this.isSelected = isSelected; }
	public void setEstActivee(boolean estActivee) { this.estActivee = estActivee; }

	public boolean equals(CarteBonus carte)
	{
		if(this.image == carte.getImage())
			return true;
		return false;
	}
}
/* SAE 2.01 
 * @author  : LEFEVRE Donovan, SA Mateo, DUNET Tom, LE BRETON Kyllian
 * version  : 2.01 
 * date     : 16/06
 */

package metier;

import controleur.Controleur;

import java.awt.Color;

public class Scenario
{
	private Controleur ctrl;
	private String[] pioche;
	private CarteBonus carteBonus;
	private CarteBonus carteBonus2;

	public Scenario(Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.pioche = new String[10];
		this.carteBonus = null;
		this.carteBonus2 = null;
	}

	public String[] getPioche(){ return this.pioche; }

	public void activerCarteBonus()
	{ 
		this.carteBonus.setEstActivee(false); 
		if (ctrl.getModeDuo()) { this.carteBonus2.setEstActivee(false);}
	}

	public CarteBonus getCarteBonus() { return this.carteBonus; }

	public void lancerScenario(int nb)
	{
		//Acces libre solo
		if (nb == 0 )
		{
			this.ctrl.newJeuSolo();
			this.ctrl.activerEvent( false, false, false, false);

			this.ctrl.getMetier().setCouleur( Color.RED );

			this.pioche  = new String[] {Carte.CARTE[0], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0] };
			String[] tab = new String[this.pioche.length];

			for (int cpt=0 ; cpt<this.pioche.length ; cpt++)
			{
				tab[cpt] = this.pioche[cpt];
			}

			this.ctrl.getMetier().getPioche().remplir( tab );
		}

		//acces libre solo + event
		if (nb == 1 )
		{
			this.ctrl.newJeuSolo();
			this.ctrl.activerEvent( true, true, true, true);

			this.ctrl.getMetier().setCouleur( Color.RED );

			this.pioche  = new String[] {Carte.CARTE[0], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0] };
			String[] tab = new String[this.pioche.length];

			for (int cpt=0 ; cpt<this.pioche.length ; cpt++)
			{
				tab[cpt] = this.pioche[cpt];
			}

			this.ctrl.getMetier().getPioche().remplir( tab );
		}

		//Solo : Extremité + Cycle + Changement de couleur + Passage sur une ligne déjà colorier + Bonus Passage
		if (nb == 2 )
		{
			this.ctrl.newJeuSolo();
			this.ctrl.activerEvent( false, false, false, true);

			this.ctrl.getMetier().setCouleur( Color.RED );

			this.pioche  = new String[] {Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0],Carte.CARTE[5], Carte.CARTE[0], Carte.CARTE[0] };
			String[] tab = new String[this.pioche.length];

			for (int cpt=0 ; cpt<this.pioche.length ; cpt++)
			{
				tab[cpt] = this.pioche[cpt];
			}

			this.ctrl.getMetier().getPioche().remplir( tab );

			this.ctrl.getMetier().colorierArete("4", "9"  );
			this.ctrl.getMetier().colorierArete("9", "13" );
			this.ctrl.getMetier().colorierArete("13", "15");
			this.ctrl.getMetier().colorierArete("15", "8" );

			this.carteBonus = new CarteBonus("BonusVoieDouble");
			this.carteBonus.setEstActivee(true);
			this.ctrl.getMetier().setCarteBonus(this.carteBonus);
			this.activerCarteBonus();
		}

		//Solo : Croisement 2 couleurs + Bonus Croisement + Cycle 2 couleurs 
		if (nb == 3 )
		{
			this.ctrl.newJeuSolo();
			this.ctrl.activerEvent( false, false, false, true);

			this.ctrl.getMetier().setCouleur( Color.RED );

			this.pioche  = new String[] {Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0] };
			String[] tab = new String[this.pioche.length];

			for (int cpt=0 ; cpt<this.pioche.length ; cpt++)
			{
				tab[cpt] = this.pioche[cpt];
			}

			this.ctrl.getMetier().getPioche().remplir( tab );

			this.ctrl.getMetier().colorierArete("4", "9");
			this.ctrl.getMetier().colorierArete("9", "13");
			this.ctrl.getMetier().colorierArete("13", "17");
			this.ctrl.getMetier().colorierArete("6", "7");

			this.carteBonus = new CarteBonus("BonusCroisement");
			this.carteBonus.setEstActivee(true);
			this.ctrl.getMetier().setCarteBonus(this.carteBonus);
			this.activerCarteBonus();
		}

		//Solo : Dernière carte noir + Fin
		if( nb == 4 )
		{
			this.ctrl.newJeuSolo();
			this.ctrl.activerEvent( false, false, false, false);
			
			this.ctrl.getMetier().setCouleur( Color.RED );

			String[] tab = new String[] {Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[5], Carte.CARTE[6], Carte.CARTE[7], Carte.CARTE[8], Carte.CARTE[9] };
			this.ctrl.getMetier().getPioche().remplir( tab );

			this.ctrl.getMetier().colorierArete("4", "3"  );
			this.ctrl.getMetier().colorierArete("3", "9"  );
			this.ctrl.getMetier().colorierArete("9", "13" );
			this.ctrl.getMetier().colorierArete("13", "14");
			this.ctrl.getMetier().colorierArete("14", "16");

			tab = new String[] {Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[6], Carte.CARTE[8], Carte.CARTE[9] };
			this.ctrl.getMetier().getPioche().remplir( tab );
			
			this.ctrl.getMetier().colorierArete("8", "15" );
			this.ctrl.getMetier().colorierArete("8", "18" );
			this.ctrl.getMetier().colorierArete("18", "19");
			this.ctrl.getMetier().colorierArete("19", "20");
			this.ctrl.getMetier().colorierArete("15", "13");
			this.ctrl.getMetier().colorierArete("13", "12");
		}
		
		//Solo : Voie bonus + Bonus double desservance + Bonus Ile double
		if (nb == 5 )
		{
			this.ctrl.newJeuSolo();
			this.ctrl.activerEvent( false, false, false, true);

			this.ctrl.getMetier().setCouleur( Color.RED );

			this.pioche  = new String[] {Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0], };
			String[] tab = new String[this.pioche.length];

			for (int cpt=0 ; cpt<this.pioche.length ; cpt++)
			{
				tab[cpt] = this.pioche[cpt];
			}

			this.ctrl.getMetier().getPioche().remplir( tab );

			this.ctrl.getMetier().colorierArete("4", "9");
			this.ctrl.getMetier().colorierArete("9", "13");

			this.carteBonus = new CarteBonus("BonusX2");
			this.carteBonus.setEstActivee(true);
			this.ctrl.setCarteBonus(this.carteBonus);
			this.ctrl.getMetier().setCarteBonus(this.carteBonus);
		}

		//Accès libre Duo
		if (nb == 6 )
		{
			this.ctrl.newJeuDuo();
			this.ctrl.activerEvent( false, false, false, false);

			this.ctrl.getMetier(1).setCouleur( Color.RED );
			this.ctrl.getMetier(2).setCouleur( Color.BLUE );

			this.pioche  = new String[] {Carte.CARTE[0], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0] };
			String[] tab = new String[this.pioche.length];

			for (int cpt=0 ; cpt<this.pioche.length ; cpt++)
			{
				tab[cpt] = this.pioche[cpt];
			}

			this.ctrl.getMetier().getPioche().remplir( tab );
		}

		//Accès libre Duo + événement
		if (nb == 7 )
		{
			this.ctrl.newJeuDuo();
			this.ctrl.activerEvent( true, true, true, true);

			this.ctrl.getMetier(1).setCouleur( Color.RED );
			this.ctrl.getMetier(2).setCouleur( Color.BLUE );

			this.pioche  = new String[] {Carte.CARTE[0], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0] };
			String[] tab = new String[this.pioche.length];

			for (int cpt=0 ; cpt<this.pioche.length ; cpt++)
			{
				tab[cpt] = this.pioche[cpt];
			}

			this.ctrl.getMetier().getPioche().remplir( tab );
		}

		
		//Duo : Changement de couleur + Pioche Commune + Tour par tour
		if ( nb == 8)
		{
			this.ctrl.newJeuDuo();
			this.ctrl.activerEvent( false, false, false, false);

			String[] tab = new String[] {Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0] };
			this.ctrl.getMetier().getPioche().remplir( tab );
			
			this.ctrl.getMetier(1).setCouleur( Color.RED );
			this.ctrl.getMetier(2).setCouleur( Color.BLUE );

			this.ctrl.getMetier(1).colorierArete("4", "5");
			this.ctrl.getMetier(2).colorierArete("15", "17");

			this.ctrl.getMetier(1).colorierArete("5", "6");
			this.ctrl.getMetier(2).colorierArete("15", "14");
			
			this.ctrl.getMetier(1).colorierArete("6", "7");
			this.ctrl.getMetier(2).colorierArete("14", "13");
			
			this.ctrl.getMetier(1).colorierArete("7", "18");
			this.ctrl.getMetier(2).colorierArete("13", "12");
			
			this.ctrl.getMetier(1).colorierArete("18", "19");
			this.ctrl.getMetier(2).colorierArete("12", "9");
		}
		
		//Duo : Log + Score en direct + Bonus 2 tour en 1 (Séparation du bonus) + Fin
		if(nb == 9)
		{
			this.ctrl.newJeuDuo();
			this.ctrl.activerEvent( false, false, false, true);

			this.ctrl.getMetier(1).setCouleur( Color.RED  );
			this.ctrl.getMetier(2).setCouleur( Color.BLUE );

			this.pioche = new String[] {Carte.CARTE[1], Carte.CARTE[5], Carte.CARTE[0] };
			String[] tab = new String[this.pioche.length];

			for (int cpt=0 ; cpt<this.pioche.length ; cpt++)
			{
				tab[cpt] = this.pioche[cpt];
			}

			this.ctrl.getMetier().getPioche().remplir( tab );

			this.ctrl.getMetier(1).colorierArete("4", "9");
			this.ctrl.getMetier(2).colorierArete("15", "13");
			
			this.ctrl.getMetier(1).colorierArete("9", "13");
			this.ctrl.getMetier(2).colorierArete("13", "9");

			this.carteBonus = new CarteBonus("BonusBis");
			this.carteBonus2 = new CarteBonus("BonusBis");

			this.carteBonus.setEstActivee(true);
			this.carteBonus2.setEstActivee(true);

			this.ctrl.getMetier(1).setCarteBonus(this.carteBonus);
			this.ctrl.getMetier(2).setCarteBonus(this.carteBonus2);	
		}

		//Événement : Volcan -> Bifurcation -> Tsunami
		if (nb == 10 )
		{
			this.ctrl.setTourBiffurcation(6);
			this.ctrl.setTourVolcan(5);
			this.ctrl.setTourTsunami(7);

			this.ctrl.newJeuSolo();
			this.ctrl.activerEvent( true, true, true, false);

			this.ctrl.getMetier().setCouleur( Color.RED );

			this.pioche = new String[] {Carte.CARTE[0], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[5], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0], Carte.CARTE[0] };
			String[] tab = new String[this.pioche.length];

			for (int cpt=0 ; cpt<this.pioche.length ; cpt++)
			{
				tab[cpt] = this.pioche[cpt];
			}

			this.ctrl.getMetier().getPioche().remplir( tab );

			this.ctrl.getMetier().colorierArete("4", "9");
			this.ctrl.getMetier().colorierArete("9", "13");
			this.ctrl.getMetier().colorierArete("13", "17");
			this.ctrl.getMetier().colorierArete("17", "18");
		}
	}
}
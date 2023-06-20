/* SAE 2.01 
 * @author  : LEFEVRE Donovan, SA Mateo, DUNET Tom, LE BRETON Kyllian
 * version  : 2.01 
 * date     : 16/06
 */

package metier;

import java.util.*;

import controleur.Controleur;

import java.io.FileInputStream;
import iut.algo.Decomposeur;
import java.awt.Color;
import java.awt.geom.Line2D;

public class Graphique
{
	private ArrayList<Ile>    lstIle;
	private ArrayList<Route>  lstRoute;
	private ArrayList<Region> lstRegion;
	private Map<String,Ile> dicIle;

	private ArrayList<Ile>    lstIleVisite;
	private ArrayList<Route>  lstRouteVisite;

	private Ile ileDepart;
	private Ile ileArrivee;

	private Ile ileBifurcation;
	private int numTour;
	private int tourBifurcation;

	private int tourVolcan;
	private int tourTsunami;

	private Pioche pioche;
	private boolean peutJouer;

	private Joueur joueurRouge;
	private Joueur joueurBleu;
	private Joueur joueurActuel;

	private boolean couleurChange;

	private Color couleur;
	private Color couleurBonus;

	private CarteBonus carteBonus;
	private boolean    bonusPasse;
	private boolean    aCarteBonus;

	private Controleur ctrl;

	private int id;

	public Graphique(Controleur ctrl, int id)
	{
		this.ctrl = ctrl;
		this.id = id;

		this.lstIle    = new ArrayList<Ile>();
		this.lstRoute  = new ArrayList<Route> ();
		this.lstRegion = new ArrayList<Region>();

		this.lstIleVisite   = new ArrayList<Ile>();
		this.lstRouteVisite = new ArrayList<Route>();
		
		this.joueurRouge = new Joueur(Color.RED);
		this.joueurBleu  = new Joueur(Color.BLUE);

		if ((int)(Math.random()*2)+1 == 1) { this.joueurActuel = this.joueurRouge; }
		else {this.joueurActuel = this.joueurBleu; }

		this.couleur = this.joueurActuel.getColor();
		this.couleurChange = false;
		this.couleurBonus = new Color(9, 169, 21);

		this.lecteurFichier();

		this.pioche = new Pioche(this.ctrl);
		this.pioche.remplir();

		this.peutJouer = true;

		this.ileArrivee = null;
		this.ileDepart  = null;

		this.ileBifurcation = null;
		this.numTour = 0;

		this.tourBifurcation = this.ctrl.getTourBifurcation();
		this.tourVolcan      = this.ctrl.getTourVolcan();
		this.tourTsunami     = this.ctrl.getTourTsunami();

		this.carteBonus = new CarteBonus(this.ctrl.getCarteBonus().getImage());
		this.bonusPasse = false;

		this.dicIle = new HashMap<String,Ile>();
		for (Ile ile : lstIle)
		{
			dicIle.put(ile.getNom(), ile);
		}
	}

	public ArrayList<Ile> getIleExtremite()
	{
		ArrayList <Ile> listTmp = new ArrayList<>();
		listTmp.add(ileArrivee);
		listTmp.add(ileDepart);

		if (ileBifurcation != null) {listTmp.add(ileBifurcation); }

		return listTmp;
	}

	public Pioche getPioche() { return this.pioche; }

	// Lecture du fichier "Matrice.data"
	public void lecteurFichier()
	{
		try
		{
			Ile.autoIncrementeRemise0();

			Scanner sc = new Scanner ( new FileInputStream ( "./data/Map.data" ), "UTF8" );
			sc.nextLine();

			// BOUCLE NOEUDS (iles) //
			String ligne = sc.nextLine();
			do
			{
				Decomposeur dec = new Decomposeur(ligne);
				this.lstIle.add( new Ile(dec.getString(0), dec.getString(1), dec.getInt(2), dec.getInt(3), dec.getInt(4), dec.getInt(5) ) );

				ligne = sc.nextLine();
			} while(!ligne.isEmpty());

			sc.nextLine();

			// BOUCLE REGIONS //
			ligne = sc.nextLine();
			do
			{
				String[] tabString = ligne.split("	");

				Region regionTmp = new Region(tabString[0]);

				for (int cpt = 1; cpt < tabString.length; cpt++)
				{
					regionTmp.addIle(lstIle.get(Integer.parseInt(tabString[cpt]) - 1));
				}

				this.lstRegion.add(regionTmp);
				
				ligne = sc.nextLine();
			} while(!ligne.isEmpty());

			sc.nextLine();

			// BOUCLE ARETES //
			ligne = sc.nextLine();
			do
			{
				Ile   ileTmp1 = null;
				Ile   ileTmp2 = null;
				String[] tabString  = ligne.split("	");

				for(int cpt = 0; cpt < tabString.length; cpt++)
				{
					if(cpt % 2 == 0)
						ileTmp1 = this.lstIle.get(Integer.parseInt(tabString[cpt]) - 1);
					else
						ileTmp2 = this.lstIle.get(Integer.parseInt(tabString[cpt]) - 1);

					if(ileTmp1 != null && ileTmp2 != null) 
						this.lstRoute.add(new Route(ileTmp1, ileTmp2));
				}

				ligne = sc.nextLine();
			} while(!ligne.isEmpty());

			if (sc.hasNextLine() && sc.nextLine().equals("[BONUS]"))
			{
				// BOUCLE BONUS //
				int bonus = Integer.parseInt(sc.nextLine()); // Récupération de la valeur du bonus
				
				while( sc.hasNextLine())
				{
					ligne = sc.nextLine();

					Decomposeur dec = new Decomposeur(ligne);
					
					this.getArete(this.getIle(dec.getInt(0)), this.getIle(dec.getInt(1))).setBonus(bonus, this.couleurBonus);
				}
			}

			sc.close();
		}
		catch (Exception e){ e.printStackTrace(); }
	}

	public Ile getIle(int id)
	{
		for (Ile ile : this.lstIle)
		{
			if (ile.getId() == id) { return ile;}
		}
		return null;
	}

	public Ile getIle(String nomIle)
	{
		for (Ile ile : this.lstIle)
		{
			if (ile.getNom().equals(nomIle)) { return ile;}
		}
		return null;
	}
	
	public Route getArete(Ile s1, Ile s2)
	{
		for (Route arete : this.lstRoute)
		{
			if (arete.getIle1() == s1 && arete.getIle2() == s2 ||
			    arete.getIle1() == s2 && arete.getIle2() == s1) { return arete;}
		}
		return null;
	}

	public ArrayList<Ile> getListIle() { return this.lstIle; }

	public ArrayList<Route> getListArete() { return this.lstRoute; }

	public ArrayList<Region> getListRegion() { return this.lstRegion; }

	public CarteBonus getCarteBonus() { return this.carteBonus; }

	// Permet de changer la couleur du joueur actuel, et de relancer une manche / Finir la partie
	public boolean changerCouleur()
	{
		if(!this.couleurChange)
		{

			if(this.couleur == Color.BLUE)
			{
				this.couleur = Color.RED;
				this.joueurActuel = this.joueurRouge;
			}	
			else
			{
				this.couleur = Color.BLUE;
				this.joueurActuel = this.joueurBleu;
			}
				 
			this.couleurChange = true;

			if( this.ctrl.getModeDuo() )
			{
				this.ctrl.initPioche();
				this.ctrl.majCouleur();
			}
			else
			{
				this.pioche = new Pioche(this.ctrl);
				if (!ctrl.getModeScenario()) { this.pioche.remplir(); }
				else 
				{ 
					this.pioche.remplir(this.ctrl.getScenario().getPioche());
				}
				this.ctrl.majCouleur(this.couleur);
			}

			if (this.ctrl.getModeScenario() && this.ctrl.getScenario().getCarteBonus() != null) {this.ctrl.getScenario().activerCarteBonus();}


			this.lstIleVisite   = new ArrayList<Ile>();
			this.lstRouteVisite = new ArrayList<Route>();

			this.ileArrivee = this.ileDepart = this.ileBifurcation = null;
			// this.tourBifurcation = this.ctrl.getTourBifurcation();

			if (this.id == 1){this.ctrl.newCarte(); }

			if (!ctrl.getModeScenario()) { this.carteBonus = new CarteBonus(this.ctrl.getCarteBonus().getImage()); }

			if(!this.aCarteBonus)
				this.carteBonus.setEstActivee(true);

			if(this.ctrl.getModeDuo() && this.id == 1)
			{
				this.ctrl.ajouterLog("Les 2 Joueur ont échanger leur couleur");
			}
			else
			{
				this.ctrl.ajouterLog("Vous avez changer de couleur");
			}

			return true;
		}
		else
		{
			this.ctrl.maj();
			this.ctrl.fin(totalPointsPopUp());
		}

		this.ctrl.maj();
		return false;
	}

	// S'occupe du déroulement d'un tour
	public void tour()
	{
		if( this.pioche.estFini() )
		{
			if( this.ctrl.getModeDuo() )
			{
				if( this.ctrl.getAJouer() )
				{
					if( this.couleurChange) this.ctrl.finDuo();
					else this.ctrl.changerCouleur();

					this.ctrl.setAJouer(false);
					this.ctrl.setPeutJouer();
				}
				else
				{
					this.ctrl.setAJouer(true);
					this.peutJouer = false;
				}
			}
			else
			{
				if( this.ctrl.getModeChallenge())
				{
					if( this.couleurChange) this.ctrl.fin(this.totalPointsPopUp());
					else
					{
						this.changerCouleur();
						this.pioche.remplir( this.ctrl.getClassement().getPioche(1) );
					}
				}
				else
				{
					if( this.couleurChange) this.ctrl.fin(this.totalPointsPopUp());
					else this.changerCouleur();
				}
				
			}
		}
		else
		{
			if( this.ctrl.getModeDuo() )
			{
				if( this.ctrl.getAJouer() )
				{
					this.pioche.piocher();
					this.ctrl.setAJouer(false);
					this.ctrl.setPeutJouer();
					this.ctrl.ajouterLog("La Carte " + (this.pioche.getCarte(0).getImage().charAt(1) == 'N'?"primaire ":"secondaire ") + this.pioche.getCarte(0).getCouleur() + " a était tiré !");
				}
				else
				{
					if (!this.carteBonus.getImage().equals("BonusBis") || !this.carteBonus.getIsSelected() ) 
					{
						this.ctrl.setAJouer(true);
						this.peutJouer = false;
					}
					
				}
			}
			else
			{
				if (!this.carteBonus.getImage().equals("BonusBis") || !this.carteBonus.getIsSelected() ) 
				{
					this.pioche.piocher();
					this.ctrl.ajouterLog("La Carte " + (this.pioche.getCarte(0).getImage().charAt(1) == 'N'?"primaire ":"secondaire ") + this.pioche.getCarte(0).getCouleur() + " a était tiré !");
				}
				else 
				{
					this.bonusPasse = true;
				}

			}
		}

		if (this.carteBonus.getIsSelected())
		{
			this.carteBonus.setIsSelected(false);
			this.carteBonus.setEstActivee(true);
		}

		this.joueurActuel.compterPoints();

		if (this.numTour%10 == this.tourBifurcation)
		{
			this.ctrl.ajouterLog("Vous pouvez faire une BIFURCATION");
		}
		
		if(this.ctrl.getModeDuo() ) this.ctrl.majCouleur();
		else this.ctrl.majCouleur(this.couleur);

		this.ctrl.maj();
	}

	// Permet de colorier une arete et de mettre à jour l'IHM
	public void colorierArete(String som1, String som2)
	{
		String tmp = verifColoriage(som1, som2);
		this.ctrl.majLabelIHM(tmp, this.id);

		if( tmp.equals("L'arete a bien été colorié"))
		{
			Ile ileTmp1 = null;
			Ile ileTmp2 = null;

			try
			{
				ileTmp1 = this.getIle(Integer.parseInt(som1));
				ileTmp2 = this.getIle(Integer.parseInt(som2));
			}
			catch (Exception e)
			{
				ileTmp1 = this.dicIle.get(som1);
				ileTmp2 = this.dicIle.get(som2);
			}

			Route areteTmp = this.getArete(ileTmp1, ileTmp2);

			if (ctrl.getModeDuo())
			{
				this.ctrl.ajouterLog("Le joueur " + this.id + " a créée une liaison entre " + ileTmp1.getNom() + " et " + ileTmp2.getNom());
			}
			else
			{
				this.ctrl.ajouterLog("Vous avez créée une liaison entre " + ileTmp1.getNom() + " et " + ileTmp2.getNom());
			}

			// Evenement Biffurcation	
			if (this.numTour == this.tourBifurcation && this.ileDepart != ileTmp1 && this.ileArrivee != ileTmp1)
			{
				this.ileBifurcation = ileTmp1;
			}

			//changement des extremiter
			if (this.joueurActuel.getIleVisitee().size() != 0)
			{
				if      (this.ileArrivee     == ileTmp1) { this.ileArrivee = ileTmp2;     }
				else if (this.ileArrivee     == ileTmp2) { this.ileArrivee = ileTmp1;     }
				else if (this.ileDepart      == ileTmp1) { this.ileDepart  = ileTmp2;     }
				else if (this.ileDepart      == ileTmp2) { this.ileDepart  = ileTmp1;     }
				else if (this.ileBifurcation == ileTmp1) { this.ileBifurcation = ileTmp2; }
				else if (this.ileBifurcation == ileTmp2) { this.ileBifurcation = ileTmp1; }
			}

			if (this.joueurActuel.getIleVisitee().size() == 0)
			{
				this.ileDepart  = ileTmp1;
				this.ileArrivee = ileTmp2;
			}

			// Ajoute les arêtes visités à une liste pour la vérification de croisement
			this.lstRouteVisite.add(areteTmp);

			// Ajoute les iles visités à des listes pour le comptage de points et la vérification de cycle
			if (!this.lstIleVisite.contains(ileTmp1)) { this.lstIleVisite.add(ileTmp1); }
			if (!this.lstIleVisite.contains(ileTmp2)) { this.lstIleVisite.add(ileTmp2); }

			this.joueurActuel.addIleVisite(ileTmp1);
			this.joueurActuel.addIleVisite(ileTmp2);

			// Ajout de la région visite au joueur
			for (Region region : this.lstRegion)
			{
				if (region.getEnsIles().contains(ileTmp1) || region.getEnsIles().contains(ileTmp2)) 
				{ 
					this.joueurActuel.addRegionVisite(region);
				}
			}

			if (areteTmp.getColor() == this.couleurBonus && areteTmp.getColor() == Color.ORANGE) { this.joueurActuel.addBonus(areteTmp.getValeur());}

			// Change la couleur dans la classe Arete
			areteTmp.setCouleur(couleur);

			this.ctrl.majScoreIHM(this.totalPoints(), this.id);
			if(this.ctrl.getModeDuo() ) this.ctrl.majCouleur();
			else this.ctrl.majCouleur(this.couleur);

			this.tour();
		}
	}

	public void eventBonusX2()
	{
		if (this.carteBonus.getIsSelected() && this.carteBonus.getImage().equals("BonusX2") && this.joueurActuel.getIleVisitee().size() == 0)
		{
			if (this.joueurActuel == this.joueurRouge ) { this.getIle("Ticó") .setBonus(2); }
			if (this.joueurActuel == this.joueurBleu  ) { this.getIle("Mutaa").setBonus(2); }
		}

		else if (this.carteBonus.getIsSelected() && this.carteBonus.getImage().equals("BonusX2") )
		{
			this.joueurActuel.getIleVisitee().get(this.joueurActuel.getIleVisitee().size()-1).setBonus(2);
		}

		this.carteBonus.setEstActivee(true);

		this.ctrl.majScoreIHM(this.totalPoints(), this.id);
		this.ctrl.maj();
	}

	public void eventBifurcation()
	{
		// Evenement Bifurcation
		if (this.numTour%10 == this.tourBifurcation)
		{
			for (Ile ile : this.lstIleVisite)
			{
				ile.setMouvementPossible(true);
			}
		}
	}

	// Vérifie si l'arête que l'on souhaite colorier remplit toutes les contraintes
		public String verifColoriage(String som1, String som2)
		{
			Ile ile1 = null;
			Ile ile2 = null;
			Route  arete1;
			
			if( this.ctrl.getModeDuo() && !this.peutJouer )
				return "Attendez que l'adversaire joue";

			// Vérifie que les deux iles existes
			try
			{
				ile1 = this.getIle(Integer.parseInt(som1));
				ile2 = this.getIle(Integer.parseInt(som2));
			}
			catch (Exception e)
			{
				try
				{
					ile1 = this.dicIle.get(som1);
					ile2 = this.dicIle.get(som2);
				}
				catch (Exception er)
				{
					return( "Veuillez entrer des iles valides" );
				}
			}

			// Vérifie que l'arête existe
			arete1 = this.getArete(ile1, ile2);
			if(arete1 == null)
				return("Il n'y a pas d'arête entre vos 2 iles");


			// Vérifie que l'on part de la bonne ile
			if (this.joueurActuel.getIleVisitee().size() == 0 && this.joueurActuel == this.joueurRouge && 
				!ile1.getNom().equals("Ticó") && !ile2.getNom().equals("Ticó") )
			{
				return "Vous devez partir de l'ile Ticó";
			}

			if (this.joueurActuel.getIleVisitee().size() == 0 && this.joueurActuel == this.joueurBleu && 
				!ile1.getNom().equals("Mutaa") && !ile2.getNom().equals("Mutaa") )
			{
				return "Vous devez partir de l'ile Mutaa";
			}

			if(!ile2.getCouleurIle().equals(this.pioche.getCarte(0).getCouleur()) && !this.pioche.getCarte(0).getCouleur().equals("Joker") )// && !ile2.getCouleurIle().equals
			{
				return "L'île selectionnée n'est pas de la même couleur que la carte piochée";
			}

			// Vérifie que l'arête n'est pas déjà possédé
			if (this.carteBonus.getIsSelected() && this.carteBonus.getImage().equals("BonusVoieDouble"))
			{
				return "L'arete a bien été colorié";
			}
			else 
			{
				if (arete1.getColor() != Color.BLACK && arete1.getColor() != this.couleurBonus && arete1.getColor() != Color.ORANGE) 
				{ 
					return "L'arete est déjà colorié";
				}
			}

			// Vérifie que ça ne forme pas un cycle
			if (this.joueurActuel.getIleVisitee().contains(ile1) && this.joueurActuel.getIleVisitee().contains(ile2))
			{ 
				return "Cette arete forme un cycle"; 
			}

			// Vérifie que l'on part d'un ile visité
			boolean ileVisitee = ile1 == this.ileArrivee     || ile1 == this.ileDepart || 
								ile2 == this.ileArrivee     || ile2 == this.ileDepart ||
								ile1 == this.ileBifurcation || ile2 == this.ileBifurcation;
			
			if (!ileVisitee && this.lstIleVisite.size() != 0 && this.numTour != this.tourBifurcation) { return "Vous ne partez pas d'une extremité"; }

			// Vérifie que l'arête ne croise pas une autre arête
			if (this.carteBonus.getImage().equals("BonusCroisement") && this.carteBonus.getIsSelected() )
			{
				return "L'arete a bien été colorié";
			}
			else 
			{
				for (Route arete2 : this.lstRoute)
				{
					int[][] segment = {   {arete1.getIle1().getPosX(), arete1.getIle1().getPosY() ,
										arete1.getIle2().getPosX(), arete1.getIle2().getPosY()},
										{arete2.getIle1().getPosX(), arete2.getIle1().getPosY() , 
										arete2.getIle2().getPosX(), arete2.getIle2().getPosY()} };
					
					boolean intersect = doEdgesIntersect(segment[0], segment[1]);

					if (intersect) 
					{
					if (arete2.getColor() == Color.RED || arete2.getColor() == Color.BLUE) 
						{
							return "L'arete croise une arete déjà colorié";
						}
					}
				}
			}
			
			return "L'arete a bien été colorié";
		}

	// Vérifie si deux segments se croisent
	public static boolean doEdgesIntersect(int[] cord1, int[] cord2)
	{
		// Création des objets Line2D pour représenter les arêtes
		Line2D line1 = new Line2D.Double(cord1[0], cord1[1], cord1[2], cord1[3]);
		Line2D line2 = new Line2D.Double(cord2[0], cord2[1], cord2[2], cord2[3]);
	
		// Vérification si les lignes se croisent
		if (line1.intersectsLine(line2)) { // Les arêtes se croisent

			// Vérification si les arêtes sont adjacentes
			if (line1.getP1().equals(line2.getP1()) || line1.getP1().equals(line2.getP2()) || line1.getP2().equals(line2.getP1()) || line1.getP2().equals(line2.getP2()))
			{
				return false; // Les arêtes sont adjacentes, elles ne se croisent pas
			}	
			return true; // Les arêtes se croisent
		}
		return false; // Les arêtes ne se croisent pas
	}

	public Color getCouleurActu() {return this.couleur;}

	public String totalPoints()
	{ 
		return "<html><pre>" +
			   "ROUGE - Points : " + String.format("%02d", this.joueurRouge .compterPoints()) + " Bonus voie : " + this.joueurRouge.getBonus() + " \n" + 
		       "BLEU  - Points : " + String.format("%02d", this.joueurBleu .compterPoints())  + " Bonus voie : " + this.joueurBleu .getBonus() + " \n" +  
			   "Bonus iles     : " + this.compterBonus() + 
			   "</pre></html>";
	}

	public String totalPointsPopUp()
	{
		return "Points rouge : " + (this.joueurRouge.compterPoints() + this.joueurRouge.getBonus()) + " \n" + 
		       "Points bleu : "  + (this.joueurBleu .compterPoints() + this.joueurBleu .getBonus()) + " \n" +  
			   "Bonus : "        + this.compterBonus() + "\n" + 
			   "Total : "        + this.calculPoint();
	}

	public int calculPoint()
	{
		return this.joueurBleu .compterPoints() + this.joueurBleu .getBonus() + this.joueurRouge.compterPoints() +this.joueurRouge.getBonus() +this.compterBonus();
	}

	public int compterBonus()
	{
		int bonus = 0;

		for (Ile ileRouge : this.joueurRouge.getIleVisitee())
		{
			for (Ile ileBleu : this.joueurBleu.getIleVisitee())
			{
				if (ileRouge == ileBleu) { bonus += 2; }
			}
		}
		return bonus;
	}

	//Méthode de test de scenario
	public void setCouleur(Color coul)
	{
		this.couleur = coul;
		if( this.couleur == Color.RED )
			this.joueurActuel = this.joueurRouge;
		else
			this.joueurActuel = this.joueurBleu;

		if(this.ctrl.getModeDuo() ) this.ctrl.majCouleur();
		else this.ctrl.majCouleur(this.couleur);
	}

	public void setPioche(Pioche pioche) { this.pioche = pioche; }

	public void setPeutJouer() { this.peutJouer = true; }

	public void eventVolcan()
	{
		if(this.numTour == this.tourVolcan && !this.bonusPasse)
		{
			Ile ileTmp1 = null;
			Ile ileTmp2 = null;

			int test = (int)(Math.random()*3);
			if( test == 0)
			{
				this.lstIle.add( new Ile("Volcan", "Jocker", 1075, 80, 1035, 40 ));
				if(this.ileExiste("Kiola"))
				{
					ileTmp1 = this.getIle("Kiola");
					this.lstRoute.add(new Route(this.getIle("Kiola"), this.lstIle.get(this.lstIle.size() - 1) ));
					this.getArete(this.getIle("Kiola"), this.lstIle.get(this.lstIle.size() - 1)).setBonus(3, Color.ORANGE);
				}
				if(this.ileExiste("Fama"))
				{
					ileTmp2 = this.getIle("Fama");
					this.lstRoute.add(new Route(this.getIle("Fama"), this.lstIle.get(this.lstIle.size() - 1) ));
					this.getArete(this.getIle("Fama"), this.lstIle.get(this.lstIle.size() - 1)).setBonus(3, Color.ORANGE);
				}
			}
			if( test == 1)
			{
				this.lstIle.add( new Ile("Volcan", "Jocker", 1000, 750, 960, 710 ));
				if(this.ileExiste("Fissah"))
				{
					ileTmp1 = this.getIle("Fissah");
					this.lstRoute.add(new Route(this.getIle("Fissah"), this.lstIle.get(this.lstIle.size() - 1) ));
					this.getArete(this.getIle("Fissah"), this.lstIle.get(this.lstIle.size() - 1)).setBonus(3, Color.ORANGE);
				}
				if(this.ileExiste("Maÿa"))
				{
					ileTmp2 = this.getIle("Maÿa");
					this.lstRoute.add(new Route(this.getIle("Maÿa"), this.lstIle.get(this.lstIle.size() - 1) ));
					this.getArete(this.getIle("Maÿa"), this.lstIle.get(this.lstIle.size() - 1)).setBonus(3, Color.ORANGE);
				}
			}
			if( test == 2)
			{
				this.lstIle.add( new Ile("Volcan", "Jocker", 450, 370, 410, 330 ));
				if(this.ileExiste("Milaù"))
				{
					ileTmp1 = this.getIle("Milaù");
					this.lstRoute.add(new Route(this.getIle("Milaù"), this.lstIle.get(this.lstIle.size() - 1) ));
					this.getArete(this.getIle("Milaù"), this.lstIle.get(this.lstIle.size() - 1)).setBonus(3, Color.ORANGE);
				}
				if(this.ileExiste("Kita"))
				{
					ileTmp2 = this.getIle("Kita");
					this.lstRoute.add(new Route(this.getIle("Kita"), this.lstIle.get(this.lstIle.size() - 1) ));
					this.getArete(this.getIle("Kita"), this.lstIle.get(this.lstIle.size() - 1)).setBonus(3, Color.ORANGE);
				}
			}
			
			this.dicIle.put("Volcan", this.lstIle.get(this.lstIle.size() - 1));
			
			if (this.id != 2){this.ctrl.ajouterLog("Un Volcan est apparue dans l'archipel au largue de " + (ileTmp1 != null?(ileTmp1.getNom() + (ileTmp2 != null?(" et de " + ileTmp2.getNom()):"")):ileTmp2.getNom()));}
			if (this.id != 2){this.ctrl.popUp("ATTENTION : UN VOLCAN EST APPARUE");}
		}
	}

	private boolean ileExiste(String nomIle)
	{
		for (Ile ile : lstIle)
		{
			if(ile.getNom().equals(nomIle))
				return true;
		}

		return false;
	}

	public void eventTsunami()
	{
		if(this.numTour == this.tourTsunami && !this.bonusPasse)
		{
			ArrayList <Ile> ileBleuVisitee  = (ArrayList<Ile>) this.joueurBleu.getIleVisitee();
			ArrayList <Ile> ileRougeVisitee = (ArrayList<Ile>) this.joueurRouge.getIleVisitee();

			ArrayList <Ile> ileDispo = new ArrayList <Ile>();

			for (Ile ile : this.lstIle)
			{
				if (!ileBleuVisitee.contains(ile) && !ileRougeVisitee.contains(ile))
				{
					ileDispo.add(ile);
				}
			}

			Ile ileRemove = ileDispo.get((int)(Math.random()*ileDispo.size()));
			
			while (ileRemove == this.getIle(15) || ileRemove == this.getIle(4))
			{
				ileRemove = ileDispo.get((int)(Math.random()*ileDispo.size()));
			}

			this.lstIle.remove(ileRemove);

			for (int cptRoute = 0 ; cptRoute < this.lstRoute.size() ; cptRoute ++)
			{
				if (this.lstRoute.get(cptRoute).getIle1() == ileRemove || this.lstRoute.get(cptRoute).getIle2() == ileRemove)
				{
					this.lstRoute.remove(cptRoute);
					cptRoute --;
				}
			}

			if (this.id != 2){this.ctrl.ajouterLog("Un Tsunami a eux lieux dans l'archipel l'ile : " + ileRemove.getNom() + " a disparue !");}

			if (this.id != 2){this.ctrl.popUp("ATTENTION : UN TSUNAMI");}
		}
	}

	public void activerEvent(boolean bif, boolean vol, boolean tsu, boolean bon)
	{
		if (!bif) { this.tourBifurcation = -1;}
		if (!vol) { this.tourTsunami     = -1;}
		if (!tsu) { this.tourVolcan      = -1;}
		this.aCarteBonus = bon;
		if (!bon) { this.carteBonus.setEstActivee(true);}
	}

	public void setCarteBonus(CarteBonus carte) { this.carteBonus = carte; }

	public void incNumTour() 	{ this.numTour ++;}
}
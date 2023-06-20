/* SAE 2.01 
 * @author  : LEFEVRE Donovan, SA Mateo, DUNET Tom, LE BRETON Kyllian
 * version  : 2.01 
 * date     : 16/06
 */

package controleur;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import metier.*;
import ihm.Frame;
import ihm.FrameChoix;
import iut.algo.Decomposeur;

public class Controleur
{
	private Graphique metier;
	private Frame ihm;
	private Scenario scenario;
	private GenererClassement classement;

	private boolean modeChallenge;
	private boolean modeDuo;
	private boolean aJouer;
	private Graphique metier2;

	private int tourVolcan;
	private int tourBifurcation;
	private int tourTsunami;

	private int tourEvent;

	private ArrayList<CarteBonus> lstCarteBonus;
	private CarteBonus carteBonus;

	private Date debutPartie;

	private boolean modeScenario;

	public Controleur()
	{
		this.tourBifurcation = (int)(Math.random()*10);
		this.tourVolcan      = (int)(Math.random()*15);
		this.tourTsunami     = (int)(Math.random()*15);

		this.creerCarteBonus();
		this.carteBonus = this.lstCarteBonus.get((int)(Math.random()*this.lstCarteBonus.size()));

		this.tourEvent = 0;

		this.debutPartie = new Date();
		this.modeScenario = false;
		this.modeChallenge = false;

		new FrameChoix( this );
	}

	// Récupération de la classe Graphique pour le mode Solo
	public Graphique getMetier() { return this.metier; }

	// Récupération de la classe Graphique pour le mode Duo
	public Graphique getMetier(int choix)
	{
		if (choix == 1) {return this.metier;}
		else {return this.metier2;}
	}

	// Récupère si le mode de jeu est en Solo (false) ou Duo (true)
	public boolean getModeDuo() { return this.modeDuo; }

	// Récupere si le mode de jeu est en Challenge
	public boolean getModeChallenge() { return this.modeChallenge; }

	// Récupère le numéro du scénario
	public Scenario getScenario() { return this.scenario; }

	// Recupere le gerenateur classement
	public GenererClassement getClassement() { return this.classement; }

	// Récupère si le joueur peut jouer ou non
	public boolean getAJouer() { return this.aJouer; }

	public Pioche     getPioche()     { return this.metier.getPioche(); }
	public CarteBonus getCarteBonus() { return this.carteBonus; }

	public int getTourBifurcation() { return this.tourBifurcation; }
	public int getTourVolcan()      { return this.tourVolcan; }
	public int getTourTsunami()     { return this.tourTsunami; }

	public int getTourEvent()       { return this.tourEvent;}

	public boolean getModeScenario() { return this.modeScenario; }

	// Créer et mélange la pioche
	public void initPioche()
	{
		Pioche pioche = new Pioche(this);
		pioche.remplir();

		this.metier.setPioche(pioche);
		this.metier2.setPioche(pioche);
	}

	// Permet de changer la permission de jouer
	public void setAJouer(Boolean bool) { this.aJouer = bool; }

	// Permet de remettre la permission de jouer aux deux joueurs
	public void setPeutJouer() 
	{ 
		this.metier .setPeutJouer();
		this.metier2.setPeutJouer();
	}

	public void setTourBiffurcation(int tour){ this.tourBifurcation = tour;}
	public void setTourVolcan      (int tour){ this.tourVolcan      = tour;}
	public void setTourTsunami     (int tour){ this.tourTsunami     = tour;}

	// Change la couleur des arêtes
	public void changerCouleur()
	{
		this.metier.changerCouleur();
		this.metier2.changerCouleur();
	}

	public void maj() { this.ihm.maj(); }

	public void majLabelIHM(String messLabel, int id) { this.ihm.majLabelIHM(messLabel, id); }
	public void majScoreIHM(String messScore, int id) { this.ihm.majScore   (messScore, id); }
	public void majCouleur (Color couleur)            { this.ihm.majCouleur (couleur); }
	public void majCouleur ()                         { this.ihm.majCouleur ( this.metier.getCouleurActu(),  this.metier2.getCouleurActu());}

	// Lance le jeu en mode Solo
	public void newJeuSolo()
	{
		this.metier = new Graphique(this, 1);
		this.ihm    = new Frame(this);
		this.modeDuo = false;
	}

	// Ouvre le jeu en mode Solo avec un scénario
	public void lancerScenario( int nb )
	{
		if( this.scenario == null)
			this.scenario = new Scenario(this);

		this.scenario.lancerScenario(nb);
		this.modeScenario = true;
	}

	// Ouvre le jeu en mode Duo
	public void newJeuDuo()
	{
		this.modeDuo = true;
		this.metier  = new Graphique(this, 1);
		this.metier2 = new Graphique(this, 2);
		
		this.initPioche();

		this.aJouer = false;
		this.ihm    = new Frame(this);

		this.metier .setCouleur( Color.BLUE );
		this.metier2.setCouleur( Color.RED );

		this.majCouleur();
	}

	
	// Lance un challenge
	public void lancerChallenge()
	{
		if(this.classement == null)
			this.classement = new GenererClassement();
		
		this.modeChallenge = true;
		this.metier        = new Graphique(this, 1);
		this.ihm           = new Frame(this);
		this.modeDuo       = false;
		
		this.metier.setCouleur(Color.RED);
		this.metier.getPioche().remplir( this.classement.getPioche(0) );
	}

	// Affiche le score du joueur dans le mode Solo
	public void fin(String tableauPoint)
	{
		this.ecritureLog(this.ihm.getLog() + tableauPoint);
		this.ihm.fin(tableauPoint + "\nTotal : " + this.metier.calculPoint() );
		
		if( this.classement != null )
		{
			String nom = JOptionPane.showInputDialog(null, "Entrez votre nom :", "Saisie du nom", JOptionPane.PLAIN_MESSAGE);
			if(nom != null)
			{
				this.classement.modifierClassement(nom, this.metier.calculPoint());
				this.classement.genererHTML();
				try
				{
					File fichierHTML = new File("./html/" + this.classement.getDataDate(LocalDate.now()) + ".html");
					Desktop.getDesktop().open(fichierHTML);
				} 
				catch (Exception e) { System.out.println("Ce n'est pas cencer être ici"); }
			}
		}
		this.ihm.dispose();
	}

	// Affiche le score des deux joueurs dans le mode Duo
	public void finDuo()
	{
		String s = "";
		s += "Joueur 1 :\n";
		s += this.metier.totalPointsPopUp() + "\n\n";
		s += "Joueur 2 :\n";
		s += this.metier2.totalPointsPopUp() + "\n\n";

		if( this.metier.calculPoint() > this.metier2.calculPoint() )
			s+= "Joueur 1 à Gagner !";
		if( this.metier.calculPoint() < this.metier2.calculPoint() )
			s+= "Joueur 2 à Gagner !";
		if( this.metier.calculPoint() == this.metier2.calculPoint() )
			s+= "Egalité !";

		this.ecritureLog(this.ihm.getLog() + s);
		this.ihm.fin(s);
		this.ihm.dispose();
	}

	public void setDeuxJoueurFaux() { this.modeDuo = false; }

	public void activerEvent(boolean bifu, boolean vol, boolean tsu, boolean bon)
	{
		this.metier.activerEvent( bifu, vol, tsu, bon);
		if (this.modeDuo) { this.metier2.activerEvent( bifu, vol, tsu, bon); }
	}

	public void popUp(String mess) { this.ihm.popUp(mess); }

	public void ajouterLog(String mess) { this.ihm.ajouterLog(mess); }

	public void tourEvent() { this.tourEvent++; }

	public void event()
	{
		this.metier.eventBifurcation();
		this.metier.eventTsunami();
		this.metier.eventVolcan();

		if (this.getModeDuo())
		{
			this.metier2.eventTsunami();
			this.metier2.eventBifurcation();
			this.metier2.eventVolcan();
		}
		this.ihm.maj();
	}

	public void creerCarteBonus()
	{
		this.lstCarteBonus= new ArrayList<CarteBonus> ();

		this.lstCarteBonus.add(new CarteBonus("BonusBis"));
		this.lstCarteBonus.add(new CarteBonus("BonusCroisement"));
		this.lstCarteBonus.add(new CarteBonus("BonusVoieDouble"));
		this.lstCarteBonus.add(new CarteBonus("BonusX2"));

		Collections.shuffle(this.lstCarteBonus);
	}

	public void newCarte() { this.carteBonus = this.lstCarteBonus.get((int)(Math.random()*this.lstCarteBonus.size())); }

	public void closeMode(  )
	{
		this.modeScenario = false;
		this.modeChallenge = false;
	}

	private void ecritureLog(String log)
	{
		Date finPartie = new Date();

		String sFinPartie = finPartie.toString();

		sFinPartie = sFinPartie.replace(':', '-');
		sFinPartie = sFinPartie.replace(' ', '_');

		String chemin = "./data/log/" + "log_" + sFinPartie + ".data";
		try
		{
			PrintWriter pw = new PrintWriter( new OutputStreamWriter( new FileOutputStream( chemin), "UTF8"));
			pw.println(finPartie + " durée : " + (finPartie.getTime() - debutPartie.getTime())/1000 + "s\n" );
			pw.println(log);
			pw.close();
		}
		catch (Exception e){ e.printStackTrace(); }
	}

	public void setCarteBonus(CarteBonus carte) { this.carteBonus = carte; }

	public static void main(String[] args)
	{
		new Controleur();
	}
}
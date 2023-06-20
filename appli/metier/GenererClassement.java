/* SAE 2.01 
 * @author  : LEFEVRE Donovan, SA Mateo, DUNET Tom, LE BRETON Kyllian
 * version  : 2.01 
 * date     : 16/06
 */

package metier;

import java.io.*;
import iut.algo.*;
import metier.Carte;
import metier.Pioche;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class GenererClassement
{
	private final static String CHEMIN = "./data/classement/";

	private File   dossier;
	private File[] files;
	private String nomFichier;

	private String[][] classement;
	
	public GenererClassement() 
	{
		this.nomFichier = this.getDataDate(LocalDate.now()) + ".data";
		this.classement = new String[100][2];
		File fileData = new File(GenererClassement.CHEMIN + nomFichier);
		File fileHTML = new File("./html/" + this.nomFichier.substring(0, this.nomFichier.length()-5) + ".html");

		try 
		{ 
			if(!fileData.exists()) 
			{ 
				fileData.createNewFile();
				initPiocheDuJour();
			
			}
			if(!fileHTML.exists()) { fileHTML.createNewFile(); }
		}
		catch (IOException e) { e.printStackTrace(); }

		this.classement = this.initClassement();
	}

	public String getDataDate(LocalDate date)
	{
		String sRet = "";

		sRet += date.getDayOfMonth() + "-" + date.getMonthValue() + "-" + date.getYear();
	
		return sRet;
	}

	// Genére une pioche pour la journée
	public void initPiocheDuJour()
	{
		Pioche pioche = new Pioche();
		pioche.remplir();

		String s = "";
		for(int cpt=0; cpt<2; cpt++)
		{
			pioche.remplir();
			for( Carte carte : pioche.getPioche())
			{
				s += carte.getImage() + "\t";
			}
		}

		try 
		{
			PrintWriter pw = new PrintWriter( new FileOutputStream(GenererClassement.CHEMIN + nomFichier) );
			pw.write(s);
			pw.close();

		}
		catch (IOException e) { e.printStackTrace();}
	}

	// Récupère la pioche du jour dans le .data
	public String[] getPioche(int num)
	{
		String[] pioche = new String[10];

		try
		{
			Scanner sc = new Scanner ( new FileInputStream (GenererClassement.CHEMIN + this.nomFichier));
			
			String s = sc.nextLine();
			Decomposeur dec = new Decomposeur ( s );

			for(int cpt=0; cpt<pioche.length; cpt++)
			{
				pioche[cpt] = dec.getString(cpt + num * 10);
			}
			
			sc.close();
		}
		catch (Exception e){ e.printStackTrace(); }

		return pioche;
	}

	// Récupère le classement du .data du jour
	public String[][] initClassement()
	{
		try
		{
			Scanner sc = new Scanner ( new FileInputStream (GenererClassement.CHEMIN + this.nomFichier), "UTF8" );
			
			sc.nextLine(); //ajout
			for(int i = 0; i < this.classement.length; i++)
			{
				if (!sc.hasNextLine() ) { break; }
				String s = sc.nextLine();
			
				Decomposeur dec  = new Decomposeur ( s );
				classement[i][0] = dec.getString(0);
				classement[i][1] = dec.getString(1);
			}

			sc.close();
		}
		catch (Exception e){ e.printStackTrace(); }

		return classement;
	}

	// Genère l'HTML du classement du jour
	public void genererHTML()
	{
		String nomFichierHTML = this.nomFichier.substring(0, this.nomFichier.length()-5);
		try{
			PrintWriter pw = new PrintWriter( new FileOutputStream("./html/"+ nomFichierHTML +".html") );

			pw.println("<!DOCTYPE html>");
			pw.println("<html>");
			pw.println("\t<head>");
			pw.println("\t\t<title> Classement Mode Challenge </title>");
			pw.println("\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">");
			pw.println("\t</head>");
			pw.println("\t<body>");
			pw.println("\t\t<h1> Classement du " + convertDateString(this.nomFichier.substring(0, this.nomFichier.length()-5)) + "</h1>");

			pw.println("\t\t<select onchange=\"changePage(this)\">");
			pw.println("\t\t\t<option value=\"\"> Sélectionnez un jour </option>");
	
			dossier = new File(GenererClassement.CHEMIN);
			files   = dossier.listFiles();

			for(int i = 0; i < files.length; i++)
			{
				if(files[i].isFile())
				{
					String s = files[i].getName().substring(0, files[i].getName().length()-5);
					
					pw.println("\t\t\t\t<option value=\"" + s + ".html\">" + convertDateString(s) + "</option>");
				}
			}

			pw.println("\t\t\t</select>");
			pw.println("\t\t\t<script src=\"script.js\"></script>");
			pw.println("\t\t\t<br> <br> <br> <br> <br>");
			pw.println("\t\t<table>");
			pw.println("\t\t\t<tr>");
			pw.println("\t\t\t\t<th> Rang </th>");
			pw.println("\t\t\t\t<th> Nom </th>");
			pw.println("\t\t\t\t<th> Score </th>");
			pw.println("\t\t\t</tr>");

			for(int i = 0; i <= this.classement.length; i++)
			{
				if (classement[i][0] == null) { break; }

				pw.println("\t\t\t<tr>");
				pw.println("\t\t\t\t<th> " + (i+1) + " </th>");
				pw.println("\t\t\t\t<th> " + classement[i][0] + " </th>");
				pw.println("\t\t\t\t<th> " + classement[i][1] + " </th>");
				pw.println("\t\t\t</tr>");
			}
			pw.println("\t\t</table>");
			pw.println("\t</body>");
			pw.println("</html");

			pw.close();
		}
		catch (Exception e){ e.printStackTrace(); }
	}

	// Met à jour le .data du jour
	public void modifierClassement( String nom, int score )
	{
		try
		{
			Scanner sc = new Scanner ( new FileInputStream ( GenererClassement.CHEMIN + nomFichier) );
			
			String s = "";
			String donnees = sc.nextLine() + "\n";

			if(!sc.hasNextLine() ) 
			{ 
				donnees += nom + "\t" + score;
			}
			else
			{
				do
				{
					s = sc.nextLine();
					Decomposeur dec = new Decomposeur ( s );

					if(!dec.getString(0).isEmpty())
					{
						if(dec.getInt(1) < score )
						{
							donnees += nom + "\t" + score + "\n";
							nom   = dec.getString(0);
							score = dec.getInt(1);
						}
						else
						{
							donnees += s + "\n";
						}
					}
				} while(sc.hasNextLine());
				donnees += nom + "\t" + score;
			}
			sc.close();

			PrintWriter pw = new PrintWriter( new FileOutputStream( GenererClassement.CHEMIN + nomFichier) );
			pw.println(donnees);
			pw.close();
		}
		catch (Exception e){ e.printStackTrace(); }
		
		updateClassement();
	}

	// Converti le nom du fichier, en date
	public String convertDateString(String date)
	{
		String[] s = date.split("-");

		String sRet = "";
		if (10 > Integer.parseInt(s[0])) { sRet += "0"; }
		
		sRet += s[0];

		switch(s[1])
		{
			case "1"  -> sRet += " Janvier ";
			case "2"  -> sRet += " Février ";
			case "3"  -> sRet += " Mars ";
			case "4"  -> sRet += " Avril ";
			case "5"  -> sRet += " Mai ";
			case "6"  -> sRet += " Juin ";
			case "7"  -> sRet += " Juillet ";
			case "8"  -> sRet += " Août ";
			case "9"  -> sRet += " Septembre ";
			case "10" -> sRet += " Octobre ";
			case "11" -> sRet += " Novembre ";
			case "12" -> sRet += " Décembre ";
		}
		sRet += s[2];

		return sRet;
	}

	// Met à jour le .data, puis l'HTML
	public void updateClassement()
	{
		initClassement();
		genererHTML();
	}
}
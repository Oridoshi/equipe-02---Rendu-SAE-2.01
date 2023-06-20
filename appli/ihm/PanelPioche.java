/* SAE 2.01 
 * @author  : LEFEVRE Donovan, SA Mateo, DUNET Tom, LE BRETON Kyllian
 * version  : 2.01 
 * date     : 16/06
 */

package ihm;

import javax.swing.*;

import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.util.List;
import java.awt.Dimension;

import controleur.Controleur;
import metier.Carte;
import metier.Pioche;

public class PanelPioche extends JPanel implements ActionListener
{
	private Controleur ctrl;

	private JPanel    panel;
	private JTextArea txtAreaLog;
	private JButton   btnPioche;

	public PanelPioche (Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.setLayout(new BorderLayout());

		if( this.ctrl.getModeDuo() )
			this.setPreferredSize(new Dimension(200, 500));
		else
			this.setPreferredSize(new Dimension(260, 500));

		this.panel      = new JPanel();
		this.txtAreaLog = new JTextArea("Bienvenue dans cette partie de CINKTERA \n");
		this.btnPioche  = new JButton("Passer le tour");

		this.txtAreaLog.setEditable(false);
		this.txtAreaLog.setLineWrap(true);
		this.txtAreaLog.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(this.txtAreaLog);

		this.panel.setLayout       (new BorderLayout());
		this.panel.setPreferredSize(new Dimension(250, 280));

		this.panel.add( scrollPane, BorderLayout.CENTER );
		this.panel.add( this.btnPioche, BorderLayout.SOUTH );
		
		this.add( this.panel, BorderLayout.SOUTH);

		btnPioche.addActionListener(this);
	}

	public void ajouterLog(String mess)
	{
		this.txtAreaLog.append(mess + "\n");
		this.repaint();
	}

	public void paintComponent( Graphics g )
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		Pioche pioche = this.ctrl.getPioche();
		List<Carte> lstCarte = pioche.getPioche();

		//Affichage de la pioche
		/*if( lstCarte.size() != 1)
		{
			for(int cpt=1; cpt<lstCarte.size(); cpt++)
			{
				Image img = getToolkit().getImage ( "./images/cartes/DosCarte.png" );
				g2.drawImage( img, 25 + cpt*10, 0, this);
			}
		}*/

		//Affichage de la carte retourné
		Image img = getToolkit().getImage ( "./images/cartes/" + lstCarte.get(0).getImage() + ".png" );
		g2.drawImage( img, 50, 10, this);

		String[] cartesNoires = {"CNoirJoker", "CNoirVert", "CNoirRose", "CNoirJaune", "CNoirBrun"};
		String[] cartesBlanches = {"CBlancJoker", "CBlancVert", "CBlancRose", "CBlancJaune", "CBlancBrun"};

		//Affichage des petites cartes Noirs
		int x = 25;
		int y = 250;
		for (String carteNoire : cartesNoires)
		{
			y = 250;
			for(int cpt=1; cpt<lstCarte.size(); cpt++)
			{
				Carte carte = new Carte(carteNoire);
				if (lstCarte.get(cpt).equals(carte)) 
				{
					g2.drawImage(getToolkit().getImage("./images/cartes/" + carteNoire + ".png"), x, y, 24, 40, this);
					y+=5;
				}
			}
			x += 40;
		}

		//Affichage des petites cartes Blanches
		x = 25;
		y = 310;
		for (String carteBlanche : cartesBlanches)
		{
			y = 310;
			for(int cpt=1; cpt<lstCarte.size(); cpt++)
			{
				Carte carte = new Carte(carteBlanche);
				if (lstCarte.get(cpt).equals(carte)) 
				{
					g2.drawImage(getToolkit().getImage("./images/cartes/" + carteBlanche + ".png"), x, y, 24, 40, this);
					y+=5;
				}
			}
			x += 40;
		}
	}

	public void actionPerformed ( ActionEvent e )
	{
		if (e.getSource() == this.btnPioche )
		{
			this.txtAreaLog.append("Vous avez passez le tour !\n");
			this.ctrl.setAJouer(true);
			this.ctrl.getMetier().tour();
			this.repaint();
		}
	}

	public String getLog()
	{
		return this.txtAreaLog.getText();
	}
}
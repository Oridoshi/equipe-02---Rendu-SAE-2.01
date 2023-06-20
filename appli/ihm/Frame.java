/* SAE 2.01 
 * @author  : LEFEVRE Donovan, SA Mateo, DUNET Tom, LE BRETON Kyllian
 * version  : 2.01 
 * date     : 16/06
 */

package ihm;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Toolkit;

import java.awt.BorderLayout;
import java.awt.Color;

import controleur.Controleur;

public class Frame extends JFrame
{

	private final int      TAILLE_X = 1250;
	private final int      TAILLE_Y = 800;

	private Controleur     ctrl;

	private PanelOption    panelOption;
	private PanelGraphique panelGraphique;
	private PanelPioche    panelPioche;
	private PanelGraphique panelGraphique2;
	private PanelOption    panelOption2;

	public Frame(Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.setTitle("CinkeTera - SAE 2.01 - Equipe 2");

		Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
        int longueurEcran = tailleEcran.width;
        int largeurEcran = tailleEcran.height;
		this.setSize(TAILLE_X, TAILLE_Y);
		this.setLocation(((longueurEcran/2)-(TAILLE_X/2)), ((largeurEcran/2)-(TAILLE_Y/2)));

		JPanel panelTmp = new JPanel();
		panelTmp.setLayout(new BorderLayout());

		this.panelOption    = new PanelOption   (this.ctrl, 1);
		this.panelGraphique = new PanelGraphique(this.ctrl, 1);
		this.panelPioche    = new PanelPioche   (this.ctrl);

		panelTmp.add(this.panelOption);

		this.add(panelTmp               , BorderLayout.SOUTH );
		this.add(this.panelGraphique    , BorderLayout.CENTER);
		this.add(this.panelPioche       , BorderLayout.EAST  );

		if (ctrl.getModeDuo())
		{
			this.setSize(TAILLE_X + 350, TAILLE_Y-50);
			this.setLocation(((longueurEcran/2)-((TAILLE_X+350)/2)), ((largeurEcran/2)-(TAILLE_Y-50/2)));
			this.panelGraphique2 = new PanelGraphique(this.ctrl, 2);
			this.panelOption2    = new PanelOption   (this.ctrl, 2);
			
			//panelTmp.remove( this.panelOption );
			panelTmp.add(this.panelOption , BorderLayout.WEST  );
			panelTmp.add(this.panelOption2, BorderLayout.EAST  );

			this.add(this.panelGraphique  , BorderLayout.WEST  );
			this.add(this.panelPioche     , BorderLayout.CENTER);
			this.add(this.panelGraphique2 , BorderLayout.EAST  );
			this.add(panelTmp             , BorderLayout.SOUTH );

			this.revalidate();
		}

		this.setVisible(true);
		
		addWindowListener(new java.awt.event.WindowAdapter() 
		{
			public void windowClosing(java.awt.event.WindowEvent windowEvent) 
			{
				FrameChoix frame = new FrameChoix(ctrl);
				frame.setVisible(true);
			}
		});
	}
	
	public String getLog() { return this.panelPioche.getLog(); }

	public void dispose()
	{
		super.dispose();

		FrameChoix frame = new FrameChoix(this.ctrl);
		if(this.ctrl.getScenario() != null)
		{
			frame.choixScenario();
		}
		this.ctrl.closeMode();
	}

	public void maj() { this.repaint(); }

	public void fin(String tableauPoint) 
	{
		this.repaint();
		JOptionPane.showMessageDialog(null, tableauPoint);
	}

	public void majLabelIHM(String messErreur, int id)
	{
		if (id == 1) { this.panelOption.majLabel(messErreur); }
		else { this.panelOption2.majLabel(messErreur); }
	}

	public void majScore(String messScore, int id)
	{
		if (id == 1) { this.panelOption .majScore(messScore); }
		else         { this.panelOption2.majScore(messScore); }
	}

	public void majCouleur(Color couleur) { this.panelOption.majCouleur(couleur); }

	public void majCouleur(Color couleur1, Color couleur2)
	{
		this.panelOption .majCouleur(couleur1);
		this.panelOption2.majCouleur(couleur2);
	}

	public void popUp(String mess)
	{
		this.repaint();
		JOptionPane.showMessageDialog(null, mess);
	}

	public void ajouterLog(String mess) { this.panelPioche.ajouterLog(mess); }
}
/* SAE 2.01 
 * @author  : LEFEVRE Donovan, SA Mateo, DUNET Tom, LE BRETON Kyllian
 * version  : 2.01 
 * date     : 16/06
 */

package ihm;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.Toolkit;

import java.awt.GridLayout;
import java.awt.event.*;

import controleur.Controleur;

public class FrameChoix extends JFrame implements ActionListener, ItemListener
{
	private Controleur ctrl;

	private final int      TAILLE_X = 700;
	private final int      TAILLE_Y = 500;

	private JPanel  panel;
	private JButton btnSolo;
	private JButton btnDuo;
	private JButton btnScenario;

	private JButton btnRetour;
	private JButton btnScene;

	private JButton btnSauv;

	private JComboBox<String> ddlstScene;

	private JCheckBox cbVolcan;
	private JCheckBox cbTsunami;
	private JCheckBox cbBifurcation;

	private JCheckBox cbCarteBonus;
	private JCheckBox cbChallenge;
	
	public FrameChoix(Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.ctrl.setDeuxJoueurFaux();
		this.ctrl.closeMode();

		Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
        int longueurEcran = tailleEcran.width;
        int largeurEcran = tailleEcran.height;

		this.setTitle("CinkeTera - SAE 2.01 - Equipe 2");
		this.setSize(TAILLE_X,TAILLE_Y);
		this.setLocation(((longueurEcran/2)-(TAILLE_X/2)), ((largeurEcran/2)-(TAILLE_Y/2)));

		this.btnSolo     = new JButton("Solo");
		this.btnDuo      = new JButton("Duo");
		this.btnScenario = new JButton("Scénarios");
		this.btnRetour   = new JButton("Retour");
		this.btnScene    = new JButton("Lancer Scénario");
		this.btnSauv     = new JButton("Lancer une sauvegarde");

		this.cbVolcan      = new JCheckBox("Volcan"     , false );
		this.cbTsunami     = new JCheckBox("Tsunami"    , false );
		this.cbBifurcation = new JCheckBox("Bifurcation", false );
		this.cbCarteBonus  = new JCheckBox("Carte Bonus", false );
		this.cbChallenge   = new JCheckBox("Challenge"  , false );

		this.btnSolo    .addActionListener(this);
		this.btnDuo     .addActionListener(this);
		this.btnScenario.addActionListener(this);
		this.btnRetour  .addActionListener(this);
		this.btnScene   .addActionListener(this);
		this.btnSauv    .addActionListener(this);

		this.cbChallenge   .addItemListener(this);

		this.choixMode();

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	// Affiche le choix du mode de jeu [Solo, Duo, Scénario]
	public void choixMode()
	{
		if( this.panel != null)
			this.remove(this.panel);
		this.setSize(700,500);

		this.panel = new JPanel();
		this.panel.setLayout( new GridLayout(6,1) );

		JPanel pnlTemp = new JPanel();
		pnlTemp.add( this.cbBifurcation );
		pnlTemp.add( this.cbVolcan      );
		pnlTemp.add( this.cbTsunami     );
		pnlTemp.add( this.cbCarteBonus  );
		pnlTemp.add( this.cbChallenge   );
		
		ImageIcon img = new ImageIcon("./images/cink.png");
		this.panel.add( new JLabel(img) );
		this.panel.add( this.btnSolo     );
		this.panel.add( pnlTemp          );
		this.panel.add( this.btnDuo      );
		this.panel.add( this.btnScenario );
		this.panel.add( this.btnSauv     );

		this.btnSauv.setEnabled(false);

		this.add( this.panel );
		this.revalidate();
	}
	
	// Affiche le choix des scénarios
	public void choixScenario()
	{
		this.remove(this.panel);
		this.setSize(700,300);

		this.panel = new JPanel();
		this.panel.setLayout(new GridLayout(3,1));

		String[] text = new String[] {
		"1.  Solo : Accès Libre",
		"2.  Solo : Accès Libre + Evenement",
		"3.  Solo : Extremité + Cycle + Changement couleur + Passage sur ligne déjà colorié + Bonus",
	    "4.  Solo : Croisement 2 couleurs + Bonus Croisement + Cycle 2 couleurs", 
		"5.  Solo : Dernière carte noir + Fin", 
		"6.  Solo : Voie bonus + Bonus double desservance + Bonus Ile double",
		"7.  Duo  : Accès libre",
		"8.  Duo  : Accès libre + événement",
		"9.  Duo  : Changement de couleur + Pioche Commune + Tour par tour",
		"10. Duo  : Log + Score en direct + Bonus 2 tour en 1 (Séparation du bonus) + Fin",
		"11. Evenements : Tsunami, Volcan, Bifurcation"
		};

		this.ddlstScene = new JComboBox<String>( text );
		this.panel.add( this.ddlstScene );
		this.panel.add( this.ddlstScene );
		this.panel.add( this.btnScene   );
		this.panel.add( this.btnRetour  );

		this.add( this.panel );
		this.revalidate();
	}

	public void choixSauvegarde()
	{
		this.remove(this.panel);
		this.setSize(700,300);

		this.panel = new JPanel();
		this.panel.setLayout(new GridLayout(3,1));

		String[] text   = new String[] {"pas du sauvegarde"};
		this.ddlstScene = new JComboBox<String>( text );

		this.panel.add( this.ddlstScene );
		this.panel.add( this.ddlstScene );
		this.panel.add( this.btnSauv    );
		this.panel.add( this.btnRetour  );

		this.add( this.panel );
		this.revalidate();
	}

	public void actionPerformed ( ActionEvent e )
	{
		if (e.getSource() == this.btnSolo)
		{
			if( !this.cbChallenge.isSelected() )
			{
				this.ctrl.newJeuSolo();
				this.ctrl.activerEvent( this.cbBifurcation.isSelected(), this.cbVolcan.isSelected(), this.cbTsunami.isSelected(), this.cbCarteBonus.isSelected());
			}
			else
			{
				this.ctrl.lancerChallenge();
				this.ctrl.activerEvent( this.cbBifurcation.isSelected(), this.cbVolcan.isSelected(), this.cbTsunami.isSelected(), this.cbCarteBonus.isSelected());
			}
			this.dispose();
		}

		if (e.getSource() == this.btnDuo)
		{
			this.ctrl.newJeuDuo();
			this.ctrl.activerEvent( this.cbBifurcation.isSelected(), this.cbVolcan.isSelected(), this.cbTsunami.isSelected(), this.cbCarteBonus.isSelected());
			this.dispose();
		}

		if (e.getSource() == this.btnScenario)
		{
			this.choixScenario();
		}

		if (e.getSource() == this.btnRetour)
		{
			this.choixMode();
		}
		
		if (e.getSource() == this.btnScene)
		{
			this.ctrl.lancerScenario( this.ddlstScene.getSelectedIndex() );
			this.dispose();
		}

		if( e.getSource() == this.btnSauv )
		{
			this.choixSauvegarde();
		}

	}

	/*
	public void itemStateChanged(ItemEvent e)
	{
		if( this.cbChallenge.isSelected() )
		{
			this.cbBifurcation.setSelected( false );
			this.cbVolcan.setSelected( false );
			this.cbTsunami.setSelected( false );
			this.cbCarteBonus.setSelected( false );

			this.cbBifurcation.setEnabled( false );
			this.cbVolcan.setEnabled( false );
			this.cbTsunami.setEnabled( false );
			this.cbCarteBonus.setEnabled( false );

			this.btnDuo.setEnabled(false);
			this.btnScenario.setEnabled(false);
		}
		else
		{
			this.cbBifurcation.setEnabled( true );
			this.cbVolcan.setEnabled( true );
			this.cbTsunami.setEnabled( true );
			this.cbCarteBonus.setEnabled( true );

			this.btnDuo.setEnabled(true);
			this.btnScenario.setEnabled(true);

		}
		this.revalidate();
	}
	*/

	public void itemStateChanged(ItemEvent e)
	{
		if (e.getSource() == this.cbChallenge)
		{
			if( this.cbChallenge.isSelected() )
			{
				this.cbBifurcation.setSelected( false );
				this.cbVolcan.setSelected( false );
				this.cbTsunami.setSelected( false );
				this.cbCarteBonus.setSelected( false );

			}

			this.cbBifurcation.setEnabled( !this.cbBifurcation.isEnabled() );
			this.cbVolcan.setEnabled( !this.cbVolcan.isEnabled() );
			this.cbTsunami.setEnabled( !this.cbTsunami.isEnabled() );
			this.cbCarteBonus.setEnabled( !this.cbCarteBonus.isEnabled() );

			this.btnDuo.setEnabled(!this.btnDuo.isEnabled());
			this.btnScenario.setEnabled(!this.btnScenario.isEnabled());
			this.revalidate();
		}
		
	}
}
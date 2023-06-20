/* SAE 2.01 
 * @author  : LEFEVRE Donovan, SA Mateo, DUNET Tom, LE BRETON Kyllian
 * version  : 2.01 
 * date     : 16/06
 */

package ihm;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.Dimension;

import controleur.Controleur;

public class PanelOption extends JPanel implements ActionListener
{
	private Controleur ctrl;
	private JTextField txtSommet1;
	private JTextField txtSommet2;
	private JButton    btnValider;

	private JLabel lblErreur;
	private JLabel lblScore;
	private JPanel panelCouleurActu;

	private int id;

	public PanelOption (Controleur ctrl, int id)
	{
		this.ctrl = ctrl;
		this.id = id;

		this.setPreferredSize(new Dimension(650, 90));

		JPanel panelGlobal = new JPanel();

		this.txtSommet1 = new JTextField(8);
		this.txtSommet2 = new JTextField(8);
		this.lblErreur  = new JLabel(" ", JLabel.CENTER);
		this.lblScore   = new JLabel(" ");

		this.panelCouleurActu = new JPanel();
		this.panelCouleurActu.setBackground(this.ctrl.getMetier(id).getCouleurActu());

		this.btnValider = new JButton("Valider");

		this       .setLayout(new GridLayout(2, 1));
		panelGlobal.setLayout(new GridLayout(1, 4));

		JPanel panelTmp1;
		JPanel panelTmp2;

		panelTmp1 = new JPanel();
		panelTmp1  .add(new JLabel("Ile 1 : "));
		panelTmp1  .add(this.txtSommet1);
		panelGlobal.add(panelTmp1);

		panelTmp1 = new JPanel();
		panelTmp1  .add(new JLabel("Ile 2 : "));
		panelTmp1  .add(this.txtSommet2);
		panelGlobal.add(panelTmp1);

		panelTmp1 = new JPanel();
		panelTmp1  .add(this.btnValider);
		panelGlobal.add(panelTmp1);

		panelGlobal.add(panelCouleurActu);

		panelTmp2 = new JPanel();
		panelTmp2.setLayout( new GridLayout(1,2));
		panelTmp2.add(this.lblErreur);
		panelTmp2.add(this.lblScore );

		this.add(panelTmp2);
		this.add(panelGlobal);

		btnValider.addActionListener(this);
	}

	public void actionPerformed ( ActionEvent e )
	{
		if (e.getSource() == this.btnValider)
		{
			String[] txtEntree = new String[] { this.txtSommet1.getText().toLowerCase(), this.txtSommet2.getText().toLowerCase()};

			for (int i = 0; i < 2; i++) {

				// Permet de vérifier que le nom entré est bien un nom d'île même si il n'y a pas d'accent/majuscule
				txtEntree[i] = txtEntree[i].substring(0, 1).toUpperCase() + txtEntree[i].substring(1);

				switch(txtEntree[i])
				{
					case "Tiamu"       -> txtEntree[i] = "Tiamù";
					case "Tico"        -> txtEntree[i] = "Ticó";
					case "Khont rolah" -> txtEntree[i] = "Khont-Rolah";
					case "Khont-rolah" -> txtEntree[i] = "Khont-Rolah";
					case "Laçao"       -> txtEntree[i] = "Laçao";
					case "Loumu"       -> txtEntree[i] = "Loümu";
					case "Loa"         -> txtEntree[i] = "Loà";
					case "Milau"       -> txtEntree[i] = "Milaù";
					case "Maya"        -> txtEntree[i] = "Maÿa";
					default            -> txtEntree[i] = txtEntree[i];
				}
			}

			this.ctrl.getMetier(id).colorierArete(txtEntree[0], txtEntree[1] );
			this.ctrl.maj();
		}
	}

	public void majLabel(String messErreur)
	{
		this.lblErreur.setText(messErreur);
	}

	public void majScore(String messScore)
	{
		this.lblScore.setText(messScore);
	}

	public void majCouleur(Color couleur)
	{
		this.panelCouleurActu.setBackground(couleur);
	}
}
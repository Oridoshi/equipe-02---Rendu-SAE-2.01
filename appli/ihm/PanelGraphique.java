/* SAE 2.01 
 * @author  : LEFEVRE Donovan, SA Mateo, DUNET Tom, LE BRETON Kyllian
 * version  : 2.01 
 * date     : 16/06
 */

package ihm;

import controleur.Controleur;
import metier.*;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Dimension;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.ArrayList;

public class PanelGraphique extends JPanel implements MouseListener
{
	private final double MULTIPLICATEUR_X = 0.0008;
	private final double MULTIPLICATEUR_Y = 0.0011;

	private Ile ile1;
	private Controleur ctrl;

	public int id;

	public PanelGraphique(Controleur ctrl, int id)
	{
		this.id = id;

		if (ctrl.getModeDuo())
		{ 
			this.setPreferredSize(new Dimension(650, 350));
		}

		this.ctrl = ctrl;
		this.addMouseListener(this);
		this.setBackground(new Color(182, 211, 229));
	}

	public void paintComponent( Graphics graph )
	{
		super.paintComponent(graph);
		Graphics2D g2 = (Graphics2D) graph;
		
		ArrayList<Route> ensRoutes = this.ctrl.getMetier(id).getListArete();

		g2.setColor((new Color(0, 0, 132, 50)));
		float[] espace = { 5.0f, 5.0f };
		g2.setStroke(new BasicStroke( 1.5f , BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 10.0f, espace, 0.0f));
		g2.drawLine(                                                0 ,(int) (400 * this.getSize().getHeight() * MULTIPLICATEUR_Y), (int) ( 750 * this.getSize().getWidth()  * MULTIPLICATEUR_X) , (int) ( 400 * this.getSize().getHeight() * MULTIPLICATEUR_Y));
		g2.drawLine((int) (750 * this.getSize().getWidth()  * MULTIPLICATEUR_X) ,(int) (400 * this.getSize().getHeight() * MULTIPLICATEUR_Y), (int) (this.getSize().getWidth()) , (int) (  this.getSize().getHeight()));
		g2.drawLine((int) (750 * this.getSize().getWidth()  * MULTIPLICATEUR_X) ,(int) (400 * this.getSize().getHeight() * MULTIPLICATEUR_Y), (int) (this.getSize().getWidth()) , 0);
		g2.drawLine((int) (435 * this.getSize().getWidth()  * MULTIPLICATEUR_X) ,                                               0 , (int) ( 435 * this.getSize().getWidth()  * MULTIPLICATEUR_X) , (int) (this.getSize().getHeight()));

		for(int cpt=0; cpt < ensRoutes.size(); cpt++)
		{
			if(ensRoutes.get(cpt).getColor() == Color.RED || ensRoutes.get(cpt).getColor() == Color.BLUE)
				g2.setStroke(new BasicStroke( 5.0f ));
			else
				g2.setStroke(new BasicStroke( 1.5f ));

			g2.setColor( ensRoutes.get(cpt).getColor() );
			g2.drawLine( (int) (ensRoutes.get(cpt).getIle1().getPosX() * this.getSize().getWidth()  * MULTIPLICATEUR_X),
			             (int) (ensRoutes.get(cpt).getIle1().getPosY() * this.getSize().getHeight() * MULTIPLICATEUR_Y), 
			             (int) (ensRoutes.get(cpt).getIle2().getPosX() * this.getSize().getWidth()  * MULTIPLICATEUR_X), 
			             (int) (ensRoutes.get(cpt).getIle2().getPosY() * this.getSize().getHeight() * MULTIPLICATEUR_Y));
		}

		ArrayList<Ile> ensIles = this.ctrl.getMetier(id).getListIle();

		// Passe toute les îles en mouvement impossible
		for (Ile ile : ensIles) { ile.setMouvementPossible(false); }

		// Evenement Bi 
		if(ile1 == null)
			this.ctrl.getMetier(id).eventBifurcation();

		// Boucle transformation des images des îles en sombre
		for (int cpt = 0 ; cpt < ensIles.size() ; cpt ++ )
		{
			BufferedImage imgIle = null;

			try
			{
				imgIle = ImageIO.read(new File("./images/iles/" + ensIles.get(cpt).getNom() + ".png"));
			} catch (Exception e) {}

			if(this.ile1 == null)
			{
				if(ctrl.getMetier(id).getIleExtremite().get(1) != null)
				{
					for (Ile ile : ctrl.getMetier(id).getIleExtremite()) 
					{
						ile.setMouvementPossible(true);
					}
				}
				else
				{
					if(this.ctrl.getMetier(id).getCouleurActu().equals(Color.RED))
					{
						if(ensIles.get(cpt).getNom().equals("Ticó"))
							ensIles.get(cpt).setMouvementPossible(true);
					}
					else
						if(ensIles.get(cpt).getNom().equals("Mutaa"))
							ensIles.get(cpt).setMouvementPossible(true);
				}
			}
			else
			{

				for (Ile ile : ensIles)
				{
					if(this.ctrl.getMetier(id).verifColoriage(ile1.getNom(), ile.getNom()).equals("L'arete a bien été colorié"))
						ile.setMouvementPossible(true);
				}
			}

			if(ensIles.get(cpt).getMouvementPossible())
			{
				int width    = imgIle.getWidth();
				int height   = imgIle.getHeight();
				int[] pixels = imgIle.getRGB(0, 0, width, height, null, 0, width);
		
				// Parcours les pixels et les assombrire
				for (int i = 0; i < pixels.length; i++)
				{
					int rgb   = pixels[i];
					int alpha = (rgb >> 24) & 0xFF;
					int r     = (rgb >> 16) & 0xFF;
					int g     = (rgb >> 8) & 0xFF;
					int b     = rgb & 0xFF;

					r *= 1.25;
					b *= 1.25;
					g *= 1.25;

					pixels[i] = (alpha << 24) | (r << 16) | (g << 8) | b;
				}

				// Créer une nouvelle image avec les pixels modifiés
				BufferedImage modifiedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				modifiedImage.setRGB(0, 0, width, height, pixels, 0, width);
		
				g2.drawImage(modifiedImage, (int) (ensIles.get(cpt).getPosXImage() * this.getSize().getWidth()  * MULTIPLICATEUR_X),
				                            (int) (ensIles.get(cpt).getPosYImage() * this.getSize().getHeight() * MULTIPLICATEUR_Y),
				                            (int) (modifiedImage.getWidth()        * this.getSize().getWidth()  * MULTIPLICATEUR_X),
				                            (int) (modifiedImage.getHeight()       * this.getSize().getHeight() * MULTIPLICATEUR_Y), this);
			}
			else
			{
				if(ensIles.get(cpt).getIsSelected())
				{
					int width    = imgIle.getWidth();
					int height   = imgIle.getHeight();
					int[] pixels = imgIle.getRGB(0, 0, width, height, null, 0, width);
			
					// Parcours les pixels et les assombrire
					for (int i = 0; i < pixels.length; i++)
					{
						int rgb   = pixels[i];
						int alpha = (rgb >> 24) & 0xFF;
						int r     = (rgb >> 16) & 0xFF;
						int g     = (rgb >> 8) & 0xFF;
						int b     = rgb & 0xFF;

						if(this.ctrl.getMetier(id).getCouleurActu() == Color.RED)
						{
							r = 255;
							g = 0;
							b = 0;
						}
						else
						{
							r = 0;
							g = 0;
							b = 255;
						}

						pixels[i] = (alpha << 24) | (r << 16) | (g << 8) | b;
					}

					// Créer une nouvelle image avec les pixels modifiés
					BufferedImage modifiedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
					modifiedImage.setRGB(0, 0, width, height, pixels, 0, width);
			
					g2.drawImage(modifiedImage, (int) (ensIles.get(cpt).getPosXImage() * this.getSize().getWidth()  * MULTIPLICATEUR_X),
					                            (int) (ensIles.get(cpt).getPosYImage() * this.getSize().getHeight() * MULTIPLICATEUR_Y),
					                            (int) (modifiedImage.getWidth()        * this.getSize().getWidth()  * MULTIPLICATEUR_X),
					                            (int) (modifiedImage.getHeight()       * this.getSize().getHeight() * MULTIPLICATEUR_Y), this);
				}
				else
				{
					int width    = imgIle.getWidth();
					int height   = imgIle.getHeight();
					int[] pixels = imgIle.getRGB(0, 0, width, height, null, 0, width);
			
					// Parcours les pixels et les assombrire
					for (int i = 0; i < pixels.length; i++)
					{
						int rgb   = pixels[i];
						int alpha = (rgb >> 24) & 0xFF;
						int r     = (rgb >> 16) & 0xFF;
						int g     = (rgb >> 8) & 0xFF;
						int b     = rgb & 0xFF;

						r *= 0.60;
						b *= 0.60;
						g *= 0.60;

						pixels[i] = (alpha << 24) | (r << 16) | (g << 8) | b;
					}

					// Créer une nouvelle image avec les pixels modifiés
					BufferedImage modifiedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
					modifiedImage.setRGB(0, 0, width, height, pixels, 0, width);
			
					g2.drawImage(modifiedImage, (int) (ensIles.get(cpt).getPosXImage() * this.getSize().getWidth()  * MULTIPLICATEUR_X),
					                            (int) (ensIles.get(cpt).getPosYImage() * this.getSize().getHeight() * MULTIPLICATEUR_Y),
					                            (int) (modifiedImage.getWidth()        * this.getSize().getWidth()  * MULTIPLICATEUR_X),
					                            (int) (modifiedImage.getHeight()       * this.getSize().getHeight() * MULTIPLICATEUR_Y), this);
				}
			}

			g2.setColor(Color.BLACK);
			g2.drawString(ensIles.get(cpt).getNom(), (int) (ensIles.get(cpt).getPosXImage() * this.getSize().getWidth()  * MULTIPLICATEUR_X) + 50, (int) (ensIles.get(cpt).getPosYImage() * this.getSize().getHeight() * MULTIPLICATEUR_Y) );
		
			BufferedImage imgCarteBonus = null;

			try
			{
				imgCarteBonus = ImageIO.read(new File("./images/cartes/" + this.ctrl.getMetier(id).getCarteBonus().getImage() + ".png"));
			} catch (Exception e) {}

			if (!this.ctrl.getMetier(id).getCarteBonus().getEstActivee())
			{
				if (!this.ctrl.getMetier(id).getCarteBonus().getIsSelected())
				{
					int width    = imgCarteBonus.getWidth();
					int height   = imgCarteBonus.getHeight();
					int[] pixels = imgCarteBonus.getRGB(0, 0, width, height, null, 0, width);
			
					// Parcours les pixels et les assombrire
					for (int i = 0; i < pixels.length; i++)
					{
						int rgb   = pixels[i];
						int alpha = (rgb >> 24) & 0xFF;
						int r     = (int)(((rgb >> 16) & 0xFF) * 0.3);
						int g     = (int)(((rgb >> 8)  & 0xFF) * 0.3);
						int b     = (int)(( rgb        & 0xFF) * 0.3);
						
						pixels[i] = (alpha << 24) | (r << 16) | (g << 8) | b;
					}

					// Créer une nouvelle image avec les pixels modifiés
					imgCarteBonus = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
					imgCarteBonus.setRGB(0, 0, width, height, pixels, 0, width);
				}
				g2.drawImage(imgCarteBonus, (int)this.getSize().getWidth()-48, (int)this.getSize().getHeight()-80, 48, 80, this);
			}
		}
	}
	
	public void mouseClicked(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();

		boolean tmp = this.ile1 != null;
	
		ArrayList<Ile> ensIles =  this.ctrl.getMetier(id).getListIle();
		
		for (int cpt = 0 ; cpt < ensIles.size() ; cpt ++ )
		{
			if( (int) (ensIles.get(cpt).getPosX() * this.getSize().getWidth()  * MULTIPLICATEUR_X) < (int) (x + 100 * this.getSize().getWidth()  * MULTIPLICATEUR_X) &&
			    (int) (ensIles.get(cpt).getPosX() * this.getSize().getWidth()  * MULTIPLICATEUR_X) > (int) (x - 100 * this.getSize().getWidth()  * MULTIPLICATEUR_X) &&
			    (int) (ensIles.get(cpt).getPosY() * this.getSize().getHeight() * MULTIPLICATEUR_Y) < (int) (y + 100 * this.getSize().getHeight() * MULTIPLICATEUR_Y) &&
			    (int) (ensIles.get(cpt).getPosY() * this.getSize().getHeight() * MULTIPLICATEUR_Y) > (int) (y - 100 * this.getSize().getHeight() * MULTIPLICATEUR_Y) &&
				pixelTransparent(x, y, ensIles.get(cpt)) && ensIles.get(cpt).getMouvementPossible())
			{
				if( this.ile1 != null && ensIles.get(cpt) != this.ile1 )
				{
					this.ctrl.getMetier(id).colorierArete(this.ile1.getId() + "", ensIles.get(cpt).getId() + "");
					this.ile1.setIsSelected(false);
					this.ile1 = null;
				}
				else
				{
					this.ile1 = ensIles.get(cpt);
					this.ile1.setIsSelected(true);
				}
			}
		}

		if(this.ile1 != null && tmp)
		{
			ile1.setIsSelected(false);
			this.ile1 = null;
		}

		if ((int) (this.getSize().getWidth() ) < (int) (x + 48) &&
		    (int) (this.getSize().getWidth() ) > (int) (x) &&
			(int) (this.getSize().getHeight()) < (int) (y + 80) &&
			(int) (this.getSize().getHeight()) > (int) (y) && 
			!this.ctrl.getMetier(id).getCarteBonus().getEstActivee())
		{

			if (ctrl.getCarteBonus().getImage().equals("BonusX2")) 
			{ 
				this.ctrl.getMetier(id).getCarteBonus().setIsSelected(true);
				this.ctrl.getMetier(id).eventBonusX2();
			}
			else 
			{
				this.ctrl.getMetier(id).getCarteBonus().setIsSelected(!this.ctrl.getMetier(id).getCarteBonus().getIsSelected());
			}
		}
		this.repaint();
	}
	
	public void mousePressed (MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered (MouseEvent e){}
	public void mouseExited  (MouseEvent e){}

	// Méthode qui permet de savoir si un pixel d'une image est transparent ou non
	public boolean pixelTransparent(int x, int y, Ile ile)
	{
		x /= this.getSize().getWidth()  * MULTIPLICATEUR_X;
		y /= this.getSize().getHeight() * MULTIPLICATEUR_Y;

		BufferedImage image = null;
		try
		{
			image = ImageIO.read(new File("./images/iles/" + ile.getNom() + ".png"));
		} 
		catch (Exception e) { }

		final int[] imageCoordonee = { ile.getPosXImage(),
			                           ile.getPosYImage()};

		if (x >= imageCoordonee[0]                     &&
		    y >= imageCoordonee[1]                     &&
		    x <  imageCoordonee[0] + image.getWidth () &&
		    y <  imageCoordonee[1] + image.getHeight()) 
			{
				int pixel = image.getRGB(x - imageCoordonee[0], y - imageCoordonee[1]);

				if (((pixel >> 24) & 0xFF) != 0) 
				{
					return true;
				}
			}
		return false;
	}
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package routage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



import org.miv.graphstream.graph.Edge;
import org.miv.graphstream.graph.Graph;
import org.miv.graphstream.graph.Node;
import org.miv.graphstream.graph.implementations.DefaultGraph;
import org.miv.graphstream.ui.GraphViewerRemote;



public class GUIAccueil extends JFrame implements ActionListener{
	protected JPanel base, milieu;
	protected JMenuBar barre;
	protected JMenu fich;
	protected JMenuItem nouv;
	protected JLabel tit_Table;
	protected JScrollPane txt_pan;
	protected JTextArea affi_Table;

	protected JPanel graphe_option;
	protected JComboBox choix_Noeud;
	protected JButton aff_tables;
	protected JButton aff_graphe;
	protected JComboBox depart_chemin;
	protected JComboBox arrive_chemin;
	protected JButton aff_chemin;

	protected Graph actuel ;
	protected Reseau res;

	/*
	 * Fenetre de creation de reseau
	 */
	protected JFrame creation;
	protected JPanel crea_node;
	protected JPanel crea_corps;
	protected JPanel crea_grp_Button;
	protected JLabel crea_tit_Table;
	protected JTextArea crea_affi_Table;

	protected JButton crea_ajoutNode;
	protected JButton crea_supprNode;
	protected JList crea_listNode ;
	protected Vector<String> crea_listText;
	protected JScrollPane crea_scrollNode;
	protected JLabel crea_titreNode;

	protected JPanel crea_panEdge;
	protected JPanel crea_grp_ButtEdge;
	protected JLabel crea_fleche, crea_lien;
	protected JComboBox crea_cible;
	protected JComboBox crea_source;
	protected JTextField crea_poid;
	protected JList crea_listEdge ;
	protected Vector<String> crea_listEdgeText;
	protected JButton crea_ajoutEdge;
	protected JButton crea_supprEdge;
	protected JScrollPane crea_scrollEdge;

	protected JPanel crea_grp_last;
	protected JButton crea_terminer;
	protected JButton crea_annulCrea;

	protected Graph crea_actuel ;
	protected ArrayList<Node> crea_noeuds ;

	/*
	 * Fenetre d'ajout de node
	 */
	protected JFrame ajoutNode;
	protected JPanel ajout_lien, ajout_node;
	protected JPanel ajout_grp_Button;
	protected JLabel ajout_tit_Table;
	protected JTextField ajout_nomNode;

	protected JButton ajout_annuler;
	protected JButton ajout_valider;
	protected JComboBox ajout_typeNode ;
	protected JLabel ajout_nom, ajout_type;

	protected Node ajout_actuel ;
	protected boolean ajout_termine ;

	protected GraphViewerRemote iGraphe;

	public GUIAccueil(){
		setName("Routage");
		setTitle("Routage");
		setSize(600,480);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		base = new JPanel(new BorderLayout());


		/*
		 * Creation de la barre de menu
		 */
		barre = new JMenuBar() ;
		fich = new JMenu("Fichier") ;
		nouv = new JMenuItem("Nouveau") ;
		nouv.addActionListener(this);
		fich.add(nouv);
		barre.add(fich);
		base.add(barre, BorderLayout.NORTH);

		/*
		 * Milieu
		 */
		milieu = new JPanel(new BorderLayout());
		tit_Table = new JLabel();

		tit_Table.setText("Table de routage :");
		milieu.add(tit_Table, BorderLayout.NORTH);

		affi_Table = new JTextArea();
		affi_Table.setSize(180, 300);
		affi_Table.setEditable(false);
		affi_Table.setFocusable(true);
		txt_pan = new JScrollPane(affi_Table);
		milieu.add(txt_pan, BorderLayout.CENTER);

		base.add(milieu, BorderLayout.CENTER);

		/*
		 *  Partie option d'affichage du graphe
		 */
		graphe_option = new JPanel(new GridLayout(3,2));
		choix_Noeud = new JComboBox();
		aff_tables = new JButton("Tables");
		depart_chemin = new JComboBox();
		arrive_chemin = new JComboBox();
		aff_chemin = new JButton("Plus court chemin");

		aff_tables.addActionListener(this);
		aff_chemin.addActionListener(this);

		graphe_option.add(choix_Noeud);
		graphe_option.add(aff_tables);
		graphe_option.add(depart_chemin);
		graphe_option.add(arrive_chemin);
		graphe_option.add(new JLabel(""));
		graphe_option.add(aff_chemin);

		base.add(graphe_option,BorderLayout.SOUTH);
		add(base);

	}

	public void addTableRoutage(String a){
		affi_Table.setText(affi_Table.getText()+a);
	}

	public void clearTableRoutage(){
		affi_Table.setText("");
	}

	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == (Object)nouv){
			GUICreation(); // Nouveau JFrame Principale pour crée les ordinateur et commutateur (crée un graphe)
			creation.setVisible(true);
		}

		if(e.getSource() == (Object)crea_ajoutNode){
			GUIAjout(); // Nouveau JFrame Pour ajouter les ordi et commutateur
			ajoutNode.setVisible(true);			
		}

		if(e.getSource() == (Object)ajout_annuler){
			ajout_actuel=null;
			ajoutNode.setVisible(false);
		}
		if(e.getSource() == (Object)ajout_valider){
			
			crea_actuel.addNode(ajout_nomNode.getText());

			crea_actuel.getNode(ajout_nomNode.getText()).addAttribute("type", ajout_typeNode.getSelectedIndex());
			crea_actuel.getNode(ajout_nomNode.getText()).addAttribute("nom", ajout_nomNode.getText());
                        // ajouter dans un vecteur
			crea_listText.add(crea_listText.size(),ajout_nomNode.getText() + " - " + ajout_typeNode.getSelectedItem());
			// ajout l ensemble des noeuds
                        crea_noeuds.add(crea_actuel.getNode(ajout_nomNode.getText()));
			crea_listNode.setListData(crea_listText);
                        //Ajouter a la liste avc JComboBox
			crea_source.addItem(ajout_nomNode.getText());
			crea_cible.addItem(ajout_nomNode.getText());
			ajoutNode.setVisible(false);

		}
		if(e.getSource() == (Object)crea_ajoutEdge){
			
			try{
				if(crea_actuel.getEdge(crea_source.getSelectedItem()+" - "+crea_cible.getSelectedItem()) == null && // si le poid n'existe pas
						crea_actuel.getEdge(crea_cible.getSelectedItem()+" - "+crea_source.getSelectedItem()) == null &&
						!crea_cible.getSelectedItem().equals(crea_source.getSelectedItem())){
					crea_actuel.addEdge(((String)crea_source.getSelectedItem()+" - "+crea_cible.getSelectedItem()),(String)crea_source.getSelectedItem(),(String)crea_cible.getSelectedItem());
					crea_actuel.getEdge(crea_source.getSelectedItem()+" - "+crea_cible.getSelectedItem()).setAttribute("poid", new Double(crea_poid.getText()));
					crea_listEdgeText.add(crea_listEdgeText.size(),crea_source.getSelectedItem() + " - " + crea_cible.getSelectedItem());
					crea_listEdge.setListData(crea_listEdgeText);
				}
			}catch(Exception ex){

			}
		}

		if(e.getSource() == (Object)crea_supprEdge){
			//System.out.println(ajout_nomNode.getText());
			if(crea_listEdge.getSelectedIndex() != -1){
                                 System.out.println(crea_listEdge.getSelectedIndex());
				crea_actuel.removeEdge(((String)crea_source.getSelectedItem()+" - "+crea_cible.getSelectedItem()));
				crea_listEdgeText.remove(crea_listEdge.getSelectedIndex());
                               	crea_listEdge.setListData(crea_listEdgeText);
			}
		}

		if(e.getSource() == (Object)crea_supprNode){
			if(crea_listNode.getSelectedIndex() != -1){
				//System.out.println("Suppression");
				//System.out.println(crea_noeuds.get(crea_listNode.getSelectedIndex()).getId());
				ArrayList<Integer> list = new ArrayList<Integer>();
				for(int i = 0 ; i < crea_listEdgeText.size() ; i ++){
					if((crea_listEdgeText.get(i).contains((crea_noeuds.get(crea_listNode.getSelectedIndex()).getId()+" -"))) || (crea_listEdgeText.get(i).contains(("- "+crea_noeuds.get(crea_listNode.getSelectedIndex()).getId())))){
						//System.out.println("on note");
						list.add(0, i);
						System.out.println(crea_listEdgeText.get(list.get(i))+" : "+list.get(i));
					}
				}
				//System.out.println("1 boucle");

				for(int i = 0 ; i < list.size() ; i ++){
					//System.out.println("on supprime");
					//System.out.println(crea_listEdgeText.get(list.get(i))+" : "+list.get(i));
					crea_actuel.removeEdge(crea_listEdgeText.get(list.get(i)));
					crea_listEdgeText.remove(list.get(i).intValue());
					//System.out.println(crea_listEdgeText.get(i));

				}
				//System.out.println("2 boucle");
				crea_listEdge.setListData(crea_listEdgeText);

				crea_source.removeItem(crea_noeuds.get(crea_listNode.getSelectedIndex()).getId());
				crea_cible.removeItem(crea_noeuds.get(crea_listNode.getSelectedIndex()).getId());

				crea_actuel.removeNode(crea_noeuds.get(crea_listNode.getSelectedIndex()).getId());
				crea_noeuds.remove(crea_listNode.getSelectedIndex());
				crea_listText.remove(crea_listNode.getSelectedIndex());
				crea_listNode.setListData(crea_listText);
			}
		}
		if(e.getSource() == (Object)crea_terminer){
			//System.out.println(ajout_nomNode.getText());
			if(crea_listEdgeText.size() > 0 & crea_listText.size() > 0){
				actuel = crea_actuel ;
				res = new Reseau("New");
				for(int i = 0 ; i < crea_noeuds.size(); i ++){
					if(crea_noeuds.get(i).getAttribute("type").equals(1)){
						depart_chemin.addItem(crea_noeuds.get(i).getId());
						arrive_chemin.addItem(crea_noeuds.get(i).getId());
					}else{
						choix_Noeud.addItem(crea_noeuds.get(i).getId());
					}
					res.addNoeud(crea_noeuds.get(i).getId(), crea_noeuds.get(i).getAttribute("type"));
				}

				Edge temp;
				for(Iterator<? extends Edge> it = actuel.getEdgeIterator() ; it.hasNext() ;){
					temp = it.next();
					res.addEdge(temp.getId(), temp.getNode0().getId(), temp.getNode1().getId(), temp.getAttribute("poid"));
				}

				iGraphe = res.display();
				iGraphe.setQuality(3);
				creation.setVisible(false);
			}
		}

		if(e.getSource() == (Object)crea_annulCrea){
			creation.setVisible(false);
		}

		if(e.getSource() == (Object)aff_tables){
			//System.out.println(ajout_nomNode.getText());
			if(choix_Noeud.getItemCount() > 0 ){
				//System.out.println("bouton en marche !");
				affi_Table.setText(res.getTableRoutage((String)choix_Noeud.getSelectedItem()));
				//System.out.println(res.getTableRoutage((String)choix_Noeud.getSelectedItem()));
			}
		}

		if(e.getSource() == (Object)aff_chemin){
			//System.out.println(ajout_nomNode.getText());
			res.initColorEdge();
			if(depart_chemin.getSelectedIndex() != -1 & arrive_chemin.getSelectedIndex() != -1){
				res.affichePlusCourtChemin((String)depart_chemin.getSelectedItem(), (String)arrive_chemin.getSelectedItem());
			}
		}


	}

	public void GUIAjout(){
		ajoutNode = new JFrame();
		ajoutNode.setName("Ajout d'un noeud");
		ajoutNode.setTitle("Ajout d'un noeud");
		ajoutNode.setSize(300,150);
		ajoutNode.setResizable(false);
		ajoutNode.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ajoutNode.setLayout(new GridLayout(3,2));
		ajout_node = new JPanel(new BorderLayout());

		ajout_termine = false;

		/*
		 * Gestion de Noeud
		 */
		ajout_nom=new JLabel();
		ajout_nom.setText("Nom :");
		ajoutNode.add(ajout_nom);

		ajout_nomNode = new JTextField();
		ajoutNode.add(ajout_nomNode);

		ajout_type=new JLabel();
		ajout_type.setText("Type :");

		ajoutNode.add(ajout_type);

		ajout_typeNode = new JComboBox();
		ajout_typeNode.addItem("Commutateur");
		ajout_typeNode.addItem("Ordinateur");

		ajoutNode.add(ajout_typeNode);

		ajout_grp_Button = new JPanel(new GridLayout(1,2));
		ajout_valider = new JButton("Ajouter") ;
		ajout_valider.addActionListener(this);
		ajout_annuler = new JButton("Annuler") ;
		ajout_annuler.addActionListener(this);
		ajoutNode.add(ajout_annuler);
		ajoutNode.add(ajout_valider);

	}

	public void GUICreation(){
		creation=new JFrame();
		creation.setName("Nouveau Graphe");
		creation.setTitle("Nouveau Graphe");
		creation.setSize(500,400);
		creation.setResizable(false);
		creation.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		creation.setLayout(new BorderLayout());
		crea_node = new JPanel(new BorderLayout());

		crea_corps=new JPanel(new GridLayout(2,1));
		/*
		 * Gestion de Noeud
		 */

		crea_grp_Button = new JPanel(new GridLayout(3,1));
		crea_ajoutNode = new JButton("Ajout") ;
		crea_ajoutNode.addActionListener(this);
		crea_supprNode = new JButton("Suppression") ;
		crea_supprNode.addActionListener(this);

		crea_grp_Button.add(crea_ajoutNode);
		crea_grp_Button.add(new JLabel(""));
		crea_grp_Button.add(crea_supprNode);

		crea_titreNode=new JLabel();
		crea_titreNode.setText("Gestion de Noeud :");
		crea_node.add(crea_titreNode,BorderLayout.NORTH);

		crea_listText = new Vector<String>();
		crea_listNode = new JList(crea_listText);

		crea_noeuds = new ArrayList<Node>();
		crea_scrollNode = new JScrollPane(crea_listNode);
		crea_node.add(crea_scrollNode, BorderLayout.CENTER);

		crea_node.add(crea_grp_Button, BorderLayout.EAST);

		crea_corps.add(crea_node);

		/*
		 * Gestion des Edge
		 */
		crea_lien = new JLabel("Gestion des liens :");
		crea_panEdge = new JPanel(new BorderLayout());
		crea_grp_ButtEdge = new JPanel(new GridLayout(6,1));
		crea_fleche = new JLabel("<->");
		crea_cible = new JComboBox();
		crea_source= new JComboBox();
		crea_listEdgeText = new Vector<String>();
		crea_listEdge = new JList(crea_listEdgeText );

		crea_ajoutEdge = new JButton("Ajouter");
		crea_supprEdge = new JButton("Supprimer");
		crea_scrollEdge = new JScrollPane(crea_listEdge);
		crea_poid = new JTextField();

		crea_ajoutEdge.addActionListener(this);
		crea_supprEdge.addActionListener(this);

		crea_grp_ButtEdge.add(crea_source);
		crea_grp_ButtEdge.add(crea_fleche);
		crea_grp_ButtEdge.add(crea_cible);
		crea_grp_ButtEdge.add(crea_poid);
		crea_grp_ButtEdge.add(crea_ajoutEdge);
		crea_grp_ButtEdge.add(crea_supprEdge);

		crea_panEdge.add(crea_lien, BorderLayout.NORTH);
		crea_panEdge.add(crea_scrollEdge, BorderLayout.CENTER);
		crea_panEdge.add(crea_grp_ButtEdge, BorderLayout.EAST);

		crea_corps.add(crea_panEdge);

		creation.add(crea_corps, BorderLayout.CENTER);

		/*
		 * Creation des boutons de confirmation de creation ou d'annulation !
		 */
		crea_grp_last = new JPanel(new GridLayout(1,5));
		crea_terminer = new JButton("Terminer");
		crea_annulCrea = new JButton("Annuler");

		crea_terminer.addActionListener(this);
		crea_annulCrea.addActionListener(this);

		crea_grp_last.add(new JLabel(" "));
		crea_grp_last.add(crea_annulCrea);
		crea_grp_last.add(new JLabel(" "));
		crea_grp_last.add(crea_terminer);
		crea_grp_last.add(new JLabel(" "));

		creation.add(crea_grp_last, BorderLayout.SOUTH);

		crea_actuel=new DefaultGraph();

	}

	public Graph getActuel() {
		return actuel;
	}

	public void setActuel(Graph actuel) {
		this.actuel = actuel;
	}
}

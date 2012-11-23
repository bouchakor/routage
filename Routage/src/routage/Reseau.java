/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package routage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.miv.graphstream.graph.Edge;
import org.miv.graphstream.graph.Graph;
import org.miv.graphstream.graph.Node;
import org.miv.graphstream.graph.implementations.DefaultGraph;
import org.miv.graphstream.ui.GraphViewerRemote;
import org.miv.graphstream.algorithm.Dijkstra;

import org.miv.graphstream.graph.Graph;
import org.miv.graphstream.graph.Node;
import org.miv.graphstream.graph.implementations.DefaultGraph;
import org.miv.graphstream.ui.GraphViewerRemote;


public class Reseau {
	protected Graph graphe;
	protected ArrayList<Edge> arteres;
	protected String nom ;

	public Reseau(){
		graphe = new DefaultGraph( false, true );
		arteres = new ArrayList<Edge>() ;
		nom = "Default";
	}

	public Reseau(String n){
		graphe = new DefaultGraph( false, true );
		arteres = new ArrayList<Edge>() ;
		nom = n;
	}

	public void addNoeud(String n, Object i){
		graphe.addNode( n );
		graphe.getNode(n).addAttribute("nom", n);
		graphe.getNode(n).addAttribute("type", i);
		if(i.equals(1)){
			graphe.getNode(n).addAttribute("color", "blue");
		}else{
			graphe.getNode(n).addAttribute("color", "green");
		}
		graphe.getNode(n).addAttribute("label", n);
	}

	public void addEdge(String n, String a, String b, Object poid){
		graphe.addEdge(n, a, b);
		graphe.getEdge(n).addAttribute("poid", poid);
		graphe.getEdge(n).addAttribute("label", poid);
		graphe.getEdge(n).addAttribute("color", "black");
	}

	public List<Edge> plusCourtchemin(String a, String b){
		Dijkstra d = new Dijkstra(graphe, Dijkstra.Element.valueOf("edge"), "poid", graphe.getNode(a));
		return d.getEdgeSetShortestPaths(graphe.getNode(b));
	}

	public void initColorEdge(){
		for(Iterator <? extends Edge> it = graphe.getEdgeIterator() ; it.hasNext() ;)
			it.next().setAttribute("color", "black");

	}

	public void affichePlusCourtChemin(String a, String b){
		//System.out.println(a +" -> "+ b);
		List<Edge> list = plusCourtchemin(a, b);
		for(int i = 0 ; i < list.size(); i++){
			graphe.getEdge(list.get(i).getId()).setAttribute("color", "yellow");
		}

	}

	private String[] sortTrajetVoisin(String a, String b){
		String [] result ;
		Node temp;
		Edge test;
		double inter, somme;
		List<Edge> path;
		Iterator<? extends Node> itVoisins = graphe.getNode(a).getNeighborNodeIterator();
		ArrayList<Node> voisins = new ArrayList<Node>();
		ArrayList<Double> score = new ArrayList<Double>();
		// Recherche de voisins
		int cpt =0 ;
		//System.out.println("Destination : "+b);
		while(itVoisins.hasNext()){
			temp=itVoisins.next();
			if(!temp.getAttribute("type").equals(1))
				voisins.add(temp);
			//System.out.println("voisin "+(cpt)+ voisins.get(cpt).getId());
			cpt++;
		}
		Graph sousGraphe ;

		sousGraphe = new DefaultGraph();

		Node n;
		for(Iterator <? extends Node> it = graphe.getNodeIterator() ; it.hasNext() ;){
			n = it.next();
			if(!n.getAttribute("type").equals(1))
				sousGraphe.addNode(n.getId());
		}
		Edge t ;
		for(Iterator <? extends Edge> it = graphe.getEdgeIterator() ; it.hasNext() ;){
			t = it.next();
			if(!t.getNode0().getAttribute("type").equals(1) && !t.getNode1().getAttribute("type").equals(1))
				sousGraphe.addEdge(t.getId(), t.getNode0().getId(), t.getNode1().getId()).addAttribute("poid", t.getAttribute("poid"));
		}

		sousGraphe.removeNode(a);
		for(int i = 0 ; i<voisins.size() ; i++){
			inter = 0;
			Dijkstra d = new Dijkstra(sousGraphe, Dijkstra.Element.valueOf("edge"), "poid", sousGraphe.getNode(voisins.get(i).getId()));
			path = d.getEdgeSetShortestPaths(sousGraphe.getNode(b));
			somme = 0;
			try {
				for(int j = 0 ; j < path.size() ; j ++){
					somme+=(Double)path.get(j).getAttribute("poid");
				}
			}
			catch(Exception ex){

			}
			// recherche de la valeur départ / noeud voisin
			for(Iterator<? extends Edge> it = graphe.getEdgeIterator(); it.hasNext();){
				test=it.next();
				if((test.getNode0()== graphe.getNode(a)) && (test.getNode1()== graphe.getNode(voisins.get(i).getId()))
						||((test.getNode1()== graphe.getNode(a)) && (test.getNode0()== graphe.getNode(voisins.get(i).getId())))){
					inter=(Double)test.getAttribute("poid");
				}
			}
			score.add(somme+inter);
		}

		/*
		 *  Trie des voisins par ordre croissant
		 */
		int min ;
		result = new String[score.size()] ;
		int taille = voisins.size();
		for(int j = 0 ; j < taille ; j ++){
			min = 0;
			for(int i = 0 ; i < score.size() ; i ++){
				if(i!=0){
					if(score.get(min)>= score.get(i)){
						min = i ;
					}
				}
			}
			//System.out.println(j + " : "+voisins.get(min).getId() + " " + score.get(min));
			result[j]=voisins.get(min).getId();

			score.remove(min);
			voisins.remove(min);

		}
		/*
		for(int i = 0 ; i < taille ; i++){
			voisins.remove(i);
		}

		result = new String[voisins.size()] ;
		for(int i = 0 ; i < voisins.size() ; i ++){
			System.out.println(voisins.get(i).getAttribute("nom"));
			result[i]=(String)voisins.get(i).getAttribute("nom");
		}*/

		return result;
	}
	public GraphViewerRemote display(){

		return graphe.display();
	}

	public String getTableRoutage(String a){
		String result = "Table de "+a+"\n";

		Node actu;
		String[] retour;

		for(Iterator<? extends Node> it = graphe.getNodeIterator(); it.hasNext();){
			actu = it.next();

			if(actu.getId().compareTo(a) != 0 && !actu.getAttribute("type").equals(1)){
				retour = sortTrajetVoisin(a, actu.getId());
				result += actu.getAttribute("nom")+ " |";
				for(int j = 0 ; j < retour.length ; j ++){
					result += "   "+retour[j];
				}
				result+="\n";
			}
		}
		return result ;
	}

}



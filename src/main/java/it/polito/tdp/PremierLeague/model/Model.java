package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;


public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> idMap;
	private List<Player> soluzioneMigliore;
	private double gradoTitolarieta;
	
	public Model() {
		
		
		dao = new PremierLeagueDAO();
		idMap = new HashMap<>();
		dao.listAllPlayers(idMap);
		
		
	}
	
	
	public void creaGrafo(double numero) {
		
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//vertici
		
		Graphs.addAllVertices(this.grafo, dao.getVertici(numero, idMap));
		
		//archi
		
		for(Adiacenza a: dao.getAllAdiacenze(idMap)) {
			
			if(this.grafo.containsVertex(a.getP1()) && this.grafo.containsVertex(a.getP2())) {
			
				if(a.getPeso() > 0) {
				 //da A a B
				
					Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), a.getPeso());
				
				
				}else if(a.getPeso() < 0)
				{
				
				//da B a A
				
					double pesoMag = -1*(a.getPeso());
					Graphs.addEdgeWithVertices(this.grafo, a.getP2(), a.getP1(), pesoMag);
				
				
				}
			
			}
			
		}
		
		
		
	}
	
	public int getNVertici() {
		return grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return grafo.edgeSet().size();
	}
	
	public Graph<Player,DefaultWeightedEdge> getGrafo(){
		
		return grafo;
	}
	
	
	//il bottone “Top-Player”, si trovi il giocatore che abbia “battuto”, in
	//termini di minuti giocati, il numero maggiore di avversari. Si stampino,
	//contestualmente, gli avversari battuti ordinati in modo decrescente di D.
	
	
	public TopPlayer getTopPlayer() {
		
		if(grafo == null) {
			return null;
		}
		
		
		Player best = null;
		double maxAvversari = -1;
		
		//top player 
		
		for(Player p: this.grafo.vertexSet()) {
			
			if (this.grafo.outDegreeOf(p) > maxAvversari) { // restituisce il numero di archi uscenti da un determinato vertice
				
				maxAvversari = grafo.outDegreeOf(p);
				best = p;
			}
			
		}
		
		//avversari, tra gli avversari battuti --> ordine decrescente di minuti giocati (peso) 
		
		TopPlayer tp = new TopPlayer();
		tp.setTop(best);
		
		List<Avversario> avversari = new ArrayList<>();
		
		for(DefaultWeightedEdge edge : grafo.outgoingEdgesOf(tp.getTop())) {
			avversari.add(new Avversario(grafo.getEdgeTarget(edge), (int) grafo.getEdgeWeight(edge)));
		}
		
		Collections.sort(avversari);
		tp.setAvversari(avversari);
		
		
		return tp;
		
	
		
	}


	public List<Player> doRicorsione(Integer numero) {
		
		soluzioneMigliore = null;
		
		gradoTitolarieta = 0.0;
		
		List<Player> parziale = new ArrayList<>();
		
		List<Player> partenza = new ArrayList<>(grafo.vertexSet());
		
		cerca(parziale, partenza, numero);
		
		return soluzioneMigliore;
		
		
		
	}


	private void cerca(List<Player> parziale, List<Player> partenza, Integer k) {
		
		
		//caso terminale 
		
		double gradoParziale = calcolaGrado(parziale);
		
		if(parziale.size() == k) {
			
			//controllo gradoTitolarietà
			
			if(gradoParziale > gradoTitolarieta) {
				
				soluzioneMigliore = new ArrayList<>(parziale);
				gradoTitolarieta = gradoParziale;
				
			}
			
			return;
		}
		
		
		//generazione sotto problemi : 
		/*Quando un giocatore entra a far parte del dream-team, tutti i giocatori che quest’ultimo ha “battuto”, in
			termini di minuti giocati, durante la stagione non possono più essere aggiunti alla squadra.
		 * 
		 */
		
		for(Player p : partenza) {
			
			if(!parziale.contains(p)) {
				
				parziale.add(p);
				
				//i "battuti" di p non possono più essere considerati
				  
				List<Player> remainingPlayers = new ArrayList<>(partenza);
				
				remainingPlayers.removeAll(Graphs.successorListOf(grafo, p));
				
				cerca(parziale, remainingPlayers, k);
				
				parziale.remove(p);
				
			}
		}
		
		
		
		
		
		
	}


	private double calcolaGrado(List<Player> parziale) {
		
		//calcolo il grado di titolarietà di una soluzione 
		
		/*Il grado di titolarità di ogni singolo giocatore, in particolare, è dato dalla differenza
		 * del peso dei suoi archi uscenti (i minuti che ha giocato in più dei suoi avversari) con
		 * il peso degli archi entranti (i minuti che ha giocato in meno).
		 * 
		 */
		double uscenti = 0.0;
		double entranti = 0.0;
		double differenza = 0.0;
		
		for(Player p : parziale) {
			
			for(DefaultWeightedEdge edge : grafo.outgoingEdgesOf(p)) {
				
				uscenti += grafo.getEdgeWeight(edge);
			}
			
			for(DefaultWeightedEdge edge2: grafo.incomingEdgesOf(p)) {
				
				entranti += grafo.getEdgeWeight(edge2);
			}
			
		}
		
		differenza = uscenti - entranti ;
		
		
		return differenza;
	}

}

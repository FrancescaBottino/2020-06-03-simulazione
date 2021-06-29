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

}

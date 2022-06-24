package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	
	private Map<Integer, Actor> idMap;
	
	private Graph<Actor, DefaultWeightedEdge> grafo;
	
	public Model() {
		this.dao = new ImdbDAO();
	}
	
	public List<String> getGenres() {
		return this.dao.getGenres();
	}
	
	public void creaGrafo(String genre) {
		
		this.grafo = new SimpleWeightedGraph<Actor, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiungo vertici
		this.idMap = new HashMap<Integer, Actor>();
		this.dao.getVertici(genre, idMap);
		
		Graphs.addAllVertices(grafo, idMap.values());
		
//		System.out.println("#vertici: "+grafo.vertexSet().size());
		
		//aggiungo archi
		
		for(Adiacenza a : this.dao.getAdiacenze(genre, idMap)) {
			Graphs.addEdgeWithVertices(grafo, a.getA1(), a.getA2(), a.getPeso());
		}
		
//		System.out.println("#archi: "+grafo.edgeSet().size());
		
		
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public boolean grafoCreato() {
		if(this.grafo == null)
			return false;
		else
			return true;
	}
	
	public List<Actor> getActors() {
		List<Actor> result = new ArrayList<Actor>(this.idMap.values());
		
		Collections.sort(result);
		
		return result;

	}
	
	public List<Actor> doAttoriSimili(Actor partenza) {
		
		List<Actor> result = new ArrayList<Actor>();
		
		BreadthFirstIterator<Actor, DefaultWeightedEdge> it = new BreadthFirstIterator<Actor, DefaultWeightedEdge>(grafo, partenza);
		
		while(it.hasNext()) {
			result.add(it.next());
		}
		
		result.remove(0);
		
		//sort
		Collections.sort(result);
		
		return result;
		
	}
	
	public String simula(int n) {
		
		Simulatore sim = new Simulatore(n, this.grafo);
		
		sim.init();
		sim.run();
		
		String result = "Simulazione completata.\nAttori intervistati:";
		for(Actor a : sim.getIntervistati()) {
			result += "\n"+a;
		}
		
		result += "\n\nNumero di pause: ";
		
		result += sim.getNumPause();
		
		return result;
		
	}

}

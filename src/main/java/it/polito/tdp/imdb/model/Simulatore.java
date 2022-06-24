package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.imdb.model.Event.EventType;

public class Simulatore {
	
	//parametri input
	private int n;
	
	
	//parametri di output
	private List<Actor> intervistati;
	private int numPause;
	
	//stato del mondo
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private int genereBool; // 1 uomo, 2 donna, 0 inizializzazione
	
	//coda degli eventi
	private PriorityQueue<Event> queue;
	
	public Simulatore(int n, Graph<Actor, DefaultWeightedEdge> grafo) {
		this.n = n;
		this.grafo = grafo;
	}
	
	public void init() {
		this.intervistati = new ArrayList<Actor>();
		this.numPause = 0;
		this.genereBool = 0;
		
		this.queue = new PriorityQueue<>();
		
		//primo giorno
		Actor actor = this.scegliAttoreDaIntervistare();
		
		this.queue.add(new Event(EventType.INTERVISTA, 1, actor));		
		
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = queue.poll();
			
			if(e.getGiorno()<=n) {
//				System.out.println(e.getGiorno()+" "+e.getType()+" "+e.getActor());
				processEvent(e);
			}
		}
	}

	private void processEvent(Event e) {
		
		switch (e.getType()) {
		case INTERVISTA:
			
			//intervisto l'attore
			
			this.intervistati.add(e.getActor());
			
			//pausa??
			String genere = e.getActor().getGender();
			
			boolean provarePausa = false;
			if(genere.equals("M")) {
				if(this.genereBool==1) { //avevo intervistato un maschio
					provarePausa = true;
				}
				
				this.genereBool = 1;
				
			} else {
				if(this.genereBool==2) {
					provarePausa = true;
				}
				
				this.genereBool = 2;
			}
			
			boolean pausaEffettiva = false;
			if(provarePausa) {
				
				int random = ((int) Math.random()*100+1);
				
				if(random<=90) {
					pausaEffettiva = true;
					this.queue.add(new Event(EventType.PAUSA, e.getGiorno()+1, null));
				}
			}
			
			if(!pausaEffettiva) {
				//scelgo il prossimo da intervistare
				
				int random = ((int) Math.random()*100+1);
				
				Actor actor = null;
				
				if(random<=60) {
					
					actor = this.scegliAttoreDaIntervistare();
					
				} else {
					
					actor = this.scegliAttoreGradoMassimo(e.getActor());
					if(actor==null) {
						actor = this.scegliAttoreDaIntervistare();
					}
				}
				
				if(actor!=null) {
					this.queue.add(new Event(EventType.INTERVISTA, e.getGiorno()+1, actor));
				}
				
			} else {
				
			}
			
			break;

		case PAUSA:
			
			this.numPause++;
			this.genereBool=0;
			this.queue.add(new Event(EventType.INTERVISTA, e.getGiorno()+1, this.scegliAttoreDaIntervistare()));
			
			break;
		}
		
	}
	
	private List<Actor> getListaDaIntervistare() {
		
		List<Actor> lista = new ArrayList<Actor>(this.grafo.vertexSet());
		
		lista.removeAll(intervistati);
		
		return lista;
		
	}
	
	private Actor scegliAttoreDaIntervistare() {
		int size = this.getListaDaIntervistare().size();
		
		if(size==0)
			return null;
		
		int random = ((int) (Math.random()*size));
		
		
		return this.getListaDaIntervistare().get(random);
	}
	
	private Actor scegliAttoreGradoMassimo(Actor actor) {
		
		List<Actor> migliori = new ArrayList<Actor>();
		
		int max = -1;
		for(DefaultWeightedEdge edge : grafo.edgesOf(actor)) {
			if(grafo.getEdgeWeight(edge)>max) {
				migliori = new ArrayList<Actor>();
				migliori.add(Graphs.getOppositeVertex(grafo, edge, actor));
				max = (int) grafo.getEdgeWeight(edge);
			} else if(grafo.getEdgeWeight(edge)==max) {
				migliori.add(Graphs.getOppositeVertex(grafo, edge, actor));
			}
		}
		
		migliori.removeAll(intervistati);
		
		if(migliori.size()==0)
			return null;
		
		int random = ((int) (Math.random()*migliori.size()));
		
		return migliori.get(random);
		
	}

	public List<Actor> getIntervistati() {
		return intervistati;
	}

	public int getNumPause() {
		return numPause;
	}
	

}

package it.polito.tdp.imdb.model;

public class Event implements Comparable<Event>{
	
	public enum EventType {
		INTERVISTA,
		PAUSA
	}
	
	private EventType type;
	private int giorno;
	private Actor actor;
	
	public Event(EventType type, int giorno, Actor actor) {
		super();
		this.type = type;
		this.giorno = giorno;
		this.actor = actor;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public int getGiorno() {
		return giorno;
	}

	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	@Override
	public int compareTo(Event o) {
		return this.giorno - o.giorno;
	}
	
	

}

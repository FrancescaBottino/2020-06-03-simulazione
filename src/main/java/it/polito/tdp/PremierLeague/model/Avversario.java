package it.polito.tdp.PremierLeague.model;

public class Avversario implements Comparable<Avversario>{
	
	private Player player;
	private double peso;
	
	public Avversario(Player player, double peso) {
		super();
		this.player = player;
		this.peso = peso;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	@Override
	public int compareTo(Avversario other) {
		
		if(this.getPeso() > other.getPeso()) {
			return -1;
		}
		if(this.getPeso() > other.getPeso()) {
			return +1;
		}
		
		return 0;
	}

	@Override
	public String toString() {
		return "Avversario:" + player + ", peso = " + peso + "\n";
	}
	
	

}

package HighScore;

public class Score {
	private String name;
	private int place;
	private double score;
	
	public Score(int pl, String nm, double sc){
		name = nm;
		place = pl;
		score = sc;
	}
	
	public String toString(){
		String res = this.place + " " + this.name + " " + this.score;
		return res;
	}

}

package HighScore;

public class Score {
	private String name;
	private double score;
	
	public Score( String nm, double sc){
		name = nm;
		score = sc;
	}
	
	public String toString(){
		String res = this.name + " " + this.score;
		return res;
	}
	
	public double getScr()
	{
		return score;
	}

}

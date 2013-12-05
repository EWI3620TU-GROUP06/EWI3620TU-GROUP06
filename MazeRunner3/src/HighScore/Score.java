package HighScore;

public class Score {
	private String name;
	private int score;
	
	public Score( String nm, int sc){
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
	
	public String getName()
	{
		return name;
	}
	
	public boolean equals(Object other)
	{
		if(other instanceof Score)
		{
			Score that = (Score) other;
			return this.name.equals(that.name) && this.score == that.score;
		}
		return false;
	}

}

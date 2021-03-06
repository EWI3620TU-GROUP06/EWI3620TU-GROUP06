package HighScore;

/**
 * the class Score gives us the oppertunity to make an object score which will be saved in the sql database.
 */
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
	
	public int getScr()
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

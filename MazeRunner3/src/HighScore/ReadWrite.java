package HighScore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadWrite {

	public static ArrayList<Score> highscores = new ArrayList<Score>();
	public static Score mostRecentScore = null;

	public static void readHighscore(){
		highscores = new ArrayList<Score>();
		String filenaam = "highscores.txt";
		try{
			Scanner sc = new Scanner(new File(filenaam));
			String name;
			int scr;
			sc.next();
			name = sc.next();
			scr = sc.nextInt();
			mostRecentScore = new Score(name, scr);
			
			while (sc.hasNext()){
				sc.next();
				name = sc.next();
				scr = sc.nextInt();
				Score score = new Score(name, scr);
				highscores.add(score);
			}
			sc.close();
		}
		catch (Exception e){
			System.out.println("HighscoresFile not found");
			e.printStackTrace();
		}
	}

	public static void writeHighscore(ArrayList<Score> highScore){
		String filenaam = "highscores.txt";
		try{
			FileWriter fileWriter = new FileWriter(filenaam);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write(0 + " " + mostRecentScore.toString());
			bufferedWriter.newLine();

			for(int i = 0; i < highScore.size(); i++){
				bufferedWriter.write((i + 1) + " " + highScore.get(i).toString());
				bufferedWriter.newLine();
			}
			bufferedWriter.close();

		}
		catch(Exception e){
			System.out.println("FileNotFound");
		}
	}

	public static void addScore(String naam, int scr)
	{
		readHighscore();
		Score newScore = new Score(naam, scr);
		boolean added = false;
		mostRecentScore = newScore;
		for(int i = 0; i < highscores.size(); i++)
		{
			if(highscores.get(i).getScr() < scr){
				highscores.add(i, newScore);
				added = true;
				break;
			}
		}
		if(!added)
			highscores.add(newScore);
		for(Score s : highscores)
			System.out.println(s.toString());
		writeHighscore(highscores);
	}
	
	public static int indexOf(Score score)
	{
		int res = -1;
		for(int i = 0; i < highscores.size(); i++)
		{
			if(score.equals(highscores.get(i)))
				res = i;
		}
		return res;
	}

}

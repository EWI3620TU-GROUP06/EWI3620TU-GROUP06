package HighScore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadWrite {
	
	public static ArrayList<Score> highscores = new ArrayList<Score>();
	
	public static ArrayList<Score> readHighscore(){
		
		String filenaam = "highscores.txt";
		try{
		Scanner sc = new Scanner(new File(filenaam));
			String name;
			double scr;
			while (sc.hasNext()){
					sc.next();
					name = sc.next();
					scr = sc.nextDouble();
					Score score = new Score(name, scr);
					highscores.add(score);
				
			}
			sc.close();
		}
		catch (Exception e){
			System.out.println("HigscoresFile not found");
		}
		return highscores;
	}
	
	public static void writeHighscore(ArrayList<Score> highScore){
		String filenaam = "highscorestest.txt";
		try{
			FileWriter fileWriter = new FileWriter(filenaam);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				for(int i = 0; i < highScore.size(); i++){
					bufferedWriter.write((i+ 1) + highScore.get(i).toString());
					System.out.println(highScore.get(i).toString());
					bufferedWriter.newLine();
				}
			bufferedWriter.close();
			
		}
		catch(Exception e){
			System.out.println("FileNotFound");
		}
	}
	
	public static void addScore(String naam, double scr)
	{
		readHighscore();
		for(int i = 0; i < highscores.size(); i++)
		{
			if(highscores.get(i).getScr() < scr){
				highscores.add(i, new Score(naam, scr));
				break;
			}
		}
		writeHighscore(highscores);
	}

}

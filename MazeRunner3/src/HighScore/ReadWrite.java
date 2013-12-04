package HighScore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;








public class ReadWrite {
	
	public static ArrayList<Score> highscores = new ArrayList<Score>();
	
	public static ArrayList<Score> ReadHigscore(){
		
		String filenaam = "highscores.txt";
		try{
		Scanner sc = new Scanner(new File(filenaam));
			int place;
			String name;
			double scr;
			while (sc.hasNext()){
					place = sc.nextInt();
					name = sc.next();
					scr = sc.nextDouble();
					Score score = new Score(place, name, scr);
					highscores.add(score);
				
			}
			sc.close();
		}
		catch (Exception e){
			System.out.println("HigscoresFile not found");
		}
		return highscores;
	}
	
	public static void WriteHigscore(ArrayList<Score> highScore){
		String filenaam = "highscorestest.txt";
		try{
			FileWriter fileWriter = new FileWriter(filenaam);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				for(int i = 0; i < highScore.size(); i++){
					bufferedWriter.write(highScore.get(i).toString());
					System.out.println(highScore.get(i).toString());
					bufferedWriter.newLine();
				}
			bufferedWriter.close();
			
		}
		catch(Exception e){
			System.out.println("FileNotFound");
		}
	}

}

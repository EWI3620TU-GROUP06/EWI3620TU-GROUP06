package HighScore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;






public class ReadWrite {
	
	private static String line;
	public static ArrayList<String> highscores = new ArrayList<String>();
	
	public static ArrayList<String> ReadHigscore(){
		
		String filenaam = "highscores.txt";
		try{
		FileReader fileReader = new FileReader(filenaam);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null){
					System.out.println(line);
					highscores.add(line);
				
			}
			bufferedReader.close();
		}
		catch (Exception e){
			System.out.println("HigscoresFile not found");
		}
		return highscores;
	}
	
	public static void WriteHigscore(ArrayList<String> highScore){
		String filenaam = "highscorestest.txt";
		try{
			FileWriter fileWriter = new FileWriter(filenaam);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				for(int i = 0; i < highScore.size(); i++){
					bufferedWriter.write(highScore.get(i));
					System.out.println(highScore.get(i));
					bufferedWriter.newLine();
				}
			bufferedWriter.close();
			
		}
		catch(Exception e){
			System.out.println("FileNotFound");
		}
	}

}

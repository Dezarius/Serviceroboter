package serviceroboter;

import java.util.Arrays;

import lejos.hardware.lcd.LCD;

public class Farben {
	private String farben [];
	
	public Farben(){
		farben = new String [10];
	}
	/**
	 * Fügt Farbe zum Farbarray hinzu
	 * @param Farbe die zum Array hinzugefügt werden soll
	 * @return 
	 */
	public void addColor(String color){
		String farben_new [] = Arrays.copyOf(this.farben, this.farben.length + 1 );
		farben_new [this.farben.length] = color;
		this.farben = farben_new;
	}

	/**
	 * Gibt die gelesen Farben auf dem ROboterdisplay aus
	 * @param
	 * @return 
	 */
	public void print(){
		String str = "";
		//System.out.println("Folgende Farbcodes wurden erkannt " + "\n");
		for(String color : this.farben){
			str += (color+ "  "); 
			//System.out.println("Farbecode:" + flt);
			
		}
		LCD.drawString(str, 1, 1);
	}
	/**
	 * Gibt das Array mit allen gelesenen Farben zurück
	 * @param
	 * @return Farbarray 
	 */
	public String [] getColors (){
		return this.farben;
	}

}

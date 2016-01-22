package serviceroboter;

import java.util.Arrays;

import lejos.hardware.lcd.LCD;

public class Farben {
	private String farben [];
	
	public Farben(){
		farben = new String [10];
	}
	
	public void addColor(String color){
		String farben_new [] = Arrays.copyOf(this.farben, this.farben.length + 1 );
		farben_new [this.farben.length] = color;
		this.farben = farben_new;
	}

	
	public void print(){
		String str = "";
		//System.out.println("Folgende Farbcodes wurden erkannt " + "\n");
		for(String color : this.farben){
			str += (color+ "  "); 
			//System.out.println("Farbecode:" + flt);
			
		}
		LCD.drawString(str, 1, 1);
	}
	public String [] getColors (){
		return this.farben;
	}

}

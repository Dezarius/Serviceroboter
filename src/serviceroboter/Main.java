package serviceroboter;

import lejos.utility.Delay;

public class Main {
	
	static Motoren motoren;
	static Sensoren sensoren;

	public static void main(String[] args) {
		
		sensoren = new Sensoren();
		motoren = new Motoren(sensoren);
		
		
		while (true) {
			float values[] = sensoren.getValues();
			loop();
			if(values[3] == 1){
				break;
			}
		}
		
	}

	private static void loop() {
		float values[] = sensoren.getValues();
		motoren.forward();
		motoren.diviation();
		/*
		if(values[1] == 7){
			motoren.forward();
			motoren.diviation();
		}
		*/
		
	}
}

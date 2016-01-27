package serviceroboter;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Main {
	
	static Motoren motoren;
	static Sensoren sensoren;	
	static Hindernis hindernis;
	static Farben color;
	
	private static int farbe = 6;
	private static boolean tonne = false;
	private static boolean ausrichtung = false;
	private static boolean ranfahren = false;
	private static boolean start = false;
	private static String tonnenFarbe = null;
	private static boolean foundTonne = false;
	private static boolean iter = false;
	
	private static boolean search = false;
	private static int kurve = -1;
	private static float oldDist;

	/**
	 * Setzt Roboter in Betrieb wenn der Button gedrückt wurde und
	 * beendet ihn beim erneuten Drücken
	 * @param - 
	 * @return 
	 */
	public static void main(String[] args) {
		
		sensoren = new Sensoren();
		motoren = new Motoren(sensoren);
		hindernis = new Hindernis(motoren,sensoren);
		color = new Farben();
		float values[];
		
		values = sensoren.getValues();
		
		if (values[1] == farbe){
			LCD.drawString("Bereit)", 1, 1);
			Sound.twoBeeps();
		}else {
			LCD.drawString("Keine weisse Linie", 1, 1);
			Sound.beep();
		}
		
		while (!start){
			values = sensoren.getValues();
			if (values[3] == 1){
				LCD.clear();
				start = true;
				Delay.msDelay(500);
			}
		}
		
		while (true) {
			values = sensoren.getValues();
			loop(values);
			if(values[3] == 1){
				break;
			}
		}
	}
	
	/**
	 * Durchgehende loop die Linie und Kurve unterscheidet
	 * @param Farbwerte
	 * @return 
	 */
	private static void loop(float values[]) {
		
		if(values[1] == farbe && search){
			if(values[0] > 22){
				kurve = 1;
			}
			else if(values[0] < -22){
				kurve = 2;
			}
			search = false;
			motoren.stop();
		}

		if(((values[2] <= 0.12f && values[2] != 0.0f) || tonne ) && kurve == -1){
				
			if (!tonne){
				motoren.stop();
				tonne = true;
				oldDist = values[2];
				motoren.motor_links.setPower(25);
				motoren.motor_rechts.setPower(25);
				motoren.motor_links.forward();
				motoren.motor_rechts.forward();
			}
			else if((values[2] <= 0.03f && values[2] != 0.0f || oldDist + 0.03 < values[2]) && !iter){
				motoren.stop();
				iter = true;
			}
			else if (!iter) {
				oldDist = values[2];
			}
			else if (iter && (foundTonne || hindernis.findTonne(values))) {
				foundTonne = true;
					
				if (!ranfahren) {
					motoren.hebeSensoren();
					tonnenFarbe = motoren.ranfahren();
					color.addColor(tonnenFarbe);
					ranfahren = true;
				}
				else if (!ausrichtung && hindernis.circumvent(values[1])){
					motoren.ausrichten();
					ausrichtung = true;
				}
				else if (ausrichtung && values[0] <= 0) {
					motoren.stop();
					tonne = false;
					ausrichtung = false;
					search = false;
					kurve = -1;
					ranfahren = false;
					foundTonne = false;
					iter = false;
						
				}
			}
		}
		else if(kurve == 1){
			motoren.turnLeft();
		}
		else if(kurve == 2){
			motoren.turnRight();
		}
		else if(values[1] == farbe && !search && kurve == -1){
			motoren.forward();
			motoren.diviation();
		}
		else if(values[1] != farbe){
			motoren.findLine();
			search = true;
		}
		
	}
	
	/**
	 * Setzt i wenn eine Kurve erkannt wurde
	 * @param i
	 * @return 
	 */
	public static void setKurve(int i) {
		kurve = i;
	}
	
	/**
	 * Ausgleichen nach der Kurve damit er wieder auf der Linie steht 
	 * mit Hilfe der Räder
	 * @param  Gyrosensorwere
	 * @return 
	 */
	public static void ausgleich(float[] values) {
		while ((values[0] >= 2 || values[0] <= -2) && values[3] == 0) {
			if (values[0] >= 2) {
				motoren.motor_links.setPower(20);
				motoren.motor_rechts.setPower(20);
				motoren.motor_links.forward();
				motoren.motor_rechts.backward();
			}
			else if (values[0] <= -2) {
				motoren.motor_links.setPower(20);
				motoren.motor_rechts.setPower(20);
				motoren.motor_links.backward();
				motoren.motor_rechts.forward();
			}
			values = sensoren.getValues();
		}
	}
}

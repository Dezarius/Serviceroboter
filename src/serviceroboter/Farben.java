package serviceroboter;

import java.util.Arrays;

public class Farben {
	private float farben [];
	
	public Farben(){
		farben = new float [1];
	}
	
	public void addColor(float color){
		float farben_new [] = Arrays.copyOf(this.farben, this.farben.length + 1 );
		farben_new [this.farben.length] = color;
		this.farben = farben_new;
	}

	
	public void print(){
		System.out.println("Folgende Farben wurden erkannt " + "\n");
		for(float flt : this.farben){
			System.out.println("Farbecode:" + flt);
		}
	}
	public float [] getColors (){
		return this.farben;
	}

}

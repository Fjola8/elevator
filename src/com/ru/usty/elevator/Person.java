package com.ru.usty.elevator;

public class Person implements Runnable{
	
	int startFloor, destinationFloor;
	//með þessu veit þáðurinn á hvaða hæð hann kom inn og hvar hann vill út
	public Person(int startFloor, int destinationFloor) {
		this.startFloor = startFloor;
		this.destinationFloor = destinationFloor;
	} 
	
	@Override
	public void run() {
		//Tjékka á semaphore-unni:
		try {
			ElevatorScene.semaphore1.acquire(); //wait fallið okkar
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//ef þráðurinn er kominn hingað þá er hann laus, s.s úr röðinni
		// --> þá þarf hann að lækka peopleCounterinn á þeirri hæð sem hann kom inn
		ElevatorScene.scene.decrementNrOfPeopleWaitingAtFloor(startFloor);
		
		
		System.out.println("Person thread released"); //bara til að tjékka hvenær hann losnar
	}
}

/*Manneskja er þráður sem þarf að bíða á semaphoru sem hleypir henni inní lyftu
þegar hún kemst inní lyftinu þá bíður hún á semaphoru sem hleypur henni út á þerri 
hæð sem hún er komin á - sú lyfta þarf að vera tengd við semaphoru þannig að 
þegar hún kemst á rétta hæð þá losar hún semphoruna (jafnmarga og þarf)
*/

/* Þegar persóna kemur inní röðina, er einhver semaphora sem hún á að wait á?
og á hún að signala eitthvað þegar hún er komin inn í lyftuna..
*/

/* Þegar lyfta yfirgefur hæð þarf hún að waita eða signala? 
 * Þarf hún að passa að núlla? */

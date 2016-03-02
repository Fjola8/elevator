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
		
		try {
			ElevatorScene.elevatorWaitMutex.acquire();
				ElevatorScene.inSemaphore.acquire();
			ElevatorScene.elevatorWaitMutex.release();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		ElevatorScene.scene.decrementNrOfPeopleWaitingAtFloor(startFloor);
		ElevatorScene.scene.incrementNrOfPeopleInElevator();
		System.out.println("Person thread released"); 
		int coun = ElevatorScene.scene.getNumberOfPeopleInElevator(0);
		System.out.println("numberOfPeopleInEle" + coun);
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

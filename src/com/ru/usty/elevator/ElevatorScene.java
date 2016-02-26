package com.ru.usty.elevator;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * The base function definitions of this class must stay the same
 * for the test suite and graphics to use.
 * You can add functions and/or change the functionality
 * of the operations at will.
 *
 */

public class ElevatorScene {

	//static = bara til ein útgáfa af þessu
	public static Semaphore queueSemaphore;
	
	public static Semaphore personCountMutex;
	
	/*til að vernda semaphorurnar sem hleypa okkur inn i lyfturnar, 
	  kannski að nota sama á semaphorurnar sem hleypa okkur út úr lyftunum*/
	public static Semaphore elevatorWaitMutex; 
	
	public static Semaphore elevatorPersonCountMutex;
	
	public static ElevatorScene scene;
	
	//TO SPEED THINGS UP WHEN TESTING,
	//feel free to change this.  It will be changed during grading
	public static final int VISUALIZATION_WAIT_TIME = 500;  //milliseconds

	private int nrOfPeopleInElevator;
	private int numberOfFloors;
	private int numberOfElevators;

	ArrayList<Integer> personCount; //use if you want but
									//throw away and
									//implement differently
									//if it suits you

	//Base function: definition must not change
	//Necessary to add your code in this one
	public void restartScene(int numberOfFloors, int numberOfElevators) {
		
		scene = this;
		
		queueSemaphore = new Semaphore(0);  //læst
		
		personCountMutex = new Semaphore(1); /*fyrsta person sem kesmt inní þessa semaphoru kemst í gegn og mun 
												setja semaphoruna = 0 og þá kemst enginn annar inní hana á meðan,
												svo þegar hann fer út verður semaphoran aftur = 1 og næsti kemst að */	
		elevatorWaitMutex = new Semaphore(1);
		
		
		
	
		Elevator elevator = new Elevator();
		Thread thread = new Thread(elevator);
		thread.start();
		
		
		this.numberOfFloors = numberOfFloors;
		this.numberOfElevators = numberOfElevators;

		personCount = new ArrayList<Integer>();
		for(int i = 0; i < numberOfFloors; i++) {
			this.personCount.add(0);
		}
	}

	//Base function: definition must not change
	//Necessary to add your code in this one
	public Thread addPerson(int sourceFloor, int destinationFloor) {
		
		//búa til tilvik af person klasanum, seinna setjum við inn source og dest
		Person person = new Person(sourceFloor, destinationFloor);
		
		//thread tekur inn runnable -> person er runnable og fer því þar inn
		Thread thread = new Thread(person);
		thread.start();
		
		incrementNrOfPeopleWaitingAtFloor(sourceFloor);
		
		return thread; //this means that the testSuite will not wait for the threads to finish
	}

	//Base function: definition must not change, but add your code
	public int getCurrentFloorForElevator(int elevator) {

		//dumb code, replace it!
		return 1;
	}

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleInElevator(int elevator) {
		//mutual exclusion
		
		return nrOfPeopleInElevator;
		
		
	/*	switch(elevator) {
		case 1: return 1;
		case 2: return 4;
		default: return 3;
		} */
	}
	
	public int incrementNrOfPeopleInElevator(int elevator) {
		
		return nrOfPeopleInElevator + 1;
	}

	public int decrementNrOfPeopleInElevator(int elevator) {
		
		return nrOfPeopleInElevator - 1;
	}
	
	
	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleWaitingAtFloor(int floor) {

		return personCount.get(floor);
	}

	//Við útfærðum þetta - lækkum personCount um 1:
	public void decrementNrOfPeopleWaitingAtFloor(int floor) {
		try {
			personCountMutex.acquire();
				personCount.set(floor, (personCount.get(floor) - 1));
			personCountMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void incrementNrOfPeopleWaitingAtFloor(int floor) {
		try {
			personCountMutex.acquire();
				personCount.set(floor, (personCount.get(floor) + 1));
			personCountMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Base function: definition must not change, but add your code if needed
	public int getNumberOfFloors() {
		return numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfFloors(int numberOfFloors) {
		this.numberOfFloors = numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public int getNumberOfElevators() {
		return numberOfElevators;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfElevators(int numberOfElevators) {
		this.numberOfElevators = numberOfElevators;
	}

	//Base function: no need to change unless you choose
	//				 not to "open the doors" sometimes
	//				 even though there are people there
	public boolean isElevatorOpen(int elevator) {

		return isButtonPushedAtFloor(getCurrentFloorForElevator(elevator));
	}
	//Base function: no need to change, just for visualization
	//Feel free to use it though, if it helps
	public boolean isButtonPushedAtFloor(int floor) {

		return (getNumberOfPeopleWaitingAtFloor(floor) > 0);
	}

}

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
	
	ArrayList<Integer> exitedCount = null;
	public static Semaphore exitedCountMutex;

	public static Semaphore inSemaphore;
	public static Semaphore outSemaphore;
	
	public static Semaphore personCountMutex;
	public static Semaphore elevatorWaitMutex; 
	public static Semaphore elevatorPersonCountMutex;
	
	public static ElevatorScene scene;
	
	//TO SPEED THINGS UP WHEN TESTING,
	//feel free to change this.  It will be changed during grading
	public static final int VISUALIZATION_WAIT_TIME = 3000;  //milliseconds

	private int nrOfPeopleInElevator;
	private int numberOfFloors;
	private int numberOfElevators;
	private int currentFloor = 0;
	ArrayList<Integer> personCount; //use if you want but
									//throw away and
									//implement differently
									//if it suits you

	//Base function: definition must not change
	//Necessary to add your code in this one
	public void restartScene(int numberOfFloors, int numberOfElevators) {
		
		scene = this;
		inSemaphore = new Semaphore(0);
		outSemaphore = new Semaphore(0);
		personCountMutex = new Semaphore(1); 	
		elevatorWaitMutex = new Semaphore(1);
		
		
		Elevator elevator = new Elevator(currentFloor);
		Thread thread = new Thread(elevator);
		thread.start();
		System.out.println("elevator added");
			
		this.numberOfFloors = numberOfFloors;
		this.numberOfElevators = numberOfElevators;

		personCount = new ArrayList<Integer>();
		for(int i = 0; i < numberOfFloors; i++) {
			this.personCount.add(0);
		}
		
		if(exitedCount == null) {
			exitedCount = new ArrayList<Integer>();
		}
		else {
			exitedCount.clear();
		}
		for(int i = 0; i < getNumberOfFloors(); i++) {
			this.exitedCount.add(0);
		}
		exitedCountMutex = new Semaphore(1);
		
	}

	//Base function: definition must not change
	//Necessary to add your code in this one
	public Thread addPerson(int sourceFloor, int destinationFloor) {
		
		Person person = new Person(sourceFloor, destinationFloor);
		Thread thread = new Thread(person);
		thread.start();
		
		incrementNrOfPeopleWaitingAtFloor(sourceFloor);
		System.out.println("person added");
		return thread; //this means that the testSuite will not wait for the threads to finish
	}

	//Base function: definition must not change, but add your code
	public int getCurrentFloorForElevator(int elevator) {
		//dumb code, replace it!
		return currentFloor;
	}

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleInElevator(int elevator) {

		return nrOfPeopleInElevator;
	}
	
	public void incrementNrOfPeopleInElevator() {
		System.out.println("hækka  people í lyftu!");
	//	nrOfPeopleInElevator++;
		try{
			elevatorPersonCountMutex.acquire();
			nrOfPeopleInElevator ++;
			elevatorPersonCountMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void decrementNrOfPeopleInElevator() {
		System.out.println("Lækka  people í lyftu!");
		nrOfPeopleInElevator--;
		/*try{
			elevatorPersonCountMutex.acquire();
			nrOfPeopleInElevator --;
			elevatorPersonCountMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	*/
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
	
	public void changeFloor(){
		
		if(currentFloor == 0){
			currentFloor++;
		}
		else{
			currentFloor--;
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
	
	public void personExitsAtFloor(int floor) {
		try {
			
			exitedCountMutex.acquire();
			exitedCount.set(floor, (exitedCount.get(floor) + 1));
			exitedCountMutex.release();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getExitedCountAtFloor(int floor) {
		if(floor < getNumberOfFloors()) {
			return exitedCount.get(floor);
		}
		else {
			return 0;
		}
	}


}

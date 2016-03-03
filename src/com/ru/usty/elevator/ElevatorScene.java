package com.ru.usty.elevator;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ElevatorScene {
	
	public static ElevatorScene scene;
	
	public static ArrayList<Semaphore> inSemaphores;
	public static ArrayList<Semaphore> outSemaphores;
	
	public static Semaphore exitedCountMutex;
	public static Semaphore personCountMutex;
	public static Semaphore elevatorPersonCountMutex;
	public static Semaphore moveMutex;
	public static Semaphore elevatorControllerMutex;
	
	public static final int VISUALIZATION_WAIT_TIME = 100;

//	private int nrOfPeopleInElevator;
	private int numberOfFloors;
	private int numberOfElevators;
	private int currentFloor = 0;
	private boolean up = true;
	
	ArrayList<Integer> personCount; 
	ArrayList<Integer> personElevatorCount;
	ArrayList<Integer> floorsInElevator;
	ArrayList<Integer> exitedCount = null;

	public void restartScene(int numberOfFloors, int numberOfElevators) {
		
		scene = this;
		
		personCountMutex = new Semaphore(1); 	
		elevatorPersonCountMutex = new Semaphore(1);
		moveMutex = new Semaphore(1);
		elevatorControllerMutex = new Semaphore(1);

		inSemaphores = new ArrayList<Semaphore>();
		outSemaphores = new ArrayList<Semaphore>();

		this.numberOfFloors = numberOfFloors;
		this.numberOfElevators = numberOfElevators;
		
		for (int i = 0; i < numberOfFloors; i++){
			inSemaphores.add(new Semaphore(0));
			outSemaphores.add(new Semaphore(0));
		}

		personCount = new ArrayList<Integer>();
		for(int i = 0; i < numberOfFloors; i++) {
			this.personCount.add(0);
		}
		
		floorsInElevator = new ArrayList<Integer>();
		personElevatorCount = new ArrayList<Integer>();
		for(int i = 0; i < numberOfElevators; i++) {
			this.floorsInElevator.add(0);
			this.personElevatorCount.add(0);
		}
		
		for(int i = 0; i < numberOfElevators; i++){
			Elevator elevator = new Elevator(i);
			Thread thread = new Thread(elevator);
			thread.start();
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

	public Thread addPerson(int sourceFloor, int destinationFloor) {
		
		Person person = new Person(sourceFloor, destinationFloor);
		Thread thread = new Thread(person);
		thread.start();
		
		incrementNrOfPeopleWaitingAtFloor(sourceFloor);
		return thread;
	}

	public int getCurrentFloorForElevator(int elevator) {
		return floorsInElevator.get(elevator);
	}

	public int getNumberOfPeopleInElevator(int elevator) {
		return personElevatorCount.get(elevator);
	}
	
	public void incrementNrOfPeopleInElevator(int elevator) {
		try{
			elevatorPersonCountMutex.acquire();
				//nrOfPeopleInElevator ++;
				personElevatorCount.set(elevator, (personElevatorCount.get(elevator) + 1));
			elevatorPersonCountMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void decrementNrOfPeopleInElevator(int elevator) {
		try{
			elevatorPersonCountMutex.acquire();
			//	nrOfPeopleInElevator --;
				personElevatorCount.set(elevator, (personElevatorCount.get(elevator) - 1));
			elevatorPersonCountMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public int getNumberOfPeopleWaitingAtFloor(int floor) {
		return personCount.get(floor);
	}

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
	
	public void changeFloor (int elevator) {
		if(this.floorsInElevator.get(elevator) == numberOfFloors - 1){
			up = false;
		}
		else if (this.floorsInElevator.get(elevator) == 0) {
			up = true;
		}
		
		if(up){
			try {
				moveMutex.acquire();
				int curr = this.floorsInElevator.get(elevator);
				floorsInElevator.set(elevator, curr+1);
					//currentFloor++;
				moveMutex.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			try {
				moveMutex.acquire();
				int curr = this.floorsInElevator.get(elevator);
				floorsInElevator.set(elevator, curr-1);
				//	currentFloor--;
				moveMutex.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getNumberOfFloors() {
		return numberOfFloors;
	}

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

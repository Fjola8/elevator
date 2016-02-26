package com.ru.usty.elevator;

public class Elevator implements Runnable{
	
	private static int MAX_PEOPLE;

	@Override
	public void run() {
		for(int i = 0; i < MAX_PEOPLE; i++) {
			ElevatorScene.queueSemaphore.release(); //signal
		}
	}
	
/*	public int getNrOfPeopleInElevator(){
		return nrOfPeople;
	}
	
	public void setNrOfPeopleInElevator(int currNrOfPeople){
		nrOfPeople = currNrOfPeople;
	} */
}

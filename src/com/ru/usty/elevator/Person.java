package com.ru.usty.elevator;

public class Person implements Runnable{
	
	private int startFloor, destinationFloor;
	findElevator elevController = new findElevator();

	public Person(int startFloor, int destinationFloor) {
		this.startFloor = startFloor;
		this.destinationFloor = destinationFloor;
	} 
	
	@Override
	public void run() {
		int numberOfElevator = elevController.getElevator();
		
		try {
			ElevatorScene.inSemaphores.get(startFloor).acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ElevatorScene.scene.decrementNrOfPeopleWaitingAtFloor(startFloor);
		ElevatorScene.scene.incrementNrOfPeopleInElevator(numberOfElevator);
	
		try {
			ElevatorScene.outSemaphores.get(destinationFloor).acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ElevatorScene.scene.decrementNrOfPeopleInElevator(numberOfElevator);
		
		ElevatorScene.scene.personExitsAtFloor(destinationFloor);
	}
}


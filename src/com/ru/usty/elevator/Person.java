package com.ru.usty.elevator;

public class Person implements Runnable{
	
	private int startFloor, destinationFloor;

	public Person(int startFloor, int destinationFloor) {
		this.startFloor = startFloor;
		this.destinationFloor = destinationFloor;
	} 
	
	@Override
	public void run() {
		
		try {
			ElevatorScene.inSemaphores.get(startFloor).acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ElevatorScene.scene.decrementNrOfPeopleWaitingAtFloor(startFloor);
		ElevatorScene.scene.incrementNrOfPeopleInElevator();
	
		try {
			ElevatorScene.outSemaphores.get(destinationFloor).acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ElevatorScene.scene.decrementNrOfPeopleInElevator();
		
		ElevatorScene.scene.personExitsAtFloor(destinationFloor);
	}
}


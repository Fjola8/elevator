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
			ElevatorScene.inSemaphores.get(startFloor).acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		ElevatorScene.scene.decrementNrOfPeopleWaitingAtFloor(startFloor);
		ElevatorScene.scene.incrementNrOfPeopleInElevator();
		
		System.out.println("Person thread released"); 
		int coun = ElevatorScene.scene.getNumberOfPeopleInElevator(0);
		System.out.println("numberOfPeopleInEle" + coun);
	
		try {
			ElevatorScene.outSemaphores.get(destinationFloor).acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		ElevatorScene.scene.decrementNrOfPeopleInElevator();
		ElevatorScene.scene.personExitsAtFloor(destinationFloor);
		System.out.println("Person thread released OUT");
		
	}
}


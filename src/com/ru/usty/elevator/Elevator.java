package com.ru.usty.elevator;

public class Elevator implements Runnable{
	
	private static int MAX_PEOPLE = 6;
	private int elevatorNr;
	
	Elevator(int elevator){
		this.elevatorNr = elevator;
	}
	
	@Override
	public void run() 
	{
		while(true){
			try{				
				int extraSpace = MAX_PEOPLE - ElevatorScene.scene.getNumberOfPeopleInElevator(this.elevatorNr);
				int currentFloor = ElevatorScene.scene.getCurrentFloorForElevator(this.elevatorNr);
				
				System.out.println("current floor: " + currentFloor);

				for(int i = 0; i < extraSpace; i++) {
					ElevatorScene.inSemaphores.get(currentFloor).release();
				}
		
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
				extraSpace = MAX_PEOPLE - ElevatorScene.scene.getNumberOfPeopleInElevator(this.elevatorNr);

				for(int i = 0; i < extraSpace; i++) {
					ElevatorScene.inSemaphores.get(currentFloor).acquire();
				}
				
				ElevatorScene.scene.changeFloor(this.elevatorNr);
				
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
				currentFloor =  ElevatorScene.scene.getCurrentFloorForElevator(this.elevatorNr);
				extraSpace = ElevatorScene.scene.getNumberOfPeopleInElevator(this.elevatorNr);
				
				for(int i = 0; i < extraSpace; i++) {
					ElevatorScene.outSemaphores.get(currentFloor).release(); 
				}
				
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
				extraSpace = ElevatorScene.scene.getNumberOfPeopleInElevator(this.elevatorNr);
                for (int i = 0; i < extraSpace; i++){
                    ElevatorScene.outSemaphores.get(currentFloor).acquire();
                }
				
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
				
			}catch(InterruptedException e){
	                e.printStackTrace();
			}
		}
	}
	
}

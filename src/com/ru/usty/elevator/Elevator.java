package com.ru.usty.elevator;

public class Elevator implements Runnable{
	
	private static int MAX_PEOPLE = 6;
	
	@Override
	public void run() 
	{
		while(true){
			try{
				int extraSpace = MAX_PEOPLE - ElevatorScene.scene.getNumberOfPeopleInElevator(0);
				int currentFloor = ElevatorScene.scene.getCurrentFloorForElevator(0);

				for(int i = 0; i < extraSpace; i++) {
					ElevatorScene.inSemaphores.get(currentFloor).release();
				}
		
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
				extraSpace = MAX_PEOPLE - ElevatorScene.scene.getNumberOfPeopleInElevator(0);

				for(int i = 0; i < extraSpace; i++) {
					ElevatorScene.inSemaphores.get(currentFloor).acquire();
				}
				
				ElevatorScene.scene.changeFloor();
				
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
				currentFloor =  ElevatorScene.scene.getCurrentFloorForElevator(0);
				extraSpace = ElevatorScene.scene.getNumberOfPeopleInElevator(0);
				
				for(int i = 0; i < extraSpace; i++) {
					ElevatorScene.outSemaphores.get(currentFloor).release(); 
				}
				
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
				extraSpace = ElevatorScene.scene.getNumberOfPeopleInElevator(0);
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

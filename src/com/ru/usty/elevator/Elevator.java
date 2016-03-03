package com.ru.usty.elevator;

public class Elevator implements Runnable{
	
	private static int MAX_PEOPLE = 6;
	int currFloor;
	
	public Elevator(int currfloor){
		currfloor = this.currFloor;
	}
	//critical session
	//engin persóna má gera acuire þegar þetta á sér stað
	
	@Override
	public void run() 
	{
		while(true){
			try{
				int availableSpace = MAX_PEOPLE - ElevatorScene.scene.getNumberOfPeopleInElevator(0);
				int currentFloor = ElevatorScene.scene.getCurrentFloorForElevator(0);

				for(int i = 0; i < availableSpace; i++) {
					System.out.println("hleypir fólki inn í laust pláss í lyftu");
					ElevatorScene.inSemaphores.get(currentFloor).release(); //Lyftan er opin
				}
		
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
				int peopleTravelling = MAX_PEOPLE - ElevatorScene.scene.getNumberOfPeopleInElevator(0);

				for(int i = 0; i < peopleTravelling; i++) {
					System.out.println("núllar semaphoru svo það sleppi ekki auka manneskja inn");
					ElevatorScene.inSemaphores.get(currentFloor).acquire();
				}
				
				System.out.println("Elevator floor before move " + ElevatorScene.scene.getCurrentFloorForElevator(0));
				ElevatorScene.scene.changeFloor();
				System.out.println("Elevator floor after move " + ElevatorScene.scene.getCurrentFloorForElevator(0));
				
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
				currentFloor =  ElevatorScene.scene.getCurrentFloorForElevator(0);
				int peopleToRelease = ElevatorScene.scene.getNumberOfPeopleInElevator(0);
				
				for(int i = 0; i < peopleToRelease; i++) {
					System.out.println("heypir fólki út úr lyftu");
					ElevatorScene.outSemaphores.get(currentFloor).release(); 
				}
				
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
				
				ElevatorScene.scene.changeFloor();
				System.out.println("fólk í lyftu þegar aftur niður" + ElevatorScene.scene.getNumberOfPeopleInElevator(0));
				
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
			}catch(InterruptedException e){
	                e.printStackTrace();
			}
		}
	}
	
}

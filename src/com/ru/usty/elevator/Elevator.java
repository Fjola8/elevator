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
		int peopleInElevator = MAX_PEOPLE - ElevatorScene.scene.getNumberOfPeopleInElevator(0);
		for(int i = 0; i < peopleInElevator; i++) {
			System.out.println("hleypir fólki inn í laust pláss í lyftu");
			ElevatorScene.inSemaphore.release(); //Lyftan er opin
		}
		
		Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
		// ATH number of people in elevator er ekki að núllast hér!!
		//ef lyftan fyllist ekki þá þarf að stylla queuesemaphore á núll svo hun hleypi ekki afram inn
		int peopleTravelling = MAX_PEOPLE - ElevatorScene.scene.getNumberOfPeopleInElevator(0);
		System.out.println("Hérna á talan að vera null: " + peopleTravelling);

		for(int i = 0; i < peopleTravelling; i++){
			System.out.println("núllar semaphoru svo það sleppi ekki auka manneskja inn");
			ElevatorScene.inSemaphore.release();
		}
			}catch(InterruptedException e){
	                e.printStackTrace();
			}
		}
		
	// lyftan er á ferð	ElevatorScene.inSemaphore.acquire();
		
	}
	
/*	public int getNrOfPeopleInElevator(){
		return nrOfPeople;
	}
	
	public void setNrOfPeopleInElevator(int currNrOfPeople){
		nrOfPeople = currNrOfPeople;
	} */
}

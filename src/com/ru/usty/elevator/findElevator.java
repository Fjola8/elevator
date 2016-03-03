package com.ru.usty.elevator;

public class findElevator {
	public int returnElevator(){
		for(int i = 0; i < ElevatorScene.scene.getNumberOfElevators(); i++){
			System.out.println("inní hvaða lyftu er ég: " + i);
			int people = ElevatorScene.scene.getNumberOfPeopleInElevator(i);
			System.out.println("nr of people in elevator: " + people);
			if(ElevatorScene.scene.getNumberOfPeopleInElevator(i) < 6){
				System.out.println("fjöldi elevator: " + ElevatorScene.scene.getNumberOfElevators());
				return i;
			}
		}
		return 0;
	}
}
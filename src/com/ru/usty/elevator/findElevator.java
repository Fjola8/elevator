package com.ru.usty.elevator;

public class findElevator {
	public int getElevator(){
		for(int i = 0; i < ElevatorScene.scene.getNumberOfElevators(); i++) {
			if(ElevatorScene.scene.getNumberOfPeopleInElevator(i) < 6) {
				return i;
			}	
		}
		return 0;
	}
}

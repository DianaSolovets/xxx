package com.company;

public class SpaceUsageTooMuchException extends Exception {

	@Override
	public String getMessage (){
		return "Вы пытаетесь переполниить комнату мебелью. Заполненность не должна превышать 70% от всей площади комнаты.";
	}
}

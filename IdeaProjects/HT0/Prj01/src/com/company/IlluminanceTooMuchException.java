package com.company;

public class IlluminanceTooMuchException extends Exception {

	@Override
	public String getMessage (){
		return "Вы пытаетесь создать слишком освещенную комнату. Освещенность не должная превышать 4000 лк.";
	}
}

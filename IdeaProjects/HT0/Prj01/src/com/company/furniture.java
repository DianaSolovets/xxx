package com.company;

public class furniture {

	String name;
	double spaceMin;
	double spaceMax;

	public furniture(){};

	public furniture(String name, String areaMax){
		this.name = name;
		try {
			String a = areaMax.replaceAll("\\D", "");
			spaceMax = Double.parseDouble(areaMax);
			spaceMin = spaceMax;
		}
		catch (NumberFormatException e) {
			System.out.println("Введите, пожалуйста, корректную площадь.");
		}
	}

	public furniture(String name, int areaMax) {
		this.name = name;
		this.spaceMax = areaMax;
		spaceMin = spaceMax;
	}

	public furniture (String name, String areaMin, String areaMax){
		this.name = name;
		try {
			String a = areaMin.replaceAll("\\D", "");
			String b = areaMax.replaceAll("\\D", "");
			spaceMin = Double.parseDouble(a);
			spaceMax = Double.parseDouble(b);
		}
		catch (NumberFormatException e) {
			System.out.println("Введите, пожалуйста, корректную площадь.");
		}
	}

	public furniture (String name, int areaMin, int areaMax){
		this.name = name;
		this.spaceMin = areaMin;
		this.spaceMax = areaMax;
	}

}

package com.company;

import java.util.ArrayList;

public class room {

	String name;
	int space;
	int windows;
	int illumination;
	double fullness;
	ArrayList<bulb> roomBulbs = new ArrayList();
	ArrayList<furniture> furnitures = new ArrayList();

	public room(String name, String space, String windows) {
		try{
			String a = space.replaceAll("\\D", "");
			this.space = Integer.parseInt(a);

			String b = windows.replaceAll("\\D", "");
			this.windows = Integer.parseInt(b);

			illumination = (this.windows * 700);
			checkIllumination();

			this.name = name;
		} catch (IlluminanceTooMuchException e) {
			e.getMessage();
		} catch (NumberFormatException e) {
			if (space == null)
				System.out.println("Введите, пожалуйста, корректную площадь помещения.");
			else if (windows == null)
				System.out.println("Введите, пожалуйста, корректное количество окон.");
		}
	}

	public room(String name, int space, int windows) {
		this.name = name;
		this.space = space;
		this.windows = windows;
		illumination = (this.windows * 700);
	}

	public void add(bulb b) {
		try {
			checkIllumination(b);
			roomBulbs.add(b);
			illumination += b.lk;
		} catch (IlluminanceTooMuchException e) {
			e.printStackTrace();
		}
	}

	public void add(furniture f) throws SpaceUsageTooMuchException {
		try {
			checkFullness(f);
			furnitures.add(f);
			fullness += (f.spaceMax / this.space) * 100;
		} catch (SpaceUsageTooMuchException e) {
			e.printStackTrace();
		}
	}

	public void checkIllumination() throws IlluminanceTooMuchException {
		if (illumination > 4000) {
			throw new IlluminanceTooMuchException();
		} else if (illumination < 300) {
			System.out.println("Вы создали недостаточно освещенную комнату. Добавьте еще лампочек.");
		}
	}

	public void checkIllumination(bulb b) throws IlluminanceTooMuchException {
		int curIll = illumination + b.lk;

		if (curIll > 4000) {
			throw new IlluminanceTooMuchException();
		} else if (curIll < 300) {
			System.out.println("Вы создали недостаточно освещенную комнату. Добавьте еще лампочек.");
		}
	}

	public void checkFullness(furniture f) throws SpaceUsageTooMuchException {
		if (fullness + ((f.spaceMax / this.space) * 100) > 70) throw new SpaceUsageTooMuchException();
	}

	public void roomDescribe() {
		System.out.println(name);
		System.out.println("Освещенность = " + illumination + ". " + printWindows() + printBulbs());
		System.out.println("Площадь = " + space + "м^2 (занято " + (space * (fullness / 100)) + " м^2, гарантированно свободно " + (space - (space * (fullness / 100))) + " м^2 или " + (100 - fullness) + " % площади).");
		System.out.println("Мебель: \n" + printFurnitures());
		System.out.println();

	}
	public String printWindows(){
		String a = "";
		if (windows != 0) {
			a = "Окон: " + windows + " по 700 лк.";
		}
		return a;
	}

	public String printBulbs() {
		String a = "";
		if (!roomBulbs.isEmpty()) {
			a = " Лампочки по: ";
			for (bulb bulb : roomBulbs) {
				a += bulb.lk + "лк, ";
			}
			a.substring(0, a.length() - 2);
			a += ".";
		}
		return a;
	}

	public String printFurnitures() {
		String a = "";
		if (!furnitures.isEmpty()) {
			for (furniture f : furnitures) {
				a += (f.name + ", площадь " + printFurnitureSpace(f)+ " кв.м.\n");
			}
		}
		else {
			a = "мебели нет.";
			}
		return a;
	}

	public String printFurnitureSpace(furniture f){
		String a = "";
		if (f.spaceMax == f.spaceMin) {
			a += f.spaceMax;
		}
		else {
			a = " от " + f.spaceMin + " до " + f.spaceMax ;
		}
		return a;
	}
}
package com.company;

public class Main {

    public static void main(String[] args) throws SpaceUsageTooMuchException {
        building building1 = new building("Здание 1");
        building1.addRoom("Комната 1", "20м", "2о");
        building1.addRoom("Комната 2", 5, 0);
        building1.getRoom("Комната 1").add(new bulb(150));
        building1.getRoom("Комната 1").add(new bulb(250));
        building1.getRoom("Комната 1").add(new bulb(4250));
        building1.getRoom("Комната 1").add(new furniture("Стол письменный", 3));
        building1.getRoom("Комната 1").add(new furniture("Стол письменный", 33));
        building1.getRoom("Комната 1").add(new furniture("Кресло мягкое и пушистое", "от1", "до2"));
        building1.getRoom("Комната 2").add(new bulb(150));
        building1.describe();

    }
}

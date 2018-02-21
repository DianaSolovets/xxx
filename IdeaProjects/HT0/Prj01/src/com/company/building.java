package com.company;

import java.util.ArrayList;

public class building {

	String name;
	ArrayList <room> rooms;

	public building (String name){
		this.name = name;
		rooms = new ArrayList<>();
	}

	public void addRoom(String name, int space, int windows){
		rooms.add(new room(name, space, windows));
	}

	public void addRoom(String s, String i, String i1){
		rooms.add(new room(s, i, i1));
	}

	public room getRoom (String roomName){
		for (room room:rooms) {
			if(room.name.toLowerCase().equals(roomName.toLowerCase()))
				return room;
		}
		return null;
	}
	public void describe (){
		System.out.println(name);
		for (room room:rooms) {
			room.roomDescribe();
		}
	}
}

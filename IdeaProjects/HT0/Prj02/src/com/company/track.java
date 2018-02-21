package com.company;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;
import org.farng.mp3.id3.ID3v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;


public class track {

	String performer;
	String album;
	String name;
	String duration;
	String path;
	String MD5;

	public track (File f){
		MP3File mp3file;
		try {
			mp3file = new MP3File(f);

			if (!mp3file.hasID3v2Tag()) {
				performer = "Неизвестный исполнитель";
				album = "Неизвестный альбом";
				name = "Неизвестной название";
				duration = new SimpleDateFormat("mm:ss").format(new Date(f.length() / 17));
				path = f.getPath();
				calcCheckSum();
			} else {
				AbstractID3v2 tag = mp3file.getID3v2Tag();
				album = tag.getAlbumTitle();
				name = tag.getSongTitle();
				performer = tag.getLeadArtist();
				duration = new SimpleDateFormat("mm:ss").format(new Date(f.length() / 17));
				path = f.getPath();
				calcCheckSum();

				if(performer.isEmpty())
					performer = "Неизвестный исполнитель";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TagException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void calcCheckSum () throws Exception{

		MessageDigest md = MessageDigest.getInstance("MD5");
		FileInputStream fis = new FileInputStream(path);
		byte[] dataBytes = new byte[1024];

		int nread = 0;
		while ((nread = fis.read(dataBytes)) != -1) {
			md.update(dataBytes, 0, nread);
		}

		byte[] mdbytes = md.digest();

		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		MD5 = sb.toString();
	}

	public boolean equals (Object other){
		if (other == null){ return false; }
		if (other == this){ return true; }
		if (!(other instanceof track)){ return false; }
		if (this.performer.equals(((track) other).performer) &&
				this.album.equals(((track) other).album) &&
				this.name.equals(((track) other).name)) {
			return true; }
		return false;
	}



	/*public Comparator<track> COMPARE_BY_PERFORMER = new Comparator<track>() {
		@Override
		public int compare(track n1, track n2) {
			if (n1.performer() == n2.performer()) return 0;
			else if (o1.getCount() > o2.getCount()) return 1;
			else return -1;
		}
	}
	*/


}

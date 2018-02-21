package com.company;

import java.io.*;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    static String extension = "mp3";
    static ArrayList<File> mp3List; //коллекция для хранения файлов с искомым расщирением
    static ArrayList<track> trackList;
    static Logger logger;


    public static void main(String[] args) {
		System.setProperty("log4j.configurationFile",
				"C:\\Users\\Gleb\\IdeaProjects\\HT0-2\\src\\com\\company\\log4j2.xml");
	    logger = LogManager.getRootLogger();

        TreeSet<String> paths = new TreeSet(); // создание коллекции путей к папкам для сканирования и
        // проверка на дублирующиеся пути в параметрах командной строки
        for (int i = 0; i < args.length; i++) {
            paths.add(args[i]);
        }

        for (String path : paths) {//к каждому из параметров командной строки, которые могут содержать путь к папкам, применяем метод parse
            File folder = new File(path);
            if (folder.exists()) {//проверка, существует ли указанный объект файловой системы
                if (folder.isFile()) {//проверка, является ли указанный объект папкой
                    System.out.println(folder.getName() + " - это путь к файлу. Введите, пожалуйста, путь к папке.");
                } else {
                    mp3List = new ArrayList<>();
                    parse(folder);
                }
            } else {
                System.out.println("Объекта файловой системы c именем " + folder.getName() + "не существует. Проверьте, пожалуйста, введенный путь.");
                continue;
            }
        }
        trackList = new ArrayList<>();
        for (File tr: mp3List){
            trackList.add(new track(tr));
        }

        Collections.sort(trackList, new Comparator<track>() {
            @Override
            public int compare(track o1, track o2) {
                String perf1 = o1.performer;
                String perf2 = o2.performer;
                int comp = perf1.compareTo(perf2);

                if(comp != 0) { return comp; }
                else {
                    String alb1 = o1.album;
                    String alb2 = o2.album;
                    return alb1.compareTo(alb2);
                }
            }
        });

        deliverHtml();
        LinkedHashMap<track, String> duplicates = checkDuplicates();

	    LinkedHashMap<track, String> similarSongs = checkSimilarSongs();
    }

    private static void deliverHtml() {
        try {
            File f = new File("source.htm");
            BufferedWriter bw = null;

            bw = new BufferedWriter(new FileWriter(f));

            bw.write("<html>");
            bw.write("<body>");
            bw.write("<textarea cols=200 rows=500>");

            track prevTrack = null;
            track[] tracks = trackList.toArray(new track[trackList.size()]);

	        for(int i = 0; i<trackList.size(); i++) {
	        	track curTrack = tracks[i];

	        	if(prevTrack == null || !curTrack.performer.equals(prevTrack.performer)) {
					bw.write(tracks[i].performer);
			        bw.newLine();
					bw.write("  " + curTrack.album);
			        bw.newLine();
					bw.write("    " + curTrack.name + " " + curTrack.duration + " (" + curTrack.path + ")");
			        bw.newLine();
		        } else {
			        if(!prevTrack.album.equals(curTrack.album)) {
				        bw.write("  " + curTrack.album);
				        bw.newLine();
				        bw.write("    " + curTrack.name + " " + curTrack.duration + "(" + curTrack.path + ")");
				        bw.newLine();
			        } else {
				        bw.write("    " + curTrack.name + " " + curTrack.duration + "(" + curTrack.path + ")");
				        bw.newLine();
			        }
		        }

		        prevTrack = curTrack;
	        }

            bw.write("</textarea>");
            bw.write("</body>");
            bw.write("</html>");

            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static LinkedHashMap<track, String> checkDuplicates() {
	    LinkedHashMap<track, String> map = new LinkedHashMap<>();
    	track[] tracks = trackList.toArray(new track[trackList.size()]);
    	for (int i = 0; i < tracks.length - 1; i++) {
    		for (int j = i + 1; j < tracks.length; j++) {
    			if (tracks[i].MD5.equals(tracks[j].MD5)) {
    				map.put(tracks[i], tracks[i].MD5);
    				map.put(tracks[j], tracks[j].MD5);
			    }
		    }
	    }

	    String md5 = "";
	    for(Map.Entry<track, String> track: map.entrySet()) {
    		if(md5.isEmpty() || !track.getValue().equals(md5)) {
    			logger.info("Duplicate-" + track.getValue());
    			md5 = track.getValue();
		    }
		    logger.trace(track.getKey().path);
	    }

	    return map;
    }

    private static LinkedHashMap<track, String> checkSimilarSongs() {
	    LinkedHashMap<track, String> map = new LinkedHashMap();
	    track[] tracks = trackList.toArray(new track[trackList.size()]);
	    for (int i = 0; i < tracks.length - 1; i++) {
		    for (int j = i + 1; j < tracks.length; j++) {
			    if (tracks[i].equals(tracks[j])) {
				    map.put(tracks[i], tracks[i].MD5);
				    map.put(tracks[j], tracks[j].MD5);
			    }
		    }
	    }

	    String md5 = "";
	    for(Map.Entry<track, String> track: map.entrySet()) {
		    if(md5.isEmpty() || !track.getValue().equals(md5)) {
			    logger.info(track.getKey().performer + ", " + track.getKey().album + ", " + track.getKey().name);
			    md5 = track.getValue();
		    }
		    logger.trace(track.getKey().path);
	    }

	    return map;
    }

    private static void parse(File f){
        File[] listOfFiles = f.listFiles(); //собираем в массив все файлы в указанной папке
        if (listOfFiles == null) return;

        ArrayList<File> directories = new ArrayList<>();
        for (File directoryItem : listOfFiles) {




            if (directoryItem.isDirectory()) {
                directories.add(directoryItem);//если в указанной папке есть вложенная папка, собираем их в отдельную колллекцию,
            }
            if (directoryItem.isFile()) { //если в папке находится файл, то проверяем его разрешение и, если оно соответствует искомому, добавляем в коллекцию
                int x = directoryItem.getName().lastIndexOf('.');
                String format = directoryItem.getName().substring(x + 1);
                if (format.toLowerCase().equals(extension.toLowerCase())) {
                    mp3List.add(directoryItem);
                }
            }
        }

        for (File files : directories) {//для коллекции вложенных папок рекурсивно применяем метод parse
            parse(files);
        }
    }
}

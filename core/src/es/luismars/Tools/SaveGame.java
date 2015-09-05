package es.luismars.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.*;
import java.util.*;

/**
 * Created by Dalek on 03/08/2015.
 */
public class SaveGame {

    private static String name;
    private static String path = "San Valero/savegames/";
    private static String extension = ".json";


    private static Map<String, Set<IntPair>> files = new TreeMap<String, Set<IntPair>>();
    private static Map<String, Integer> stats = new TreeMap<String, Integer>();
    static Json json = new Json();

    public static List<String> getSaveFiles() {

        FileHandle[] fileList = Gdx.files.external(path).list();
        List<String> nameList = new ArrayList<String>(fileList.length);
        for (FileHandle fh : fileList) {
            if (fh.isDirectory())
                nameList.add(fh.nameWithoutExtension());
        }

        return nameList;
    }

    public static void load() {
        load(name);
    }

    public static void load(String name) {
        System.out.println("loading " + name);

        try {
            loadItems("keys");
            loadItems("doors");
            loadItems("switches");
            loadItems("keySwitches");
            loadStats();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        System.out.println("saving " + name);
        saveItems("keys");
        saveItems("doors");
        saveItems("switches");
        saveItems("keySwitches");
        saveStats();
    }

    @SuppressWarnings("unchecked")
    private static void loadItems(String fileName) throws IOException, ClassNotFoundException {
        FileHandle save = Gdx.files.external(path + name + "/" + fileName + extension);
        json.addClassTag("Int pair", IntPair.class);

        if (save.exists()) {
            /*
            ObjectInputStream ois = new ObjectInputStream(save.read());
            Object o = ois.readObject();
            if (o != null && o instanceof TreeMap) {
                files.put(fileName, (TreeMap<Integer, List<Integer>>) o);
            }*/

            //json.setElementType(HashSet.class, fileName, IntPair.class);
            files.put(fileName, json.fromJson(HashSet.class, IntPair.class, save));
        } else {
            files.put(fileName, new HashSet<IntPair>());
        }
    }
    @SuppressWarnings("unchecked")
    private static void loadStats() throws IOException, ClassNotFoundException {
        String fileName = "stats";
        FileHandle save = Gdx.files.external(path + name + "/" + fileName + extension);
        //json.addClassTag("Int pair", IntPair.class);

        if (save.exists()) {
            //json.setElementType(HashSet.class, fileName, IntPair.class);
            stats = json.fromJson(TreeMap.class, Integer.class, save);
        } else {
            files.put(fileName, new HashSet<IntPair>());
        }
    }
    private static void saveItems(String fileName) {
        FileHandle save = Gdx.files.external(path + name + "/" + fileName + extension);

        try {
            save.writeBytes(serialize(getItems(fileName)), false);

//            json.setElementType(HashSet.class, fileName, IntPair.class);
            save.writeString(json.prettyPrint(getItems(fileName)), false);

            //json.toJson(getItems(fileName), TreeMap.class, HashSet.class, save);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveStats() {
        String fileName = "stats";
        FileHandle save = Gdx.files.external(path + name + "/" + fileName + extension);


        save.writeString(json.prettyPrint(stats), false);


    }

    public static Set<IntPair> getItems(String fileName) {
        if (files.containsKey(fileName))
            return files.get(fileName);
        return new HashSet<IntPair>();
    }


    public static void addItem(String fileName, int level, int item) {
        getItems(fileName).add(new IntPair(level, item));
    }

    public static void removeItem(String fileName, int level, Integer item) {
        getItems(fileName).remove(new IntPair(level, item));
    }

    public static void setStat(String fileName, int value) {
        stats.put(fileName, value);
    }

    public static int getStat(String filename, int def) {
        Integer integer = stats.get(filename);
        if (integer == null) {
            return def;
        }
        return integer;
    }

    public static boolean containsItem(String fileName, int level, int item) {
        return getItems(fileName).contains(new IntPair(level, item));
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
    }


    public static void setName(String name) {
        SaveGame.name = name;
    }
}

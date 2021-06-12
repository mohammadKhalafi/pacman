package sample.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import sample.models.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class DataBaseController {

    public static void createUser(String username, String password) throws Exception {
        User user = new User(username, password);
        if (hasThisUsernameUsed(username)) {
            throw new Exception("username has used");
        }
        initUser(user);
        createFile(getUserPathByUsername(username), user);
    }

    public static void initUser(User user) {
        user.getMaps().add(0);
        user.getMaps().add(1);
        user.getMaps().add(2);
    }

    public static boolean hasThisUsernameUsed(String username) {
        return new File(getUserPathByUsername(username)).exists();
    }

    public static String getUserPathByUsername(String username) {
        return getUsersPath() + "/" + username + ".json";
    }

    public static String getMapPathByIndex(int index) {
        return getMapsPath() + "/" + index + ".json";
    }


    public static <obj> void createFile(String path, obj Object) {

        try (Writer writer = new FileWriter(path)) {

            Gson gson = new GsonBuilder().create();

            gson.toJson(Object, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createFile(String path, String data) {

        try {
            File myObj = new File(path);
            myObj.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(path);
            fileWriter.write(data);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static User getUserByUsername(String username) {
        try {
            User user = (User) getObjectByPath(getUserPathByUsername(username), User.class);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public static Object getObjectByPath(String path, Class<?> cls) throws Exception {

        Reader reader = new FileReader(path);
        try {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(reader, cls);
        } finally {
            reader.close();
        }
    }

    public static String getDataOfFile(String path) {
        try {
            String data = "";
            data = new String(Files.readAllBytes(Paths.get(path)));
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String getUsersPath() {
        return "/Users";
    }

    public static void createUsersDirectory() {
        File f1 = new File(getUsersPath());
        f1.mkdir();
    }

    public static void createMapsDirectory() {
        File f1 = new File(getMapsPath());
        f1.mkdir();
    }

    public static String getMapsPath() {
        return "/Maps";
    }


    public static void deleteFile(String path) throws Exception {
        if (!new File(path).delete()) {
            throw new Exception("file deleting failed");
        }
    }

    public static void deleteUser(String username) throws Exception {
        deleteFile(getUserPathByUsername(username));
    }

    public static ArrayList<User> getAllUsers() {

        ArrayList<User> users = new ArrayList<>();
        File folder = new File(getUsersPath());
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                try {
                    Gson gson = new GsonBuilder().create();
                    Reader reader = new FileReader(file);
                    Scanner scanner = new Scanner(reader);
                    scanner.useDelimiter("\\Z");
                    User user = gson.fromJson(new Scanner(reader).nextLine(), User.class);
                    users.add(user);
                    reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return users;
    }

    public static int getNewMapIndex() {

        File folder = new File(getMapsPath());
        File[] listOfMaps = folder.listFiles();

        int counter = 0;

        for (File file : listOfMaps) {
            if (file.isFile()) {
                counter++;
            }
        }

        return counter;
    }

    public static void upDateUser(User user) {
        createFile(getUserPathByUsername(user.getUsername()), user);
    }

}

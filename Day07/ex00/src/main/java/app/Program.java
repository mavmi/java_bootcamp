package app;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Program {
    final private static Scanner scanner = new Scanner(System.in);;
    final static private String PACKAGE_NAME = "classes";
    final static private String SEPARATOR = "---------------------";

    static private Map<String, Class<?>> classes;

    public static void main(String[] args){
        classes = getListOfClasses();
        if (classes == null) exit("Couldn't create list of available classes", -1);

        printListOfClasses();
        Class<?> cls = printClassInfo();
        Object obj = createAnObject(cls);
        if (obj == null) exit("Couldn't init object", -1);
        changeValue(obj, cls);
        callMethod(obj, cls);
    }

    private static Map<String, Class<?>> getListOfClasses(){
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        URL packageUrl = classLoader.getResource(PACKAGE_NAME);
        if (packageUrl == null) return null;

        String packagePath = packageUrl.getPath();
        if (packagePath.length() == 0) return null;

        Map<String, Class<?>> classes = new HashMap<>();
        File[] files = new File(packagePath).listFiles();
        if (files == null) return null;
        for (File file : files){
            String fileName = file.getName();
            if (!fileName.endsWith(".class")) continue;
            try {
                String className = fileName.substring(0, fileName.length() - 6);
                classes.put(
                        className,
                        Class.forName(PACKAGE_NAME + "." + className)
                );
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

        return classes;
    }

    private static void printListOfClasses(){
        System.out.println("Classes:");
        for (String key : classes.keySet()){
            System.out.print("  - ");
            System.out.println(key);
        }
        System.out.println(SEPARATOR);
    }
    private static Class<?> printClassInfo(){
        System.out.print("Enter class name:\n");

        while (true){
            System.out.print("-> ");
            String className = scanner.nextLine();
            Class<?> cls = classes.get(className);
            if (cls == null){
                System.out.println("Error: invalid class name");
                continue;
            }

            System.out.println("fields:");
            for (Field field : cls.getDeclaredFields()){
                System.out.print("\t");
                System.out.print(field.getType().getSimpleName());
                System.out.print(" ");
                System.out.println(field.getName());
            }

            System.out.println("methods:");
            for (Method method : cls.getDeclaredMethods()){
                System.out.print("\t");
                System.out.print(method.getReturnType().getSimpleName());
                System.out.print(" ");
                System.out.print(method.getName());
                System.out.print("(");
                int paramsCount = method.getParameterCount();
                for (int i = 0; i < paramsCount; i++){
                    System.out.print(method.getParameterTypes()[i].getSimpleName());
                    if (i + 1 != paramsCount) System.out.print(", ");
                }
                System.out.print(")\n");
            }

            System.out.println(SEPARATOR);
            return cls;
        }
    }
    private static Object createAnObject(Class<?> cls){
        System.out.println("Let’s create an object.");

        Object obj;
        try {
            obj = cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }

        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++){
            Field field = fields[i];
            field.setAccessible(true);

            System.out.print(field.getName());
            System.out.print(":\n-> ");

            try {
                String fieldTypeName = field.getType().getSimpleName();
                if (fieldTypeName.equals("String")) {
                    field.set(obj, getString());
                } else if (fieldTypeName.equals("Integer") || fieldTypeName.equals("int")) {
                    field.set(obj, getInt());
                } else if (fieldTypeName.equals("Long") || fieldTypeName.equals("long")) {
                    field.set(obj, getLong());
                } else if (fieldTypeName.equals("Double") || fieldTypeName.equals("double")) {
                    field.set(obj, getDouble());
                } else if (fieldTypeName.equals("Boolean") || fieldTypeName.equals("boolean")) {
                    field.set(obj, getBoolean());
                }
                field.setAccessible(false);
            } catch (InputMismatchException e){
                scanner.nextLine();
                System.out.println("Invalid input. Try again");
                i--;
            } catch (IllegalAccessException e){
                System.out.println(e.getMessage());
                return null;
            }
        }

        System.out.print("Object created: ");
        System.out.print(cls.getSimpleName());
        System.out.print("[");
        for (int i = 0; i < fields.length; i++){
            Field field = fields[i];
            System.out.print(field.getName());
            System.out.print("=");

            field.setAccessible(true);
            Object objToPrint;
            try {
                objToPrint = field.get(obj);
            } catch (IllegalAccessException e) {
                exit(e.getMessage(), -1);
                return null;
            }
            field.setAccessible(false);

            if (field.getType().getSimpleName().equals("String")) {
                System.out.print("'");
                System.out.print(objToPrint);
                System.out.print("'");
            } else {
                System.out.print(objToPrint);
            }
            field.setAccessible(false);
            if (i + 1 != fields.length) System.out.print(", ");
            else System.out.print("]\n");
        }

        System.out.println(SEPARATOR);
        return obj;
    }
    private static void changeValue(Object obj, Class<?> cls){
        System.out.print("Enter name of the field for changing:\n");

        Field field = null;
        while (true) {
            System.out.print("-> ");
            String fieldName = getString();
            for (Field f : cls.getDeclaredFields()){
                if (fieldName.equals(f.getName())){
                    field = f;
                    break;
                }
            }
            if (field != null) break;
            System.out.println("Invalid field name. Try again");
        }

        field.setAccessible(true);
        String fieldTypeName = field.getType().getSimpleName();
        System.out.print("Enter ");
        System.out.print(fieldTypeName);
        System.out.print(" value:\n");
        while (true) {
            System.out.print("-> ");
            try {
                if (fieldTypeName.equals("String")) {
                    field.set(obj, getString());
                } else if (fieldTypeName.equals("Integer") || fieldTypeName.equals("int")) {
                    field.set(obj, getInt());
                } else if (fieldTypeName.equals("Long") || fieldTypeName.equals("long")) {
                    field.set(obj, getLong());
                } else if (fieldTypeName.equals("Double") || fieldTypeName.equals("double")) {
                    field.set(obj, getDouble());
                } else if (fieldTypeName.equals("Boolean") || fieldTypeName.equals("boolean")) {
                    field.set(obj, getBoolean());
                }
                field.setAccessible(false);
                break;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Invalid input. Try again");
            } catch (IllegalAccessException e) {
                exit(e.getMessage(), -1);
            }
        }

        Field[] fields = cls.getDeclaredFields();
        System.out.print("Object updated: ");
        System.out.print(cls.getSimpleName());
        System.out.print("[");
        for (int i = 0; i < fields.length; i++){
            field = fields[i];
            System.out.print(field.getName());
            System.out.print("=");

            field.setAccessible(true);
            Object objToPrint;
            try {
                objToPrint = field.get(obj);
            } catch (IllegalAccessException e) {
                exit(e.getMessage(), -1);
                return;
            }
            field.setAccessible(false);

            if (field.getType().getSimpleName().equals("String")) {
                System.out.print("'");
                System.out.print(objToPrint);
                System.out.print("'");
            } else {
                System.out.print(objToPrint);
            }
            field.setAccessible(false);
            if (i + 1 != fields.length) System.out.print(", ");
            else System.out.print("]\n");
        }

        System.out.println(SEPARATOR);
    }
    private static void callMethod(Object obj, Class<?> cls){
        System.out.println("Enter name of the method for call:");

        Method method = null;
        while (true){
            System.out.print("-> ");
            String[] input = getString().split(" ");
            if (input.length == 0){
                System.out.println("Invalid input");
                continue;
            }
            for (Method m : cls.getDeclaredMethods()){
                if (!input[0].equals(m.getName())) continue;
                if  (input.length - 1 != m.getParameterCount()) continue;
                boolean isFound = false;
                for (int i = 0; i < m.getParameterCount(); i++){
                    if (!m.getParameterTypes()[i].getSimpleName().equals(input[i + 1])) break;
                    if (i + 1 == m.getParameterCount()) isFound = true;
                }
                if (isFound){
                    method = m;
                    break;
                }
            }
            if (method != null) break;
            System.out.println("Invalid input");
        }

        Object[] args = new Object[method.getParameterCount()];
        for (int i = 0; i < args.length; i++){
            String argTypeName = method.getParameterTypes()[i].getSimpleName();
            System.out.print("Enter ");
            System.out.print(argTypeName);
            System.out.print(" value\n-> ");
            try {
                if (argTypeName.equals("String")) {
                    args[i] = getString();
                } else if (argTypeName.equals("Integer") || argTypeName.equals("int")) {
                    args[i] = getInt();
                } else if (argTypeName.equals("Long") || argTypeName.equals("long")) {
                    args[i] = getLong();
                } else if (argTypeName.equals("Double") || argTypeName.equals("double")) {
                    args[i] = getDouble();
                } else if (argTypeName.equals("Boolean") || argTypeName.equals("boolean")) {
                    args[i] = getBoolean();
                }
            } catch (InputMismatchException e){
                scanner.nextLine();
                System.out.println("Invalid input");
                i--;
            }
        }
        try {
            Object ret = method.invoke(obj, args);
            if (ret != null){
                System.out.println("Method returned:");
                System.out.println(ret);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            exit(e.getMessage(), -1);
        }
    }

    private static String getString(){
        return scanner.nextLine();
    }
    private static int getInt(){
        int val = scanner.nextInt();
        scanner.nextLine();
        return val;
    }
    private static double getDouble(){
        double val = scanner.nextDouble();
        scanner.nextLine();
        return val;
    }
    private static long getLong(){
        long val = scanner.nextLong();
        scanner.nextLine();
        return val;
    }
    private static boolean getBoolean(){
        boolean val = scanner.nextBoolean();
        scanner.nextLine();
        return val;
    }

    private static void exit(String msg, int exitCode){
        if (exitCode == 0){
            System.out.println(msg);
        } else {
            System.err.println(msg);
        }
        System.exit(exitCode);
    }
}

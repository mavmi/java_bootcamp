package app;

import annotation.OrmColumn;
import annotation.OrmColumnId;
import annotation.OrmEntity;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrmManager {
    private final String PACKAGE_NAME = "model";
    private final String DB_NAME = "day07";

    private Map<String, Class<?>> listOfClasses;
    private HikariDataSource hikariDataSource;

    public OrmManager(){
        listOfClasses = getListOfClasses();
        if (listOfClasses == null) throw new RuntimeException("Cannot upload list of model classes");
        hikariDataSource = getDataSource();
    }

    public void createTables(){
        for (String className : listOfClasses.keySet()){
            Class<?> cls = listOfClasses.get(className);
            if (!cls.isAnnotationPresent(OrmEntity.class)) continue;
            StringBuilder query = new StringBuilder();

            OrmEntity ormEntity = cls.getAnnotation(OrmEntity.class);
            final String tableName = ormEntity.table();

            query.append("drop table if exists ")
                    .append(tableName)
                    .append("; ")
                    .append("create table if not exists ")
                    .append(tableName)
                    .append("(");

            boolean isFirst = true;
            Field[] fields = cls.getDeclaredFields();
            for (int i = 0; i < fields.length; i++){
                Field field = fields[i];
                if (field.isAnnotationPresent(OrmColumnId.class)){
                    if (isFirst) isFirst = false;
                    else query.append(",");

                    String fieldTypeName = field.getType().getSimpleName();
                    if (!fieldTypeName.equals("long") && !fieldTypeName.equals("Long")){
                        throw new RuntimeException("Bad type for id value! Must be long");
                    } else {
                        query.append("id bigint generated by default as identity primary key");
                    }
                } else if (field.isAnnotationPresent(OrmColumn.class)){
                    if (isFirst) isFirst = false;
                    else query.append(",");

                    OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
                    String fieldTypeName = field.getType().getSimpleName();
                    String columnName = ormColumn.name();
                    int columnLen = ormColumn.length();

                    query.append(columnName)
                            .append(" ");
                    if (fieldTypeName.equals("String")){
                        query.append("varchar(")
                                .append(columnLen)
                                .append(")");
                    } else if (fieldTypeName.equals("Integer")){
                        query.append("int");
                    } else if (fieldTypeName.equals("Long")){
                        query.append("bigint");
                    } else if (fieldTypeName.equals("Double")){
                        query.append("double");
                    } else if (fieldTypeName.equals("Boolean")){
                        query.append("boolean");
                    }
                }
            }

            query.append(");");
            updateQuery(query.toString());
        }
    }
    public void save(Object entity){
        Class<?> cls = entity.getClass();
        if (!cls.isAnnotationPresent(OrmEntity.class)) return;

        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        OrmEntity ormEntity = cls.getAnnotation(OrmEntity.class);
        final String tableName = ormEntity.table();

        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++){
            try {
                Field field = fields[i];
                field.setAccessible(true);
                if (field.isAnnotationPresent(OrmColumnId.class)) {
                    columns.add("id");
                    values.add(field.get(entity));
                } else if (field.isAnnotationPresent(OrmColumn.class)) {
                    OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
                    columns.add(ormColumn.name());
                    values.add(field.get(entity));
                }
                field.setAccessible(false);
            } catch (IllegalAccessException e) {
                System.err.println(e.getMessage());
                return;
            }
        }

        StringBuilder query = new StringBuilder();
        query.append("insert into ")
                .append(tableName)
                .append("(");
        for (int i = 0; i < columns.size(); i++){
            query.append(columns.get(i));
            if (i + 1 != columns.size()) query.append(",");
            else query.append(")");
        }
        query.append(" values(");
        for (int i = 0; i < values.size(); i++){
            Object obj = values.get(i);
            if (obj.getClass().getSimpleName().equals("String")){
                query.append("'");
                query.append(obj);
                query.append("'");
            } else {
                query.append(obj);
            }
            if (i + 1 != columns.size()) query.append(",");
            else query.append(");");
        }

        updateQuery(query.toString());
    }
    public void update(Object entity){
        Class<?> cls = entity.getClass();
        if (!cls.isAnnotationPresent(OrmEntity.class)) return;

        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        OrmEntity ormEntity = cls.getAnnotation(OrmEntity.class);
        final String tableName = ormEntity.table();

        long id = -1;
        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++){
            try {
                Field field = fields[i];
                field.setAccessible(true);
                if (field.isAnnotationPresent(OrmColumnId.class)) {
                    id = (Long)field.get(entity);
                } else if (field.isAnnotationPresent(OrmColumn.class)) {
                    OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
                    columns.add(ormColumn.name());
                    values.add(field.get(entity));
                }
                field.setAccessible(false);
            } catch (IllegalAccessException e) {
                System.err.println(e.getMessage());
                return;
            }
        }

        if (id == -1) return;
        StringBuilder query = new StringBuilder();
        query.append("update ")
                .append(tableName)
                .append(" set ");
        for (int i = 0; i < values.size(); i++){
            String columnName = columns.get(i);
            Object newValue = values.get(i);

            query.append(columnName)
                    .append("=");
            if (newValue.getClass().getSimpleName().equals("String")){
                query.append("'");
                query.append(newValue);
                query.append("'");
            } else {
                query.append(newValue);
            }
            if (i + 1 != columns.size()) query.append(",");
        }
        query.append(" where id=")
                .append(id)
                .append(";");

        updateQuery(query.toString());
    }
    public <T> T findById(Long id, Class<T> aClass){
        if (!aClass.isAnnotationPresent(OrmEntity.class)) return null;

        String tableName = aClass.getAnnotation(OrmEntity.class).table();
        ResultSet resultSet = executeQuery(
                new StringBuilder()
                        .append("select * from ")
                        .append(tableName)
                        .append(" where id=")
                        .append(id)
                        .append(";")
                        .toString()
        );

        try {
            if (!resultSet.next()) return null;
            T newInstance = aClass.newInstance();
            for (Field field : aClass.getDeclaredFields()){
                field.setAccessible(true);
                if (field.isAnnotationPresent(OrmColumnId.class)){
                    field.set(newInstance, resultSet.getLong("id"));
                } else if (field.isAnnotationPresent(OrmColumn.class)){
                    OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
                    field.set(newInstance, resultSet.getObject(ormColumn.name()));
                }
                field.setAccessible(false);
            }
            return newInstance;
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }


    private Map<String, Class<?>> getListOfClasses(){
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
    private HikariDataSource getDataSource(){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/" + DB_NAME);
        hikariConfig.setUsername("pmaryjo");
        hikariConfig.setPassword("");
        hikariConfig.setValidationTimeout(300_000);

        return new HikariDataSource(hikariConfig);
    }
    private ResultSet executeQuery(String sql){
        try {
            printQuery(sql);
            return hikariDataSource.getConnection().createStatement().executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void updateQuery(String sql){
        try {
            printQuery(sql);
            hikariDataSource.getConnection().createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void printQuery(String sql){
        System.out.println("GENERATED SQL QUERY:");
        System.out.println(sql);
    }
}

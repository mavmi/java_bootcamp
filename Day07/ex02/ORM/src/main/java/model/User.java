package model;

import annotation.OrmColumn;
import annotation.OrmColumnId;
import annotation.OrmEntity;

@OrmEntity(table = "simple_user")
public class User {
    @OrmColumnId
    private Long id;
    @OrmColumn(name = "first_name_1", length = 11)
    private String firstName;
    @OrmColumn(name = "last_name_2", length = 12)
    private String lastName;
    @OrmColumn(name = "age_3")
    private Integer age;

    public User(){

    }
    public User(Long id, String firstName, String lastName, Integer age){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public void setId(long id){
        this.id = id;
    }
    public long getId(){
        return id;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getFirstName(){
        return firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public String getLastName(){
        return lastName;
    }

    public void setAge(int age){
        this.age = age;
    }
    public int getAge(){
        return age;
    }

    @Override
    public String toString() {
        return
                "[\n\tid=" + id + "\n" +
                "\tfirstName=" + firstName + "\n" +
                "\tlastName=" + lastName + "\n" +
                "\tage=" + age + "\n]";
    }
}

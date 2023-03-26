package edu.school21.models;

public class Product {
    private long id;
    private String name;
    private int price;

    public Product(long id, String name, int price){
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public void setId(long id){
        this.id = id;
    }
    public long getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setPrice(int price){
        this.price = price;
    }
    public int getPrice(){
        return price;
    }

    @Override
    public int hashCode() {
        int code = (int)id + price;

        for (int i = 0; i < name.length(); i++){
            code += (int)name.charAt(i);
        }

        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product)) return false;
        Product obj1 = (Product)obj;
        if (this.id != obj1.id) return false;
        if (!this.name.equals(obj1.name)) return false;
        if (this.price != obj1.price) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Product: {\n")
                .append("id=")
                .append(id)
                .append(",\n")
                .append("name=")
                .append(name)
                .append(",\n")
                .append("price=")
                .append(price)
                .append("\n}");
        return result.toString();
    }
}

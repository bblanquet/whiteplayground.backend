package whiteplayground.test.transfer.model;
import org.springframework.data.annotation.Id;

public class Customer {
    @Id
    private int id;
    private String name;
    private String password;

    public String getName(){
        return this.name;
    }

    public String getPassword(){
        return this.password;
    }

    public Customer(){

    }

    public Customer(String name,String password){
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }
}

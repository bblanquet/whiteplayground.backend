package whiteplayground.test.transfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.time.Instant;

public class Account {
    @Id
    private int id;

    private String currency;

    private Instant date;

    @JsonIgnore
    private Customer customer;

    public Account(){

    }

    public Account(Customer customer, String currency){
        this.customer = customer;
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}


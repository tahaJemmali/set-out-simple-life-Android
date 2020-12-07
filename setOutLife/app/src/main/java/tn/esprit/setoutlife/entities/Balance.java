package tn.esprit.setoutlife.entities;

import java.util.Date;

import tn.esprit.setoutlife.Enums.BalanceEnum;

public class Balance {
    private String id;
    private String name;
    private BalanceEnum type;
    private Double amount;
    private Date dateCreated;
    public Balance(){

    }

    public Balance(String id,String name, BalanceEnum type, Double amount, Date dateCreated) {
        this.id=id;
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BalanceEnum getType() {
        return type;
    }

    public void setType(BalanceEnum type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                ", dateCreated=" + dateCreated +
                '}';
    }
}

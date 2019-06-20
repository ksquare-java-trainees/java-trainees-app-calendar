package com.ksquareinc.calendar.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Objects;

@Entity(name ="customer")
@Transactional
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    private String customerAPIUrl;

    private String endPoint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getCustomerAPIUrl() {
        return customerAPIUrl;
    }

    public void setCustomerAPIUrl(String customerAPIUrl) {
        this.customerAPIUrl = customerAPIUrl;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", customerAPIUrl='" + customerAPIUrl + '\'' +
                ", endPoint='" + endPoint + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id.equals(customer.id) &&
                Objects.equals(companyName, customer.companyName) &&
                Objects.equals(customerAPIUrl, customer.customerAPIUrl) &&
                Objects.equals(endPoint, customer.endPoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyName, customerAPIUrl, endPoint);
    }
}

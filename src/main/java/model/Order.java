package model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

//class to store data about orders
public class Order {
    private String orderId;
    private BigDecimal orderValue;
    private Duration pickingTime;
    private LocalTime completeBy;
    private LocalTime dependency;
    private Duration valueDependency;

    public Order(String orderId, BigDecimal orderValue, Duration pickingTime, LocalTime completeBy, LocalTime dependency, Duration valueDependency) {
        this.orderId = orderId;
        this.orderValue = orderValue;
        this.pickingTime = pickingTime;
        this.completeBy = completeBy;
        this.dependency = dependency;
        this.valueDependency = valueDependency;
    }
    public Duration getValueDependency() {
        return valueDependency;
    }

    public void setValueDependency(Duration valueDependency) {
        this.valueDependency = valueDependency;
    }
    public LocalTime getDependency() {
        return dependency;
    }
    public void setDependency(LocalTime dependency) {
        this.dependency = dependency;
    }
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(BigDecimal orderValue) {
        this.orderValue = orderValue;
    }

    public Duration getPickingTime() {
        return pickingTime;
    }

    public void setPickingTime(Duration pickingTime) {
        this.pickingTime = pickingTime;
    }

    public LocalTime getCompleteBy() {
        return completeBy;
    }

    public void setCompleteBy(LocalTime completeBy) {
        this.completeBy = completeBy;
    }
}
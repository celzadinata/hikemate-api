package com.enigmacamp.constant.enums;

public enum TransactionStatus {
    ORDERED("ordered", "Ordered"),
    SETTLEMENT("settlement", "Settlemnet"),
    PENDING("pending", "Pending"),
    CANCEL("cancel", "Cancel"),
    FAILURE("failure", "Failure"),
    EXPIRED("expired", "Expired"),
    DENY("deny", "Deny");

    private final String name;
    private final String description;

    TransactionStatus(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

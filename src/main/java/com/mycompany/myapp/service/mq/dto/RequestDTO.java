package com.mycompany.myapp.service.mq.dto;

import java.io.Serializable;

public class RequestDTO implements Serializable {
    private String quantity;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}

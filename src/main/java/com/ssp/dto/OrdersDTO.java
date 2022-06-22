package com.ssp.dto;

import com.ssp.entities.Orders;
import lombok.Data;

@Data
public class OrdersDTO extends Orders {
    int sumNum;
}

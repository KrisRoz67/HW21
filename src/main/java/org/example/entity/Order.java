package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.OffsetTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private int id;
    private int customerId;
    private int productId;

}
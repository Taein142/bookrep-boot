package com.rep.book.bookrepboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTO {

    @NonNull
    private String userEmail;

    @NonNull
    private Double x;

    @NonNull
    private Double y;

    @NonNull
    private List<String> pickupList;

    @NonNull
    private List<String> deliveryList;

}

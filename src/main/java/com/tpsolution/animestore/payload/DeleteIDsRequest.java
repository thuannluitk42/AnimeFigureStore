package com.tpsolution.animestore.payload;

import lombok.Data;

import java.util.List;

@Data
public class DeleteIDsRequest {
    List<Integer> list;
}

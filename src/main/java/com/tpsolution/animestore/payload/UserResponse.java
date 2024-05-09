package com.tpsolution.animestore.payload;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private List<UserDetailResponse> list;
    private int totalPage;
    private int currentPage;
    private Long totalElement;
}

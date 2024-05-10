package com.tpsolution.animestore.payload;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Data
public class UserResponse extends PagingResponse {
    private List<UserDetailResponse> list;
}

package com.tpsolution.animestore.service.imp;

import com.tpsolution.animestore.payload.AddUserRequest;
import com.tpsolution.animestore.payload.DataResponse;

public interface ClientServiceImp {
    DataResponse insertNewClient(AddUserRequest request);
}

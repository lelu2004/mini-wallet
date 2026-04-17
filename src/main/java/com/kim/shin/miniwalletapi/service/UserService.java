package com.kim.shin.miniwalletapi.service;

import com.kim.shin.miniwalletapi.dto.request.RegisterRequest;
import com.kim.shin.miniwalletapi.dto.response.UserResponse;

public interface UserService {

    UserResponse register(RegisterRequest request);
}
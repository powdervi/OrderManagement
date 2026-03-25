package com.example.ordermanagement.controller;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.dto.request.UserCreateReq;
import com.example.ordermanagement.dto.request.UserSearchReq;
import com.example.ordermanagement.dto.request.UserUpdateReq;
import com.example.ordermanagement.dto.response.UserRes;
import com.example.ordermanagement.entity.User;
import com.example.ordermanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<BaseResponse<UserRes>> createUser(@RequestBody @Valid UserCreateReq userCreateReq){
        User user = userService.createUser(userCreateReq);
        UserRes userRes = modelMapper.map(user, UserRes.class);
        return ResponseEntity.ok(BaseResponse.ofSuccess(userRes));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<UserRes>> updateUser(
            @PathVariable String id,
            @RequestBody @Valid UserUpdateReq req) {
        User user = userService.updateUser(id, req);
        UserRes userRes = modelMapper.map(user, UserRes.class);
        return ResponseEntity.ok(BaseResponse.ofSuccess(userRes));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<String>> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(BaseResponse.ofSuccess("User deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<UserRes>> getById(@PathVariable String id){
        User user = userService.getById(id);
        UserRes userRes = modelMapper.map(user, UserRes.class);
        return ResponseEntity.ok(BaseResponse.ofSuccess(userRes));
    }

    @GetMapping("/search")
    public ResponseEntity<BaseResponse<List<UserRes>>> filter(UserSearchReq userSearchReq) {
        List<User> userList = userService.search(userSearchReq);
        List<UserRes> productOfferingResList = modelMapper.map(userList, new TypeToken<List<UserRes>>(){}.getType());
        return ResponseEntity.ok(BaseResponse.ofSuccess(productOfferingResList));
    }

}

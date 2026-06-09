package com.example.ordermanagement.controller;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.dto.request.UserCreateReq;
import com.example.ordermanagement.dto.request.UserSearchReq;
import com.example.ordermanagement.dto.request.UserUpdateReq;
import com.example.ordermanagement.dto.response.UserRes;
import com.example.ordermanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BaseResponse<UserRes>> createUser(@RequestBody @Valid UserCreateReq userCreateReq){
        UserRes userRes = userService.createUser(userCreateReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.ofSuccess(userRes));
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<UserRes>> updateUser(
            @PathVariable String id,
            @RequestBody @Valid UserUpdateReq req) {
        UserRes userRes = userService.updateUser(id, req);
        return ResponseEntity.ok(BaseResponse.ofSuccess(userRes));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<String>> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<UserRes>> getById(@PathVariable String id){
        UserRes userRes = userService.getById(id);
        return ResponseEntity.ok(BaseResponse.ofSuccess(userRes));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<List<UserRes>>> filter(
            @RequestParam(name = "page_size", defaultValue = "20") Integer pageSize,
            @RequestParam(name = "page_number", defaultValue = "0") Integer pageNumber,
            UserSearchReq userSearchReq) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<UserRes> result = userService.search(userSearchReq, pageable);
        return ResponseEntity.ok(BaseResponse.ofSuccess(result));
    }

}

package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.UserStatus;
import com.example.ordermanagement.dto.request.UserCreateReq;
import com.example.ordermanagement.dto.request.UserSearchReq;
import com.example.ordermanagement.dto.request.UserUpdateReq;
import com.example.ordermanagement.dto.response.UserRes;
import com.example.ordermanagement.entity.User;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.mapper.UserMapper;
import com.example.ordermanagement.repository.UserRepo;
import com.example.ordermanagement.service.UserService;
import com.example.ordermanagement.service.spec.UserSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserRes createUser(UserCreateReq userCreateReq) {
        User user = userMapper.toEntity(userCreateReq);
        user.setId(null);
        user.setPasswordHash(passwordEncoder.encode(userCreateReq.getPasswordHash()));

        User savedUser = userRepo.save(user);
        return userMapper.toRes(savedUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserRes updateUser(String id, UserUpdateReq userUpdateReq) {
        User user = getUserEntity(id);

        userMapper.updateUserFromReq(userUpdateReq, user);
        User savedUser = userRepo.save(user);

        return userMapper.toRes(savedUser);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUser(String id) {
        User user = getUserEntity(id);
        user.setStatus(UserStatus.INACTIVE);
        userRepo.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserRes getById(String id) {
        User user = getUserEntity(id);

        return userMapper.toRes(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserRes> search(UserSearchReq req, Pageable pageable) {

        Specification<User> spec = Specification.unrestricted();

        if (req.getUsername() != null && !req.getUsername().isBlank()) {
            spec = spec.and(UserSpecification.likeUsername(req.getUsername()));
        }

        if (req.getFirstName() != null && !req.getFirstName().isBlank()) {
            spec = spec.and(UserSpecification.likeFirstName(req.getFirstName()));
        }

        if (req.getLastName() != null && !req.getLastName().isBlank()) {
            spec = spec.and(UserSpecification.likeLastName(req.getLastName()));
        }

        if (req.getEmail() != null && !req.getEmail().isBlank()) {
            spec = spec.and(UserSpecification.likeEmail(req.getEmail()));
        }

        if (req.getPhone() != null && !req.getPhone().isBlank()) {
            spec = spec.and(UserSpecification.likePhone(req.getPhone()));
        }

        if (req.getStatus() != null && !req.getStatus().isBlank()) {
            spec = spec.and(UserSpecification.equalStatus(req.getStatus()));
        }

        if (req.getRole() != null && !req.getRole().isBlank()) {
            spec = spec.and(UserSpecification.equalRole(req.getRole()));
        }

        Page<User> users = userRepo.findAll(spec, pageable);

        return users.map(userMapper::toRes);
    }

    private User getUserEntity(String id) {
        User user = userRepo.findById(id).orElseThrow(() -> new MHException(MHErrors.USER_NOT_FOUND));
        return user;
    }

}

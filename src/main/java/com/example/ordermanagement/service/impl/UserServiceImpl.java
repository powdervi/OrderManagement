package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.UserRole;
import com.example.ordermanagement.common.UserStatus;
import com.example.ordermanagement.dto.request.UserCreateReq;
import com.example.ordermanagement.dto.request.UserSearchReq;
import com.example.ordermanagement.dto.request.UserUpdateReq;
import com.example.ordermanagement.entity.User;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.UserRepo;
import com.example.ordermanagement.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;
    @Override
    public User createUser(UserCreateReq userCreateReq) {
        User user = modelMapper.map(userCreateReq, User.class);
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepo.save(user);
    }

    @Override
    @Transactional
    public User updateUser(String id, UserUpdateReq userUpdateReq) {
        User user = getUserEntity(id);
        if(userUpdateReq.getFirstName() != null && !userUpdateReq.getFirstName().isBlank()){
            user.setFirstName(userUpdateReq.getFirstName());
        }

        if(userUpdateReq.getLastName() != null && !userUpdateReq.getLastName().isBlank()){
            user.setLastName(userUpdateReq.getLastName());
        }

        if(userUpdateReq.getUsername() != null && !userUpdateReq.getUsername().isBlank()){
            user.setUsername(userUpdateReq.getUsername());
        }

        if(userUpdateReq.getPhone() != null && !userUpdateReq.getPhone().isBlank()){
            user.setPhone(userUpdateReq.getPhone());
        }

        if(userUpdateReq.getEmail() != null && !userUpdateReq.getEmail().isBlank()){
            user.setEmail(userUpdateReq.getEmail());
        }
        return userRepo.save(user);

    }

    @Override
    public void deleteUser(String id) {
        User user = getUserEntity(id);
        user.setStatus(UserStatus.INACTIVE);
        userRepo.save(user);
    }

    @Override
    public User getById(String id) {
        return getUserEntity(id);
    }

    @Override
    public List<User> search(UserSearchReq userSearchReq) {
        String username = userSearchReq.getUsername();
        String firstName = userSearchReq.getFirstName();
        String lastName = userSearchReq.getLastName();
        String email = userSearchReq.getEmail();
        String phone = userSearchReq.getPhone();
        String role = userSearchReq.getRole();
        String status = userSearchReq.getStatus();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (username != null && !username.isBlank()) {
            predicates.add(cb.like(root.get("username"), "%" + username + "%"));
        }

        if (firstName != null && !firstName.isBlank()) {
            predicates.add(cb.like(root.get("firstName"), "%" + firstName + "%"));
        }

        if (lastName != null && !lastName.isBlank()) {
            predicates.add(cb.like(root.get("lastName"), "%" + lastName + "%"));
        }

        if (email != null && !email.isBlank()) {
            predicates.add(cb.like(root.get("email"), "%" + email + "%"));
        }

        if (phone != null && !phone.isBlank()) {
            predicates.add(cb.like(root.get("phone"), "%" + phone + "%"));
        }

        if (status != null && !status.isBlank()) {
            predicates.add(cb.equal(root.get("status"), status));
        }

        if (role != null && !role.isBlank()) {
            predicates.add(cb.equal(root.get("role"), role));
        }

        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        return entityManager.createQuery(query).getResultList();
    }

    private User getUserEntity(@NotBlank(message = "id cant be blank") String id) {
        User user = userRepo.findById(id).orElseThrow(() -> new MHException(MHErrors.USER_NOT_FOUND));
        return user;
    }
}

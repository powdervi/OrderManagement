package com.example.ordermanagement.service.spec;

import com.example.ordermanagement.entity.User;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class UserSpecification {

    public static Specification<User> likeUsername(String username) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (username == null || username.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(
                    cb.lower(root.get("username")),
                    "%" + username.toLowerCase() + "%"
            );
        };
    }

    public static Specification<User> likeFirstName(String firstName) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (firstName == null || firstName.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
        };
    }

    public static Specification<User> likeLastName(String lastName) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (lastName == null || lastName.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
        };
    }

    public static Specification<User> likeEmail(String email) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (email == null || email.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
        };
    }

    public static Specification<User> likePhone(String phone) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (phone == null || phone.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(root.get("phone"), "%" + phone + "%");
        };
    }

    public static Specification<User> equalStatus(String status) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (status == null || status.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<User> equalRole(String role) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (role == null || role.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("role"), role);
        };
    }
}

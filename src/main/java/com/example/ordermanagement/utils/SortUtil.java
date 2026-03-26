package com.example.ordermanagement.utils;

import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SortUtil {

    public static Sort buildSort(String sort) {
        List<Sort.Order> orders = new ArrayList<>();

        // default sort
        if (!StringUtils.hasText(sort)) {
            orders.add(new Sort.Order(Sort.Direction.ASC, "createdAt"));
            return Sort.by(orders);
        }

        Set<String> allowedFields = Set.of("name", "basePrice");

        String[] sortItems = sort.split(",");

        for (String item : sortItems) {
            if (!StringUtils.hasText(item)) {
                continue;
            }

            String[] parts = item.split(":");
            if (parts.length != 2) {
                continue;
            }

            String field = parts[0].trim();
            String direction = parts[1].trim();

            if (!allowedFields.contains(field)) {
                continue;
            }

            if (direction.equalsIgnoreCase("asc")) {
                orders.add(new Sort.Order(Sort.Direction.ASC, field));
            } else if (direction.equalsIgnoreCase("desc")) {
                orders.add(new Sort.Order(Sort.Direction.DESC, field));
            }
        }

        if (orders.isEmpty()) {
            orders.add(new Sort.Order(Sort.Direction.ASC, "createdAt"));
        }

        return Sort.by(orders);
    }
}

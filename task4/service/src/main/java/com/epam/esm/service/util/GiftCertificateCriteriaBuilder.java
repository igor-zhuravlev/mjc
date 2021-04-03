package com.epam.esm.service.util;

import com.epam.esm.repository.query.criteria.GiftCertificateCriteria;
import com.epam.esm.service.dto.GiftCertificateParamDto;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

public final class GiftCertificateCriteriaBuilder {

    private static final String PARAM_DELIMITER = ",";

    public static Sort buildSortByParams(List<String> sorts) {
        List<Sort.Order> orders = new ArrayList<>();
        Iterator<String> iterator = sorts.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            if (!item.contains(PARAM_DELIMITER)) {
                String direction = iterator.next().strip();
                Optional<Sort.Order> order = buildOrder(item.strip(), direction);
                order.ifPresent(orders::add);
                break;
            }
            String[] sortItem = item.split(PARAM_DELIMITER);
            String direction = sortItem[1].strip();
            Optional<Sort.Order> order = buildOrder(sortItem[0].strip(), direction);
            order.ifPresent(orders::add);
        }
        return Sort.by(orders);
    }

    private static Optional<Sort.Order> buildOrder(String value, String direction) {
        if (direction.equalsIgnoreCase(Sort.Direction.DESC.name())) {
            return Optional.of(Sort.Order.desc(value));
        } else if (direction.equalsIgnoreCase(Sort.Direction.ASC.name())) {
            return Optional.of(Sort.Order.asc(value));
        }
        return Optional.empty();
    }

    public static GiftCertificateCriteria build(GiftCertificateParamDto giftCertificateParam) {
        GiftCertificateCriteria criteria = new GiftCertificateCriteria();
        String name = giftCertificateParam.getName();
        if (name != null) {
            criteria.setName(name.strip());
        }
        String description = giftCertificateParam.getDescription();
        if (description != null) {
            criteria.setName(description.strip());
        }
        List<String> tags = giftCertificateParam.getTags();
        if (tags != null) {
            String[] formattedTags = tags.stream()
                    .map(String::strip)
                    .toArray(String[]::new);
            criteria.setTagNames(formattedTags);
        }
        List<String> sort = giftCertificateParam.getSort();
        if (sort != null) {
            Sort formattedSort = buildSortByParams(sort);
            criteria.setSort(formattedSort);
        }
        return criteria;
    }
}

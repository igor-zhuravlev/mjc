package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.util.Criteria;
import com.epam.esm.repository.util.QueryUtil;
import com.epam.esm.repository.exception.RepositoryException;
import com.epam.esm.repository.impl.mapper.GiftCertificateMapper;
import com.epam.esm.repository.impl.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String FIND_ALL_QUERY =
            "SELECT gc.id gc_id, gc.name gc_name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date, t.id t_id, t.name t_name " +
            "FROM gift_certificate gc " +
            "LEFT JOIN gift_certificate_tag gct ON gc.id = gct.gift_certificate_id " +
            "LEFT JOIN tag t ON gct.tag_id = t.id";
    private static final String FIND_BY_ID_QUERY =
            "SELECT id gc_id, name gc_name, description, price, duration, create_date, last_update_date " +
            "FROM gift_certificate " +
            "WHERE id = ?";
    private static final String FIND_BY_NAME_QUERY =
            "SELECT id gc_id, name gc_name, description, price, duration, create_date, last_update_date " +
            "FROM gift_certificate " +
            "WHERE name = ?";
    private static final String SAVE_QUERY =
            "INSERT INTO gift_certificate (name, description, price, duration, create_date) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String SAVE_FK_GC_TAG_QUERY =
            "INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (?, ?)";
    private static final String DELETE_BY_ID_QUERY =
            "DELETE FROM gift_certificate WHERE id = ?";
    private static final String UPDATE_BY_ID_QUERY =
            "UPDATE gift_certificate SET " +
            "name = COALESCE(?, name), " +
            "description = COALESCE(?, description), " +
            "price = COALESCE(?, price), " +
            "duration = COALESCE(?, duration), " +
            "last_update_date = ? " +
            "WHERE id = ?";

    @Override
    public GiftCertificate findById(Long id) throws RepositoryException {
        try {
            return jdbcTemplate.query(FIND_BY_ID_QUERY, giftResultSetExtractor(), id);
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public GiftCertificate findByName(String name) throws RepositoryException {
        try {
            return jdbcTemplate.query(FIND_BY_NAME_QUERY, giftResultSetExtractor(), name);
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Long update(GiftCertificate giftCertificate) throws RepositoryException {
        try {
            final long count = jdbcTemplate.update(
                    UPDATE_BY_ID_QUERY,
                    giftCertificate.getName(),
                    giftCertificate.getDescription(),
                    giftCertificate.getPrice(),
                    giftCertificate.getDuration(),
                    giftCertificate.getLastUpdateDate(),
                    giftCertificate.getId()
            );

            if (giftCertificate.getTags() != null) {
                saveGiftCertificateTagFK(giftCertificate.getId(), giftCertificate.getTags());
            }

            return count;
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public List<GiftCertificate> findAll(Criteria criteria) throws RepositoryException {
        try {
            final String query = QueryUtil.buildQuery(FIND_ALL_QUERY, criteria);
            return jdbcTemplate.query(query, giftListResultSetExtractor());
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    private void saveGiftCertificateTagFK(Long giftCertificateId, Set<Tag> tags) {
        tags.stream()
                .mapToLong(Tag::getId)
                .forEach(tagId -> jdbcTemplate.update(SAVE_FK_GC_TAG_QUERY, giftCertificateId, tagId));
    }

    @Override
    public Long deleteById(Long id) throws RepositoryException {
        try {
            return (long) jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    private ResultSetExtractor<List<GiftCertificate>> giftListResultSetExtractor() {
        return rs -> {
            GiftCertificateMapper giftCertificateMapper = new GiftCertificateMapper();
            TagMapper tagMapper = new TagMapper();
            Map<GiftCertificate, Set<Tag>> map = new LinkedHashMap<>();
            while (rs.next()) {
                GiftCertificate gift = giftCertificateMapper.mapRow(rs, rs.getRow());
                Tag tag = tagMapper.mapRow(rs, rs.getRow());
                if (!map.containsKey(gift)) {
                    map.put(gift, new HashSet<>());
                }
                map.get(gift).add(tag);
            }
            map.forEach(GiftCertificate::setTags);
            return new ArrayList<>(map.keySet());
        };
    }

    private ResultSetExtractor<GiftCertificate> giftResultSetExtractor() {
        return rs -> {
            GiftCertificate giftCertificate = null;
            GiftCertificateMapper giftCertificateMapper = new GiftCertificateMapper();
            if (rs.next()) {
                giftCertificate = giftCertificateMapper.mapRow(rs, rs.getRow());
            }
            return giftCertificate;
        };
    }

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) throws RepositoryException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            PreparedStatementCreator preparedStatementCreator = con -> {
                PreparedStatement preparedStatement = con.prepareStatement(SAVE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, giftCertificate.getName());
                preparedStatement.setString(2, giftCertificate.getDescription());
                preparedStatement.setBigDecimal(3, giftCertificate.getPrice());
                preparedStatement.setInt(4, giftCertificate.getDuration());
                preparedStatement.setTimestamp(5, Timestamp.valueOf(giftCertificate.getCreateDate()));
                return preparedStatement;
            };

            jdbcTemplate.update(preparedStatementCreator, keyHolder);

            giftCertificate.setId((Long) keyHolder.getKeys().get(GiftCertificateMapper.SECONDARY_ID));

            if (giftCertificate.getTags() != null) {
                saveGiftCertificateTagFK(giftCertificate.getId(), giftCertificate.getTags());
            }

            return giftCertificate;
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }
}

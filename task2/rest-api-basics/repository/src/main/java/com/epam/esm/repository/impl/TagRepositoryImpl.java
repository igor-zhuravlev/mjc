package com.epam.esm.repository.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.exception.RepositoryException;
import com.epam.esm.repository.impl.query.QueryBuilderProvider;
import com.epam.esm.repository.impl.mapper.TagMapper;
import com.epam.esm.repository.criteria.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.*;

@Repository
public class TagRepositoryImpl implements TagRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private QueryBuilderProvider queryBuilderProvider;

    private static final String FIND_QUERY = "SELECT id t_id, name t_name FROM tag";
    private static final String SAVE_QUERY = "INSERT INTO tag (name) VALUES (?)";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM tag WHERE id = ?";

    @Override
    public List<Tag> findAll() throws RepositoryException {
        try {
            return jdbcTemplate.query(FIND_QUERY, tagMapper);
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    private ResultSetExtractor<Tag> tagResultSetExtractor() {
        return rs -> {
            Tag tag = null;
            if (rs.next()) {
                tag = tagMapper.mapRow(rs, rs.getRow());
            }
            return tag;
        };
    }

    @Override
    public Tag find(Criteria criteria) throws RepositoryException {
        try {
            final String query = queryBuilderProvider.getQueryBuilder()
                    .build(FIND_QUERY, criteria, "");
            return jdbcTemplate.query(query, tagResultSetExtractor());
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Tag save(Tag tag) throws RepositoryException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            PreparedStatementCreator preparedStatementCreator = con -> {
                PreparedStatement preparedStatement = con.prepareStatement(SAVE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, tag.getName());
                return preparedStatement;
            };

            jdbcTemplate.update(preparedStatementCreator, keyHolder);

            tag.setId((Long) keyHolder.getKeys().get(TagMapper.SECONDARY_ID));

            return tag;
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Long deleteById(Long id) throws RepositoryException {
        try {
            return (long) jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }
}

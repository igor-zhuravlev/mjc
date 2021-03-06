package com.epam.esm.repository.impl.mapper;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagMapper implements RowMapper<Tag> {

    public static final String ID = "t_id";
    public static final String NAME = "t_name";

    public static final String SECONDARY_ID = "id";

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getLong(ID));
        tag.setName(rs.getString(NAME));
        return tag;
    }
}

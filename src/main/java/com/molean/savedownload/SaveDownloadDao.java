package com.molean.savedownload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SaveDownloadDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SaveDownloadDao() {
    }

    public void removeExpire() {
        String sql = "delete from isletopia_save where ? - isletopia_save.time > 10*60*1000;";
        jdbcTemplate.update(sql, System.currentTimeMillis());
    }

    public SaveBean getSave(String username) {
        String sql = "select * from minecraft.isletopia_save where player=?";
        List<SaveBean> query = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SaveBean.class), username);
        if (query.isEmpty()) {
            return null;
        }
        return query.get(0);
    }
}

package kr.jay.okrver3.util;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;


@Profile("test")
@Component
public class DataLoader {

    private final DataSource dataSource;


    public DataLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void loadData() {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-user.sql"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package kr.jay.okrver3.util;

import static kr.jay.okrver3.acceptance.user.UserAcceptanceTestData.*;

import java.sql.Connection;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.jay.okrver3.acceptance.user.UserAcceptanceTestData;
import kr.jay.okrver3.domain.user.User;

@Profile("test")
@Component
public class DataLoader {

    private final DataSource dataSource;

    @PersistenceContext
    public EntityManager em;


    public DataLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Transactional
    public void loadData() {
        Arrays.stream(values()).forEach(user -> {
            em.persist(getUser(user));
        });
        em.flush();
        em.clear();

        // try (Connection conn = dataSource.getConnection()) {
        //
        //     // ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-user.sql"));
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    }
}
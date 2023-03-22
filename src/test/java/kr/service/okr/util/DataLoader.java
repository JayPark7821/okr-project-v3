package kr.service.okr.util;

import java.sql.Connection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.common.utils.ClassUtils;

@Transactional
@Profile("test")
@Component
public class DataLoader<T> {

	private final DataSource dataSource;

	@PersistenceContext
	EntityManager em;

	public DataLoader(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void loadData(List<T> entity, Class<?> clazz) {

		entity.stream().map(o -> ClassUtils.getSafeCastInstance(o, clazz)).forEach(obj -> {
			em.persist(obj.orElseThrow(() -> new RuntimeException("Class Casting Failed")));
		});

		em.flush();
		em.clear();
	}

	public void loadData(List<String> sqlScriptPath) {
		try (Connection conn = dataSource.getConnection()) {
			sqlScriptPath.forEach(path -> {
				ScriptUtils.executeSqlScript(conn, new ClassPathResource(path));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
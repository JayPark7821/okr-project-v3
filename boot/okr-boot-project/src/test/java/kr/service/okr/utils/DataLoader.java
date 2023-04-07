package kr.service.okr.utils;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Profile("test")
@Component
public class DataLoader<T> {

	private final DataSource dataSource;

	public DataLoader(DataSource dataSource) {
		this.dataSource = dataSource;
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
package kr.service.okr.utils;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

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
				// ScriptUtils.executeSqlScript(conn, new ClassPathResource(path));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
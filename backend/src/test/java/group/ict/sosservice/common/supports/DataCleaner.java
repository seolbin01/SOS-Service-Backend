package group.ict.sosservice.common.supports;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("test")
@Component
public class DataCleaner implements InitializingBean {

    private static final String TRUNCATE_QUERY = "SELECT Concat('TRUNCATE TABLE ', TABLE_NAME, ' RESTART IDENTITY;') "
        + "AS q FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = SCHEMA()";

    private static final String REFERENTIAL_FORMAT = "SET REFERENTIAL_INTEGRITY %s";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<String> truncateQueries;

    @Transactional
    public void clear() {
        entityManager.clear();
        truncate();
    }

    private void truncate() {
        jdbcTemplate.execute(String.format(REFERENTIAL_FORMAT, "FALSE"));
        truncateQueries.forEach(jdbcTemplate::execute);
        jdbcTemplate.execute(String.format(REFERENTIAL_FORMAT, "TRUE"));
    }

    @Override
    public void afterPropertiesSet() {
        truncateQueries = jdbcTemplate.queryForList(TRUNCATE_QUERY, String.class);
    }
}

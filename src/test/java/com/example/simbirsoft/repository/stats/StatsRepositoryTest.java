package com.example.simbirsoft.repository.stats;

import com.example.simbirsoft.repository.stats.StatsRepository;
import com.example.simbirsoft.repository.stats.StatsRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class StatsRepositoryTest {
    private static DataSource DATASOURCE;
    private static SimpleDateFormat FORMATTER;
    private final StatsRepository statsRepository;


    public StatsRepositoryTest() {
        this.statsRepository = new StatsRepositoryImpl(new JdbcTemplate(DATASOURCE));
    }

    @BeforeAll
    public static void init() {
        FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:s");
        DATASOURCE = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Test
    public void getRawApplicationStatsByMonths_Test() throws ParseException {
        var from = new Timestamp(FORMATTER.parse("2022-03-14 22:14:07").getTime());
        var to = new Timestamp(FORMATTER.parse("2022-04-14 23:39:07").getTime());
        var expected = statsRepository.getRawApplicationStatsByMonths(2, from, to);
        assertEquals(2, expected.size());
        assertEquals(3, expected.get("03.2022"));
        assertEquals(1, expected.get("04.2022"));
    }

    @Test
    public void getRawApplicationStatsByDays_Test() throws ParseException {
        var from = new Timestamp(FORMATTER.parse("2022-03-14 22:14:07").getTime());
        var to = new Timestamp(FORMATTER.parse("2022-04-14 23:39:07").getTime());
        var expected = statsRepository.getRawApplicationStatsByDays(2, from, to);
        assertEquals(3, expected.size());
        assertEquals(2, expected.get("14.03.2022"));
        assertEquals(1, expected.get("17.03.2022"));
        assertEquals(1, expected.get("14.04.2022"));
    }
}

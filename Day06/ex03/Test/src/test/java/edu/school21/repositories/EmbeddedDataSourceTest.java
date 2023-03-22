package edu.school21.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EmbeddedDataSourceTest {
    private EmbeddedDatabase database;

    @BeforeEach
    public void init(){
        database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .setName("Day06")
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
    }

    @Test
    public void testConnection(){
        try {
            assertNotNull(database.getConnection());
        } catch (SQLException e) {
            assertNotNull(null);
        }
    }
}

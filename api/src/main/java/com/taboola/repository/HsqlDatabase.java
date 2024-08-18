package com.taboola.repository;

import com.taboola.domain.Counter;
import com.taboola.domain.CounterRepository;
import com.taboola.domain.TimeBucket;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
@Repository
public class HsqlDatabase implements CounterRepository {

    private final Connection c;

    public HsqlDatabase() throws SQLException {
//        Database JDBC connection parameters: driver=org.hsqldb.jdbc.JDBCDriver url=jdbc:hsqldb:hsql://localhost/xdb username=sa password=
        this.c = DriverManager.getConnection(
                "jdbc:hsqldb:hsql://localhost/xdb", "sa", "");
    }

    @Override
    public List<Counter> getCounters(TimeBucket timeBucket) {
        try {
            String sql = "SELECT * FROM EventAggregation WHERE \"timeBucket\" = ?";
            PreparedStatement preparedStatement = this.c.prepareStatement(sql);
            preparedStatement.setTimestamp(1, timeBucket.getTimestamp());
            ResultSet result = preparedStatement.executeQuery();
            List<Counter> list = new ArrayList<>();
            while(result.next()){

                int eventId = result.getInt("eventId");
                int count = result.getInt("count");
                Counter c = new Counter(timeBucket.getTimeBucketString(), String.valueOf(eventId), count );
                list.add(c);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Counter getCountersOfEvent(TimeBucket timeBucket, String event) {
        try {
            String sql = "SELECT * FROM EventAggregation WHERE \"timeBucket\" = ? AND \"eventId\" = ?";
            PreparedStatement preparedStatement = this.c.prepareStatement(sql);
            preparedStatement.setTimestamp(1, timeBucket.getTimestamp());
            preparedStatement.setString(2, event);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                int eventId = result.getInt("eventId");
                int count = result.getInt("count");
                return new Counter(timeBucket.getTimeBucketString(), String.valueOf(eventId), count);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.taboola.spark;

import java.util.concurrent.TimeUnit;

import org.apache.spark.sql.functions;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.streaming.Trigger;
import org.apache.spark.sql.types.DataTypes;
import static org.apache.spark.sql.functions.*;

public class SparkApp {

    public static void main(String[] args) throws StreamingQueryException {
        SparkSession spark = SparkSession.builder().master("local[4]").getOrCreate();

        // generate events
        // each event has an id (eventId) and a timestamp
        // an eventId is a number between 0 an 99
        Dataset<Row> events = getEvents(spark);
        events.printSchema();

        // REPLACE THIS CODE
        // The spark stream continuously receives messages. Each message has 2 fields:
        // * timestamp
        // * event id (valid values: from 0 to 99)
        //
        // The spark stream should collect, in the database, for each time bucket and event id, a counter of all the messages received.
        // The time bucket has a granularity of 1 minute.
        Dataset<Row> counts = counts(events);
        writeToDatabase(counts.select(
                        col("eventId"),
                        col("count"),
                        date_trunc("minute", col("window.start")).as("timeBucket"))
                , "EventAggregation");

        // the stream will run forever
        spark.streams().awaitAnyTermination();
    }

    private static Dataset<Row> getEvents(SparkSession spark) {
        return spark
                .readStream()
                .format("rate")
                .option("rowsPerSecond", "10000")
                .load()
                .withColumn("eventId", functions.rand(System.currentTimeMillis()).multiply(functions.lit(100)).cast(DataTypes.LongType))
                .select("eventId", "timestamp");
    }

    private static Dataset<Row> counts(Dataset<Row> events) {
        return events
                .repartition(200)
                .withWatermark("timestamp", "1 minutes")
                .groupBy(
                        window(col("timestamp"), "1 minutes"),
                        col("eventId"))
                .count();
    }

    private static void writeToDatabase(Dataset<Row> events, String tableName) {
        String jdbcUrl = "jdbc:hsqldb:hsql://localhost/xdb";
        String jdbcDriver = "org.hsqldb.jdbc.JDBCDriver";
        String jdbcUser = "sa";
        String jdbcPassword = "";

        events.writeStream()
                .outputMode("complete")
                .foreachBatch((batchDF, batchId) -> {
                    batchDF.write()
                            .format("jdbc")
                            .option("url", jdbcUrl)
                            .option("driver", jdbcDriver)
                            .option("dbtable", tableName)
                            .option("user", jdbcUser)
                            .option("password", jdbcPassword)
                            .mode("append")
                            .save();
                })
                .trigger(Trigger.ProcessingTime(1, TimeUnit.MINUTES))
                .start();
    }

}

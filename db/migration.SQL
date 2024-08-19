create table EventAggregation(
    "timeBucket" timestamp,
    "count" bigint ,
    "eventId" tinyint,
    PRIMARY KEY ("timeBucket", "eventId")
)
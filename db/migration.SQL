create table EventAggregation(
    "timeBucket" timestamp,
    "count" bigint ,
    "eventId" bigint,
    PRIMARY KEY ("timeBucket", "eventId")
)
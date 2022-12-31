DROP KEYSPACE IF EXISTS cle;

CREATE KEYSPACE cle WITH REPLICATION = { 'class': 'SimpleStrategy', 'replication_factor': 1 };

USE cle;

CREATE TABLE db_sales (
    ID int PRIMARY KEY,
    PRODUCT_ID int,
    KEY_STORE text,
    WEEK date,
    UNITS_SOLD int,
    );

 INSERT INTO db_sales (id , key_store , product_id , units_sold , week ) VALUES ( 0,'dc4',6111977, 1408, '2017-03-21') ;
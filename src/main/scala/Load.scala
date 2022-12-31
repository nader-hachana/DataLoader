package com.cognira.DataLoad

import org.apache.spark.sql._
import org.apache.spark.SparkConf
import org.apache.spark.sql.cassandra._
import com.datastax.spark.connector._
import com.datastax.spark.connector.cql.CassandraConnector

object Load {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf(true)
        .setAppName("data-loader")
        .set("spark.cassandra.connection.host", "172.20.0.2")
        .set("spark.cassandra.connection.port", "9042")
        .setMaster("local[*]")
        //.set("spark.cassandra.auth.username", sys.env.getOrElse("DB_USER", "cassandra"))
        //.set("spark.cassandra.auth.password", sys.env.getOrElse("DB_PASS", "cassandra"))
         

    val spark =  SparkSession
        .builder()
        .config(conf)
        .getOrCreate()

    println("*** reading csv file ***")
    val db_sales = spark.read.format("csv")
        .option("header", "true")
        .option("delimiter", "|")
        .load("/data/db_sales.csv")

    db_sales.show()
    
    val df = db_sales.select("id","product_id","key_store","week","units_sold")
    
    println("*** writing to cassandra ***")
    df.write
        .format("org.apache.spark.sql.cassandra")
        .options(Map(
            "keyspace" -> "cle",
            "table" -> "db_sales"))
        .mode("append")
        .save()

    println("*** reading data from cassandra ***")
    val _data = spark.read
        .format("org.apache.spark.sql.cassandra")
        .options(Map(
            "keyspace" -> "cle",
            "table" -> "db_sales"))
        .load()
    _data.show()

    println("*** data schema***")
    _data.printSchema()

    _data.createOrReplaceTempView("db_sales")
    spark.sql("select sum(UNITS_SOLD) from db_sales").show()

  }
}
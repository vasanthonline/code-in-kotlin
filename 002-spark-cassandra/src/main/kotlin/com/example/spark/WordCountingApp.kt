package com.example.spark


import com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions
import com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow
import com.example.Constants.cassandraHost
import com.example.Constants.kafkaBrokerUrl
import com.example.Constants.messagesTopic
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.LogManager
import org.apache.spark.SparkConf
import org.apache.spark.streaming.Durations
import org.apache.spark.streaming.api.java.JavaDStream
import org.apache.spark.streaming.api.java.JavaInputDStream
import org.apache.spark.streaming.api.java.JavaPairDStream
import org.apache.spark.streaming.api.java.JavaStreamingContext
import org.apache.spark.streaming.kafka010.ConsumerStrategies
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies
import scala.Tuple2
import java.util.*


fun main(){
 WordCount().process()
}

class WordCount {
    private val logger = LogManager.getLogger(javaClass)

    fun process() {
        val kafkaParams: MutableMap<String, Any> = HashMap()
        kafkaParams["bootstrap.servers"] = kafkaBrokerUrl
        kafkaParams["key.deserializer"] = StringDeserializer::class.java
        kafkaParams["value.deserializer"] = StringDeserializer::class.java
        kafkaParams["group.id"] = "use_a_separate_group_id_for_each_stream"
        kafkaParams["auto.offset.reset"] = "latest"
        kafkaParams["enable.auto.commit"] = false

        val topics: Collection<String> = listOf(messagesTopic)

        val sparkConf = SparkConf()
        sparkConf.setMaster("local")
        sparkConf.setAppName("WordCountingApp")
        sparkConf.set("spark.cassandra.connection.host", cassandraHost)

        val streamingContext = JavaStreamingContext(sparkConf, Durations.seconds(1))

        val messages: JavaInputDStream<ConsumerRecord<String, String>> = KafkaUtils.createDirectStream<String, String>(
            streamingContext,
            LocationStrategies.PreferConsistent(),
            ConsumerStrategies.Subscribe(topics, kafkaParams)
        )

        val results: JavaPairDStream<String, String> =
            messages.mapToPair { record ->
                Tuple2(
                    record.key(),
                    record.value()
                )
            }

        val lines: JavaDStream<String> = results.map { tuple2 -> tuple2._2() }

        val words: JavaDStream<String> =  lines.flatMap { x: String -> x.split(" ").iterator() }

        val wordCounts: JavaPairDStream<String, Int> = words.mapToPair { s -> Tuple2(s, 1) }
            .reduceByKey { i1: Int, i2: Int -> i1 + i2 }

        wordCounts.foreachRDD { javaRdd ->
            val wordCountMap: Map<String, Int> = javaRdd.collectAsMap()
            for (key in wordCountMap.keys) {
                val wordList = listOf(Word(key, wordCountMap[key] ?: 0))
                val rdd = streamingContext.sparkContext()
                    .parallelize(wordList)
                javaFunctions(rdd).writerBuilder("vocabulary", "words", mapToRow(Word::class.java))
                    .saveToCassandra()
            }
        }

        streamingContext.start()
        streamingContext.awaitTermination()
    }
}
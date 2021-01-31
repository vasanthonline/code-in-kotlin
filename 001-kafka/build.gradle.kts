import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url=uri("https://packages.confluent.io/maven/")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.github.javafaker:javafaker:1.0.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.0")
    implementation("org.apache.kafka:kafka-clients:2.7.0")
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("org.slf4j:slf4j-log4j12:1.7.30")
    implementation("io.confluent:kafka-avro-serializer:6.0.1")
    implementation("org.apache.spark:spark-core_2.11:2.3.0")
    implementation("org.apache.spark:spark-sql_2.11:2.3.0")
    implementation("org.apache.spark:spark-streaming_2.11:2.3.0")
    implementation("org.apache.spark:spark-streaming-kafka-0-10_2.11:2.3.0")
    implementation("com.datastax.spark:spark-cassandra-connector_2.11:2.3.0")
    implementation("com.datastax.spark:spark-cassandra-connector-java_2.11:1.5.2")


    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
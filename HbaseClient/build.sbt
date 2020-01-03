name := "HbaseClient"
version := "0.1"
scalaVersion := "2.11.12"

val hbaseVersion = "2.2.0"

libraryDependencies ++= Seq(
  "org.apache.hbase" % "hbase" % hbaseVersion pomOnly(),
  "org.apache.hbase" % "hbase-client" % hbaseVersion,
  "org.apache.hbase" % "hbase-common" % hbaseVersion
)
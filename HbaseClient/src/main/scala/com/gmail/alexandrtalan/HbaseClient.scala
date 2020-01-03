package com.gmail.alexandrtalan

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.HBaseAdmin

object HbaseClient {

  def main(args: Array[String]): Unit = {

    buildConfiguration("hbase-client-config.xml") match {
      case Left(config) =>
        HBaseAdmin.available(config)
    }

  }

  private def getResource(path: String) = Option(HbaseClient.getClass.getClassLoader.getResource(path))

  private def buildConfiguration(path: String) = {
    getResource(path) match {
      case Some(resource) =>
        val config = HBaseConfiguration.create()
        config.addResource(resource)
        Left(config)
      case None =>
        Right(new IllegalStateException("Resource isn't found."))
    }
  }

}

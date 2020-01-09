package com.gmail.alexandrtalan

import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}

object HbaseClient {

  private val controller = TableName.valueOf("Controller")
  private val (info, pins, logs) = ("info", "pins", "logs")

  def main(args: Array[String]): Unit = buildConfiguration("hbase-client-config.xml") match {
    case Left(config) => HBaseAdmin.available(config)

      val connection = ConnectionFactory.createConnection(config)
      val admin = connection.getAdmin

      if (admin.isTableAvailable(controller)) {
        val controllerTable = connection.getTable(controller)

        if (!controllerTable.exists(new Get("raspberry_1".getBytes()))) {
          createData(controllerTable)
        }

      } else {
        createTable(admin)
      }
  }

  def createTable(admin: Admin): Unit = {
    val controllerTableDesc = TableDescriptorBuilder.newBuilder(controller)
      .setColumnFamily(ColumnFamilyDescriptorBuilder.of(info))
      .setColumnFamily(ColumnFamilyDescriptorBuilder.of(pins))
      .setColumnFamily(
        ColumnFamilyDescriptorBuilder.newBuilder(logs.getBytes)
          .setInMemory(true)
          .setMaxVersions(1)
          .build()
      )
      .build()

    admin.createTable(controllerTableDesc)
  }

  def createData(table: Table): Unit = {
    val row1 =
      Row(
        RowId("raspberry_1"),
        columns = Array(
          Column(ColumnFamily(info), ColumnQualifier("name"), "usa_raspberry".getBytes()),
          Column(ColumnFamily(info), ColumnQualifier("location"), "USA".getBytes())
        )
      )

    val row2 =
      Row(
        RowId("raspberry_2"),
        columns = Array(
          Column(ColumnFamily(info), ColumnQualifier("name"), "ua_raspberry".getBytes()),
          Column(ColumnFamily(info), ColumnQualifier("location"), "UA".getBytes())
        )
      )

    createRow(table, row1)
    createRow(table, row2)
  }

  def createRow(table: Table, row: Row): Unit = {
    val req = new Put(row.id.value.getBytes())
    row.columns.foreach { column =>
      req.addColumn(column.family.value.getBytes(), column.qualifier.value.getBytes(), column.value)
    }
    table.put(req)
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

  case class Row(id: RowId, columns: Array[Column])

  case class RowId(value: String) extends AnyVal

  case class Column(family: ColumnFamily, qualifier: ColumnQualifier, value: Array[Byte])

  case class ColumnFamily(value: String) extends AnyVal

  case class ColumnQualifier(value: String) extends AnyVal

}

package com.ibm.watson.developer_cloud.utils.DateJsonFormat

import java.text.{DateFormat, SimpleDateFormat}
import java.time.format.DateTimeFormatter
import java.util.Date

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import spray.json._

object DateJsonFormat extends RootJsonFormat[DateTime] {

  private val parserISO = ISODateTimeFormat.dateTime()

  override def write(obj: DateTime) : JsValue = JsString(parserISO.print(obj))

  override def read(json: JsValue) : DateTime = json match {
    case JsString(s) => parserISO.parseDateTime(s)
    case _ => throw new DeserializationException("Error info you want here ...")
  }
}
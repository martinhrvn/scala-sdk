package com.ibm.watson.developer_cloud.utils.DateJsonFormat

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import spray.json._

object DateJsonFormat extends RootJsonFormat[DateTime] {

  private[this] val parserISO = ISODateTimeFormat.dateTime()

  override def write(obj: DateTime) : JsValue = JsString(parserISO.print(obj))

  override def read(json: JsValue) : DateTime = json match {
    case JsString(s) => parserISO.parseDateTime(s)
    case _ => throw new DeserializationException("Error info you want here ...")
  }
}
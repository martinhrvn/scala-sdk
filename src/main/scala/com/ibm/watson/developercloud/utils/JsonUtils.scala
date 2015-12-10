package com.ibm.watson.developercloud.utils

import spray.json.{JsString, JsValue}

/**
  * Created by martinhrvn on 10/12/15.
  */
object JsonUtils {
  def addIfNotEmpty(value: String, name: String, map: Map[String, JsValue]) : Map[String, JsValue] = {
    Option(value) match {
      case Some(v) if v.nonEmpty => map + (name -> JsString(value))
      case _ => map
    }
  }
}

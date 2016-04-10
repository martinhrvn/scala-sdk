package com.ibm.watson.developer_cloud.service
import spray.json._

/**
  * Created by Martin Harvan (martin.harvan@sk.ibm.com) on 06/04/16.
  */
abstract class GenericModel {

  override def equals(o: Any) : Boolean = {
    Option(o) match {
      case Some(g: GenericModel) => this.toString.equals(g.toString)
      case _ => false
    }
  }

  override def hashCode : Int = {
    this.toString.hashCode
  }

  override def toString : String
}

package com.ibm.watson.developer_cloud.service
import spray.json._

/**
  * Created by martinhrvn on 06/04/16.
  */
abstract class GenericModel {

  override def equals(o: Any) : Boolean = {
    if(this == o) {
      return true
    }
    if (o == null || getClass() != o.getClass) {
      return false;
    }

    val other = o.asInstanceOf[GenericModel]

    this.toString.equals(other.toString);
  }

  override def hashCode : Int = {
    this.toString.hashCode
  }

  override def toString : String
}
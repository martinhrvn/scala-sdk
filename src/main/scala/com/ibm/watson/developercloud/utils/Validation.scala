package com.ibm.watson.developercloud.utils

/**
  * Created by martinhrvn on 10/12/15.
  */
object Validation {
  def validate[T](value: T, condition: T => Boolean, message: String) = {
    if(!condition(value)) {
      throw new IllegalArgumentException(message)
    }
  }

  def notEmpty(value : String, message: String) = {
    validate(value, {p : String =>
      Option(p) match {
        case Some(x) if !x.isEmpty => true
        case _ => false
      }
    }, message)
  }

  def notNull[T](value : T, message: String) = {
    validate(value, {p : T => p != null}, message)
  }

  def assertTrue(value: Boolean, message: String) = {
    validate(value, {_ :Boolean => value}, message)
  }
}

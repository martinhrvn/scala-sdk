package com.ibm.watson.developercloud.utils

import java.io.File

import com.ibm.watson.developercloud.visual_insights.v1.Summary

import scala.concurrent.Future

/**
  * Object for validation purposes
  */
object Validation {
  /**
    * Validates if file is not null and exists
    * @param file file to validate
    * @param message message to use for exception
    */
  def fileExists(file: File, message: String) = {
    validate(file, {x : File => x != null && x.exists() }, message)
  }

  /**
    * General purpose validation function. Uses provided function to validate. If the value does not match validation rules an IllegaArgumentException is thrown.
    * @param value value to validate
    * @param condition condition for validation
    * @param message message to use for exception
    * @tparam T type of value
    */
  def validate[T](value: T, condition: T => Boolean, message: String) = {
    if(!condition(value)) {
      throw new IllegalArgumentException(message)
    }
  }

  /**
    * Checks if provided string is not null and nonEmpty
    * @param value String to validate
    * @param message message to use for exception
    */
  def notEmpty(value : String, message: String) = {
    validate(value, {p : String =>
      Option(p) match {
        case Some(x) if !x.isEmpty => true
        case _ => false
      }
    }, message)
  }

  /**
    * Simple not Null Check
    * @param value Value to check
    * @param message message to use for exception
    * @tparam T type of value to validate
    */
  def notNull[T](value : T, message: String) = {
    validate(value, {p : T => p != null}, message)
  }

  /**
    * Throws exception if the value is false
    * @param value throws exception if false
    * @param message message to use for exception
    */
  def assertTrue(value: Boolean, message: String) = {
    validate(value, {_ :Boolean => value}, message)
  }
}

// Copyright (C) 2015 IBM Corp. All Rights Reserved.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.ibm.watson.developer_cloud.utils

import java.io.File

/**
  * Object for validation purposes
  */
object Validation {

  val messageNotNull = "%s cannot be null"
  val messageNotEmpty = "%s cannot be null or empty"
  /**
    * Validates if file is not null and exists
    * @param file file to validate
    * @param message message to use for exception
    */
  def fileExists(file: File, message: String ) : Unit = {
    validate(file, {x : File => Option(x) match {
      case Some(x) if x.exists() => true
      case _ => false
    }}, message)
  }

  /**
    * General purpose validation function. Uses provided function to validate. If the value does not match validation
    * rules an IllegaArgumentException is thrown.
    * @param value value to validate
    * @param condition condition for validation
    * @param message message to use for exception
    * @tparam T type of value
    */
  def validate[T](value: T, condition: T => Boolean, message: String) : Unit = {
    if(!condition(value)) {
      throw new IllegalArgumentException(message)
    }
  }

  /**
    * Checks if provided string is not null and nonEmpty
    * @param value String to validate
    * @param message message to use for exception
    */
  def notEmpty(value : String, message: String) : Unit = {
    notEmpty(Option(value), message)
  }

  /**
    *
    */
  def notEmpty[T](value: List[T], message: String) : Unit = {
    validate(Option(value), {p: Option[List[T]] =>
      p match {
        case Some(x) if x.nonEmpty => true
        case _ => false
      }
    }, message)
  }

  /**
    * Validate Option[String] for not empty
    * @param value either Some(string) or None
    * @param message message to use for exception
    */
  def notEmpty(value: Option[String], message: String) : Unit = {
    validate(value, {p: Option[String] =>
      p match {
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
  def notNull[T](value : T, message: String) : Unit =  {
    validate(value, {x : T => notNull(x)}, message)
  }

  /**
    * Throws exception if the value is false
    * @param value throws exception if false
    * @param message message to use for exception
    */
  def assertTrue(value: Boolean, message: String) : Unit = {
    validate(value, {_ :Boolean => value}, message)
  }

  def notNull(value: Any) : Boolean = {
    Option(value) match {
      case Some(v) => true
      case _ => false
    }
  }
}

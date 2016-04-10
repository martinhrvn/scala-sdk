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

import org.scalatest.FlatSpec

/**
  * Created by Martin Harvan (martin.harvan@sk.ibm.com) on 18/12/15.
  */
class ValidationSpec extends FlatSpec {
  "Validation.notEmpty" should "throw exception for empty string" in {
    val string = ""
    intercept[IllegalArgumentException] {
      Validation.notEmpty(string, "String cannot be empty")
    }
  }

  it should "pass for not empty string" in {
    val string = "Test"
    Validation.notEmpty(string, "String cannot be empty")
  }

  it should "throw exeption for null string" in {
    val string = null : String
    intercept[IllegalArgumentException] {
      Validation.notEmpty(string, "string cannot be empty")
    }
  }

  it should "throw exception for None" in {
    val string : Option[String] = None
    intercept[IllegalArgumentException] {
      Validation.notEmpty(string, "String cannot be empty")
    }
  }

  it should "pass for Some(string)" in {
    val string : Option[String] = Some("text")
    Validation.notEmpty(string, "string cannot be empty")
  }

  "Validation.notNull" should "throw excpetion for null value" in {
    val string = null
    intercept[IllegalArgumentException] {
      Validation.notNull(string, "Value cannot be null")
    }
  }
}

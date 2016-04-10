// Copyright (C) 2016 IBM Corp. All Rights Reserved.
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
package com.ibm.watson.developer_cloud.concept_expansion.v1.model

import com.ibm.watson.developer_cloud.service.GenericModel

/**
  * Created by Martin Harvan (martin.harvan@sk.ibm.com) on 10/04/16.
  */
case class Job(id: String, status: Status.Value) extends GenericModel

object  Status extends Enumeration{
  val AwaitingWork = Value("Awaiting Work", "A")
  val Done = Value("Done", "D")
  val Failed = Value("Failed", "F")
  val InFlight = Value("In Flight", "G")
  val Retrieved = Value("Retrieved", "R")
  class MyVal(name: String, val id : String) extends Val(nextId, name)
  protected final def Value(name: String, id : String): MyVal = new MyVal(name,id)
}
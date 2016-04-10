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
package com.ibm.watson.developer_cloud.concept_expansion.v1

import com.ibm.watson.developer_cloud.concept_expansion.v1.model.{Concept, Dataset, Job, Status}
import com.ibm.watson.developer_cloud.service.{ConfigFactory, VCAPConfigFactory, WatsonService}
import com.ibm.watson.developer_cloud.utils.Validation

import scala.concurrent.Future
import spray.json._
import spray.client.pipelining._
import spray.http._
import spray.json.DefaultJsonProtocol._

import spray.httpx.SprayJsonSupport._
import ConceptExpansion._
/**
  * Created by Martin Harvan on 10/04/16.
  */
class ConceptExpansion(override val configFactory: ConfigFactory = new VCAPConfigFactory()) extends WatsonService(configFactory) {
  val dataset = Dataset.MTSamples

  def serviceType: String = "concept_expansion"

  def createJob(seeds: List[String]) : Future[Job] = {
    createJob(None, seeds)
  }

  /**
    * Creates a Job
    * @param label A conceptual classification of the seed terms.
    * @param seeds List of terms to be used as seeds
    * @return the Job
    */
  def createJob(label: Option[String], seeds: List[String]) : Future[Job] = {
    Validation.notEmpty(seeds, "Seeds cannot be null or empty")
    Validation.notNull(dataset, "dataset cannot be null")

    val properties : Map[String, JsValue] = label.map(x => "label" -> JsString(x)).toMap ++
      Map("dataset" -> JsString(dataset.id), "seeds" -> seeds.toJson)

    val data = JsObject(properties)
    val request = Post(Upload, data)
    val response = send(request)
    response.map(unmarshal[Job])
  }

  def getJobResult(job: Job) : Future[List[Concept]] = {
    Validation.notNull(job, "job cannot be null")

    val payload = JsObject("jobid"-> JsString(job.id))

    val request = Put(Result, payload)
    val response = send(request)
    response.map(unmarshal[List[Concept]])
  }

  def getJobStatus(job: Job) : Future[Status.Value] = {
    Validation.notNull(job, "Job cannot be empty")

    val request = Get(Uri(ConceptExpansion.Status).withQuery("jobid" -> job.id))
    val response = send(request)

    response.map(unmarshal[Status.Value])
  }

}

object ConceptExpansion {
  val Upload = "/v1/upload"
  val Status = "/v1/status"
  val Result = "/v1/result"
}

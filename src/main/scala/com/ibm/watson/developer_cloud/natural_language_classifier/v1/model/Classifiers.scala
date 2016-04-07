package com.ibm.watson.developer_cloud.natural_language_classifier.v1.model

import java.util.Date

import com.ibm.watson.developer_cloud.service.GenericModel
import org.joda.time.DateTime

/**
  * Created by martinhrvn on 20/03/16.
  */
//TODO: Use enum for status
case class Classifier(created: DateTime,
                      id : String,
                      language: String,
                      name: String,
                      status: Option[String],
                      statusDescription: Option[String] ,
                      url: String
                       )extends GenericModel
case class Classifiers(classifiers: List[Classifier])
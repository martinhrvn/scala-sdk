package com.ibm.watson.developer_cloud.personality_insights.v2.model

import com.ibm.watson.developer_cloud.service.GenericModel

/**
  * Created by Martin Harvan (martin.harvan@sk.ibm.com) on 20/03/16.
  */
case class Trait(category: String, children: List[Trait], id: String, name: String, percentage: Double,
                 rawSamplingError: Double, rawScore: Double, samplingError: Double)extends GenericModel

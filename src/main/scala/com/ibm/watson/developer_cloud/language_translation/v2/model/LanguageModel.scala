package com.ibm.watson.developer_cloud.language_translation.v2.model

import com.ibm.watson.developer_cloud.service.GenericModel

/**
  * Created by Martin Harvan (martin.harvan@sk.ibm.com) on 20/03/16.
  */
case class LanguageModel(var modelId : String, var source: String, var target: String, baseModelId: String,
                         var domain: String, var customizable: Boolean, var defaultModel: Boolean, var owner: String,
                         var status: String, var name: String)
  extends GenericModel

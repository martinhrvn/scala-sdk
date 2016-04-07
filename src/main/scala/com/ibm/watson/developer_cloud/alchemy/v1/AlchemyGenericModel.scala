package com.ibm.watson.developer_cloud.alchemy.v1

import com.ibm.watson.developer_cloud.service.GenericModel


abstract class AlchemyGenericModel extends GenericModel {
  def totalTransactions: Integer
}
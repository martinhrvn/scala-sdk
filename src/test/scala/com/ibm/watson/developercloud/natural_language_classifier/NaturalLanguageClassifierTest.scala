package com.ibm.watson.developercloud.natural_language_classifier

import akka.actor.ActorSystem
import com.ibm.watson.developercloud.natural_language_classifier.v1.{NaturalLanguageClassifier, TrainingData}
import org.scalatest.junit._
import com.typesafe.scalalogging._
import org.junit._
import scala.util.{Success, Failure}
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.concurrent._
import scala.concurrent.duration._

/**
 * @author Martin Harvan (martin.harvan@sk.ibm.com)
 */
@RunWith(classOf[JUnitRunner])
class NaturalLanguageClassifierTest extends FunSuite with ScalaFutures with Matchers with LazyLogging  {
  implicit val system = ActorSystem()
  import system.dispatcher


}

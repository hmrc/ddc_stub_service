/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.ddc_stub_service.controllers

import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.ddc_stub_service.utils._
import play.api.mvc._
import play.api.Environment
import javax.inject.{Inject, Singleton}
import scala.io.Source

@Singleton()
class CaseManagementSystemController @Inject()(environment: Environment, cc: ControllerComponents)
  extends BackendController(cc) {

  private val basePath = "conf/resources/data"
  private val casePath = "/debt/"

  private val listHelper: ListHelper = new ListHelper()

  def getCaseDetails(uniqueItemReference: String, duties: Option[String]) = Action { request =>
    val testOnlyResponseCode: Option[String] = request.headers.get("testOnlyResponseCode")
    if(testOnlyResponseCode.isDefined) {
      Results.Status(testOnlyResponseCode.map(_.toInt).getOrElse(500))
    } else {
      environment.getExistingFile(basePath + casePath + uniqueItemReference + ".json") match {
        case Some(file) => Ok(Source.fromFile(file).mkString)
        case _ => NotFound("file not found")
      }
    }
  }

  def getList() = Action {
    Ok(listHelper.getList(basePath + casePath))
  }
}
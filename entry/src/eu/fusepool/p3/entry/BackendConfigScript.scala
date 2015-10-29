package eu.fusepool.p3.entry

import org.apache.clerezza.rdf.utils.GraphNode
import org.osgi.service.component.annotations._
import javax.ws.rs._
import javax.ws.rs.core._
import org.apache.clerezza.rdf.scala.utils._
import org.apache.clerezza.commons.rdf._
import org.apache.clerezza.rdf.ontologies._
import eu.fusepool.p3.vocab.FP3
import Preamble._
import java.io.InputStreamReader

/**
 * This JAX-RS resource returns a js-script to configure the backend, i.e. to create the various registries.
 *
 * If an environment vraiable `P3-BACKEND-CONFIG-JS`is specified, the script at that path is used.
 */
@Component(service = Array(classOf[Object]), property = Array("javax.ws.rs=true"))
@Path("js/backend-config.js")
class BackendconfigScript {

  @GET
  def getScript = {
    //TODO check env variable
    new InputStreamReader(classOf[BackendconfigScript].getResourceAsStream("default-backend-config.js"))
  }
}
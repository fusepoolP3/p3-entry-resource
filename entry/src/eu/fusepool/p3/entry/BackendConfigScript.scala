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
import java.io.Reader
import java.io.File
import java.io.FileReader

/**
 * This JAX-RS resource returns a js-script to configure the backend, i.e. to create the various registries.
 *
 * If a script at ~/.fusepool-p3/backend-config.js exists it is used, otherwise
 * if a script at /etc/fusepool-p3/backend-config.js exists it is used, otherwise
 * a default script using services from sandbox.fusepool.info is used.
 */
@Component(service = Array(classOf[Object]), property = Array("javax.ws.rs=true"))
@Path("js/backend-config.js")
class BackendconfigScript {

  val homeDir = new File(System.getProperty("user.home"));
  
  def userFile = new File(homeDir, ".fusepool-p3/backend-config.js");
  
  def systemFile = new File("/etc/fusepool-p3/backend-config.js");
  
  
  @GET
  def getScript : Reader = {
    if (userFile.exists()) {
      return new FileReader(userFile); 
    }
    if (systemFile.exists()) {
      return new FileReader(systemFile); 
    }
    return new InputStreamReader(classOf[BackendconfigScript].getResourceAsStream("default-backend-config.js"))
  }
}
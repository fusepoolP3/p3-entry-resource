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
import javax.ws.rs.core.Response.Status
import scala.collection.mutable._
import java.io.ObjectInputStream
import java.io.File
import java.io.ObjectOutputStream
import java.io.FileOutputStream
import java.io.ObjectStreamClass
import java.util.Date



  
@SerialVersionUID(1)
class Application(
  val iri: IRI,
  val label: String,
  val description: String
 ) extends Serializable {}

@SerialVersionUID(2)
class PlatformConfig extends Serializable{    
  var locked = false;
  var tr: IRI = null;
  var irldpc: IRI = null;
  var tfr: IRI = null;
  var dcr: IRI = null;
  var ldpRoot: IRI = null;
  var sparqlEndpoint: IRI = null;
  var date: Date = null;
  val dashboards: Set[IRI] = scala.collection.mutable.HashSet[IRI]();
  val applications: Set[Application] = scala.collection.mutable.HashSet[Application]();
}


@Component(service = Array(classOf[Object]), property = Array("javax.ws.rs=true"))
@Path("")
class RootResource {

  
  var platformConfig : PlatformConfig = null;
  
  private val serializedFileName = "PlatformConfig.ser"
  @Activate
  def activate() {
    val serializedPN = ConfigDirProvider.configDir.getSubPath(serializedFileName);
    if (serializedPN.exists()) {
      //all classes in serialization are reachable with this
      val cl = getClass.getClassLoader
      val ois = new ObjectInputStream(serializedPN.getInputStream) {
        override def resolveClass(desc: ObjectStreamClass) : Class[_] = {
            try {
              return cl.loadClass(desc.getName);
            } catch  {
              case e: Exception => {
                 return super.resolveClass(desc);
              }
            }
          }
      }
      try {
        platformConfig = ois.readObject.asInstanceOf[PlatformConfig]
      } catch {
        case e: Exception => {
          System.err.println("WARNING! Could not deserialize "+serializedFileName+". Starting with blank config");
          platformConfig = new PlatformConfig
        }
      }
      ois.close
    } else {
      platformConfig = new PlatformConfig;
    }
  }
  
  
  @GET
  def platformResource(@Context uriInfo: UriInfo) =
    {
      val resource = uriInfo.getRequestUri().toString().iri;
      val g = new EzGraph() {
        (
          resource.a(FP3.Platform) -- DC.title --> ("Fusepool P3 Instance " + uriInfo.getRequestUri.getHost).lang("en"))
        (
          if (platformConfig.date != null) {
            resource -- DCTERMS.created --> platformConfig.date
          })
        (
          if (platformConfig.sparqlEndpoint != null) {
            resource -- FP3.sparqlEndpoint --> platformConfig.sparqlEndpoint
          })
        (
          if (platformConfig.tr != null) {
            resource -- FP3.transformerRegistry --> platformConfig.tr
          })
        (
          if (platformConfig.irldpc != null) {
            resource -- FP3.userInteractionRequestRegistry --> platformConfig.irldpc
          })
        (
          if (platformConfig.tfr != null) {
            resource -- FP3.transformerFactoryRegistry --> platformConfig.tfr
          })
        (
          if (platformConfig.ldpRoot != null) {
            resource -- FP3.ldpRoot --> platformConfig.ldpRoot
          })
        (
          if (platformConfig.dcr != null) {
            resource -- "http://vocab.fusepool.info/fp3#dashboardConfigRegistry".iri --> platformConfig.dcr
          })
        (
          platformConfig.dashboards.foreach {
            dashboard => resource -- FP3.dashboard --> dashboard
          })
        (
          platformConfig.applications.foreach {
            application => {
              resource -- FP3.app --> ( 
                 bnode -- DCTERMS.subject --> application.iri
                       -- RDFS.label --> application.label
                       -- DCTERMS.description --> application.description
              )
            }
          })

      };
      val node = new GraphNode(resource, g)
      Response.ok(node).header("Vary", "Accept").build();
    }

  @GET
  @Path("fullyConfigured")
  def isFullyConfigured() = {
    Response.ok(if ((platformConfig.tr != null) && (platformConfig.tfr != null) 
        && (platformConfig.irldpc != null) && (platformConfig.dcr != null) && (platformConfig.ldpRoot != null)) {
      "true"
    } else {
      "false"
    }).header("Cache-Control", "private, max-age=0, no-cache").build();
  }

  @POST
  @Path("lock")
  def lock() = {
    synchronized {
      if (platformConfig.locked) {
        throw new WebApplicationException("The platform-comfiguration is locked", Status.FORBIDDEN)
      }
      platformConfig.date = new Date()
      val file = new File(ConfigDirProvider.userConfigDir, serializedFileName);
      val oos = new ObjectOutputStream(new FileOutputStream(file))
      oos.writeObject(platformConfig)
      oos.close
      platformConfig.locked = true;
    }
  }
  
  @POST
  @Path("tr")
  def setTr(tr: String) = {
    if (platformConfig.locked) {
      throw new WebApplicationException("The platform-comfiguration is locked", Status.FORBIDDEN)
    }
    if (platformConfig.tr != null) {
      //ignoring double called while not locked
    } else {
      platformConfig.tr = tr.iri
    }
  }

  @POST
  @Path("tfr")
  def setTfr(tfr: String) = {
    if (platformConfig.locked) {
      throw new WebApplicationException("The platform-comfiguration is locked", Status.FORBIDDEN)
    }
    if (platformConfig.tfr != null) {
      //ignoring double called while not locked
    } else {
      platformConfig.tfr = tfr.iri
    }
  }

  @POST
  @Path("irldpc")
  def setIrldpc(irldpc: String) = {
    if (platformConfig.locked) {
      throw new WebApplicationException("The platform-comfiguration is locked", Status.FORBIDDEN)
    }
    if (platformConfig.irldpc != null) {
      //ignoring double called while not locked
    } else {
      platformConfig.irldpc = irldpc.iri
    }
  }

  @POST
  @Path("dcr")
  def setDcr(dcr: String) = {
    if (platformConfig.locked) {
      throw new WebApplicationException("The platform-comfiguration is locked", Status.FORBIDDEN)
    }
    if (platformConfig.dcr != null) {
      //ignoring double called while not locked
    } else {
      platformConfig.dcr = dcr.iri
    }
  }

  @POST
  @Path("registerLdpRoot")
  def setLdpRoot(ldpRoot: String) = {
    if (platformConfig.locked) {
      throw new WebApplicationException("The platform-comfiguration is locked", Status.FORBIDDEN)
    }
    if (platformConfig.ldpRoot != null) {
      //ignoring double called while not locked
    } else {
      platformConfig.ldpRoot = ldpRoot.iri
    }
  }
  
  @POST
  @Path("registerSparql")
  def setSparqlEndpoint(sparqlEndpoint: String) = {
    if (platformConfig.locked) {
      throw new WebApplicationException("The platform-comfiguration is locked", Status.FORBIDDEN)
    }
    if (platformConfig.sparqlEndpoint != null) {
      //ignoring double called while not locked
    } else {
      platformConfig.sparqlEndpoint = sparqlEndpoint.iri
    }
  }

  @POST
  @Path("registerDashboard")
  def addDashboard(dashboard: String) = {
    if (platformConfig.locked) {
      throw new WebApplicationException("The platform-comfiguration is locked", Status.FORBIDDEN)
    }
    platformConfig.dashboards += dashboard.iri
    println("dashboard added: "+dashboard);
  }
  
  @POST
  @Path("registerApplication")
  def addApplication(@FormParam("iri") application: String, @FormParam("label") _label: String, 
      @FormParam("description") _description: String) = {
    if (platformConfig.locked) {
      throw new WebApplicationException("The platform-comfiguration is locked", Status.FORBIDDEN)
    }
    platformConfig.applications += new Application(
        application.iri,
        _label,
        _description);
    println("app added: "+application);
  }
}
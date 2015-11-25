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

@Component(service = Array(classOf[Object]), property = Array("javax.ws.rs=true"))
@Path("")
class RootResource {

  trait Application {
    def iri: IRI;
    def label: String;
    def description: String;
  }
  
  var locked = false;
  
  var tr: IRI = null;
  var irldpc: IRI = null;
  var tfr: IRI = null;
  var dcr: IRI = null;
  var ldpRoot: IRI = null;
  var sparqlEndpoint: IRI = null;
  val dashboards: Set[IRI] = scala.collection.mutable.HashSet[IRI]();
  val applications: Set[Application] = scala.collection.mutable.HashSet[Application]();

  @GET
  def platformResource(@Context uriInfo: UriInfo) =
    {
      val resource = uriInfo.getRequestUri().toString().iri;
      val g = new EzGraph() {
        (
          resource.a(FP3.Platform) -- DC.title --> ("Fusepool P3 Instance " + uriInfo.getRequestUri.getHost).lang("en"))
        (
          if (sparqlEndpoint != null) {
            resource -- FP3.sparqlEndpoint --> sparqlEndpoint
          })
        (
          if (tr != null) {
            resource -- FP3.transformerRegistry --> tr
          })
        (
          if (irldpc != null) {
            resource -- FP3.userInteractionRequestRegistry --> irldpc
          })
        (
          if (tfr != null) {
            resource -- FP3.transformerFactoryRegistry --> tfr
          })
        (
          if (ldpRoot != null) {
            resource -- FP3.ldpRoot --> ldpRoot
          })
        (
          if (dcr != null) {
            resource -- "http://vocab.fusepool.info/fp3#dashboardConfigRegistry".iri --> dcr
          })
        (
          dashboards.foreach {
            dashboard => resource -- FP3.dashboard --> dashboard
          })
        (
          applications.foreach {
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
    Response.ok(if ((this.tr != null) && (tfr != null) && (irldpc != null) && (dcr != null) && (ldpRoot != null)) {
      "true"
    } else {
      "false"
    }).header("Cache-Control", "private, max-age=0, no-cache").build();
  }

  @POST
  @Path("lock")
  def lock() = {
    if (locked) {
      throw new WebApplicationException("The platform-comfiguration is locked", Status.FORBIDDEN)
    }
    locked = true;
  }
  
  @POST
  @Path("tr")
  def setTr(tr: String) = {
    if (locked) {
      throw new WebApplicationException("The platform-comfiguration is locked", Status.FORBIDDEN)
    }
    if (this.tr != null) {
      //ignoring double called while not locked
    } else {
      this.tr = tr.iri
    }
  }

  @POST
  @Path("tfr")
  def setTfr(tfr: String) = {
    if (locked) {
      throw new WebApplicationException("The platform-comfiguration is locked", Status.FORBIDDEN)
    }
    if (this.tfr != null) {
      //ignoring double called while not locked
    } else {
      this.tfr = tfr.iri
    }
  }

  @POST
  @Path("irldpc")
  def setIrldpc(irldpc: String) = {
    if (locked) {
      throw new WebApplicationException("The platform-comfiguration is locked", Status.FORBIDDEN)
    }
    if (this.irldpc != null) {
      //ignoring double called while not locked
    } else {
      this.irldpc = irldpc.iri
    }
  }

  @POST
  @Path("dcr")
  def setDcr(dcr: String) = {
    if (locked) {
      throw new WebApplicationException("The platform-comfiguration is locked", Status.FORBIDDEN)
    }
    if (this.dcr != null) {
      //ignoring double called while not locked
    } else {
      this.dcr = dcr.iri
    }
  }

  @POST
  @Path("registerLdpRoot")
  def setLdpRoot(ldpRoot: String) = {
    if (locked) {
      throw new WebApplicationException("The platform-comfiguration is locked", Status.FORBIDDEN)
    }
    if (this.ldpRoot != null) {
      //ignoring double called while not locked
    } else {
      this.ldpRoot = ldpRoot.iri
    }
  }
  
  @POST
  @Path("registerSparql")
  def setSparqlEndpoint(sparqlEndpoint: String) = {
    if (locked) {
      throw new WebApplicationException("The platform-comfiguration is locked", Status.FORBIDDEN)
    }
    if (this.sparqlEndpoint != null) {
      //ignoring double called while not locked
    } else {
      this.sparqlEndpoint = sparqlEndpoint.iri
    }
  }

  @POST
  @Path("registerDashboard")
  def addDashboard(dashboard: String) = {
    if (locked) {
      throw new WebApplicationException("The platform-comfiguration is locked", Status.FORBIDDEN)
    }
    this.dashboards += dashboard.iri
    println("dashboard added: "+dashboard);
  }
  
  @POST
  @Path("registerApplication")
  def addApplication(@FormParam("iri") application: String, @FormParam("label") _label: String, 
      @FormParam("description") _description: String) = {
    if (locked) {
      throw new WebApplicationException("The platform-comfiguration is locked", Status.FORBIDDEN)
    }
    this.applications += new Application {
      def iri = application.iri;
      def label = _label;
      def description = _description;
    }
    println("app added: "+application);
  }
}
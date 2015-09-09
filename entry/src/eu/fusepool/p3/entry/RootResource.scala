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

@Component(service = Array(classOf[Object]), property = Array("javax.ws.rs=true"))
@Path("")
class RootResource {

  var tr: IRI = null;
  var irldpc: IRI = null;
  var tfr: IRI = null;
  var dcr: IRI = null;
  var sparqlEndpoint: IRI = null;

  @GET
  def hello(@Context uriInfo: UriInfo) =
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
            resource -- FP3.irLDPC --> irldpc
          })
        (
          if (tfr != null) {
            resource -- FP3.transformerFactoryRegistry --> tfr
          })
        (
          if (dcr != null) {
            resource -- "http://vocab.fusepool.info/fp3#dashboardConfigRegistry".iri --> dcr
          })

      };
      new GraphNode(resource, g)
    }

  @GET
  @Path("fullyConfigured")
  def isFullyConfigured() = {
    if ((this.tr != null) && (tfr != null) && (irldpc != null) && (dcr != null)) {
      "true"
    } else {
      "false"
    }
  }

  @POST
  @Path("tr")
  def setTr(tr: String) = {
    if (this.tr != null) {
      throw new WebApplicationException("Field may be set only once", Status.FORBIDDEN)
    }
    this.tr = tr.iri
  }

  @POST
  @Path("tfr")
  def setTfr(tfr: String) = {
    if (this.tfr != null) {
      throw new WebApplicationException("Field may be set only once", Status.FORBIDDEN)
    }
    this.tfr = tfr.iri
  }

  @POST
  @Path("irldpc")
  def setIrldpc(irldpc: String) = {
    if (this.irldpc != null) {
      throw new WebApplicationException("Field may be set only once", Status.FORBIDDEN)
    }
    this.irldpc = irldpc.iri
  }

  @POST
  @Path("dcr")
  def setDcr(dcr: String) = {
    if (this.dcr != null) {
      throw new WebApplicationException("Field may be set only once", Status.FORBIDDEN)
    }
    this.dcr = dcr.iri
  }

  @POST
  @Path("registerSparql")
  def setSparqlEndpoint(sparqlEndpoint: String) = {
    if (this.sparqlEndpoint != null) {
      throw new WebApplicationException("Field may be set only once", Status.FORBIDDEN)
    }
    this.sparqlEndpoint = sparqlEndpoint.iri
  }
}
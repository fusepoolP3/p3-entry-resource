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

@Component(service = Array(classOf[Object]), property = Array("javax.ws.rs=true"))
@Path("")
class RootResource {

  @GET
  def hello(@Context uriInfo: UriInfo) =
    {
      val resource = uriInfo.getRequestUri().toString().iri;
      val g = new EzGraph() 
      {
        (
         resource.a(FP3.Platform) -- DC.title --> ("Fusepool P3 Instance "+uriInfo.getRequestUri.getHost).lang("en")
          -- FP3.sparqlEndoint  --> ("http://example.org/sparql").iri
         )
      };
      new GraphNode(resource,g)
    }
}
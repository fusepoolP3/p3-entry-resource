package eu.fusepool.p3.entry

import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo
import org.apache.clerezza.commons.rdf.Graph
import org.apache.clerezza.commons.rdf.IRI
import org.apache.clerezza.commons.rdf.impl.utils.simple.SimpleGraph
import org.apache.clerezza.rdf.ontologies.DC
import org.apache.clerezza.rdf.ontologies.RDFS
import org.apache.clerezza.rdf.scala.utils.EzGraph
import org.apache.clerezza.rdf.utils.GraphNode
import org.apache.clerezza.commons.rdf._
import org.apache.clerezza.rdf.utils.graphnodeprovider.GraphNodeProvider
import org.apache.clerezza.platform.typehandlerspace.SupportedTypes
import org.osgi.service.component.ComponentContext
import org.osgi.service.component.annotations.Activate
import org.osgi.service.component.annotations._
import javax.ws.rs.GET
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response.Status

@Component(service = Array(classOf[Object]), property = Array("org.apache.clerezza.platform.typehandler=true"))
@SupportedTypes(types = { Array("http://www.w3.org/2000/01/rdf-schema#Resource") }, prioritize = false)
class P3TypeHandler {

  var gnp: GraphNodeProvider = null
  
  @Reference(
    cardinality = ReferenceCardinality.MANDATORY,
    policy = ReferencePolicy.STATIC,
    unbind = "unsetGraphNodeProvider"
  )
  def setGraphNodeProvider(gnp: GraphNodeProvider) {
    this.gnp = gnp;
  }

  def unsetGraphNodeProvider(gnp: GraphNodeProvider) {
    this.gnp = null;
  }
  @GET
  def get(@Context uriInfo: UriInfo) = {
    val iri : IRI = new IRI(uriInfo.getAbsolutePath().toString());
    val result = gnp.getLocal(iri)
    result
  }
  
}

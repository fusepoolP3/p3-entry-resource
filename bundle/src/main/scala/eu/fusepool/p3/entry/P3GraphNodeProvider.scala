package eu.fusepool.p3.entry

import java.io.File
import java.io.FileInputStream
import org.apache.clerezza.commons.rdf.Graph
import org.apache.clerezza.commons.rdf.IRI
import org.apache.clerezza.commons.rdf.Literal
import org.apache.clerezza.commons.rdf.impl.sparql.SparqlGraph
import org.apache.clerezza.commons.rdf.impl.utils.simple.SimpleGraph
import org.apache.clerezza.rdf.ontologies.DC
import org.apache.clerezza.rdf.ontologies.RDF
import org.apache.clerezza.rdf.ontologies.RDFS
import org.apache.clerezza.rdf.scala.utils.EzGraph
import org.apache.clerezza.rdf.scala.utils.Preamble
import org.apache.clerezza.rdf.utils.GraphNode
import org.apache.clerezza.rdf.utils.UriMutatingGraph
import org.apache.clerezza.rdf.utils.graphnodeprovider.GraphNodeProvider
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat
import org.apache.clerezza.rdf.core.serializedform.Parser
import org.osgi.service.component.ComponentContext
import org.osgi.service.component.annotations.Activate
import org.osgi.service.component.annotations.Component
import org.osgi.service.component.annotations.Reference
import org.osgi.service.component.annotations.ReferenceCardinality
import org.osgi.service.component.annotations.ReferencePolicy


@Component(service = Array(classOf[GraphNodeProvider]), immediate = true)
class P3GraphNodeProvider extends GraphNodeProvider {

  var g: Graph = new EzGraph() {
      val iri = "http://localhost:8080/foo".iri;
      (
        iri.a(RDFS.Resource) -- DC.title --> ("Hello " + iri).lang("en"))
    };
    
  var parser: Parser = null

  
  @Reference(
    cardinality = ReferenceCardinality.MANDATORY,
    policy = ReferencePolicy.STATIC,
    unbind = "unsetParser"
  )
  def setParser(parser: Parser) {
    this.parser = parser;
  }

  def unsetParser(parser: Parser) {
    this.parser = null;
  }
  
  @Activate
  def activate(context: ComponentContext) {
    val cgRemoteFile = ConfigDirProvider.configDir.getSubPath("remote-content-graph.ttl")
    if (cgRemoteFile.exists) {
      val locationG = parser.parse(cgRemoteFile.getInputStream, SupportedFormat.TURTLE)
      val p = new Preamble(locationG)
      import p._
      val endpoint = "http://sparql.endpont/".iri/-RDF.`type`
      g = new SparqlGraph(endpoint.getNode.asInstanceOf[IRI].getUnicodeString)
      val replacementNodes = endpoint/"http://example.org/replacement".iri
      if (replacementNodes.length > 1) {
        throw new RuntimeException("Multiple prefix-mapping not yet supported")
      }
      for (r <- replacementNodes) {
        val sourcePrefix = (r/"http://example.org/sourcePrefix".iri*)
        val targetPrefix = (r/"http://example.org/targetPrefix".iri*)
        g = new UriMutatingGraph(g, sourcePrefix, targetPrefix)
      }
    } else {
      val cgFile = ConfigDirProvider.configDir.getSubPath("content-graph.ttl")
      if (cgFile.exists) {
        g = parser.parse(cgFile.getInputStream, SupportedFormat.TURTLE)
      }
    }
  }
  
  override def existsLocal(iri: IRI): Boolean = {
    !getLocal(iri).getNodeContext.isEmpty
  }

  override def get(iri: IRI): GraphNode = {
    getLocal(iri)
  }

  override def getLocal(iri: IRI): GraphNode = {
    new GraphNode(iri, g)
  }
}

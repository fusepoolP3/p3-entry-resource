package eu.fusepool.p3.entry

import java.io.File
import java.io.FileInputStream
import org.apache.clerezza.commons.rdf.Graph
import org.apache.clerezza.commons.rdf.IRI
import org.apache.clerezza.commons.rdf.impl.utils.simple.SimpleGraph
import org.apache.clerezza.rdf.ontologies.DC
import org.apache.clerezza.rdf.ontologies.RDFS
import org.apache.clerezza.rdf.scala.utils.EzGraph
import org.apache.clerezza.rdf.utils.GraphNode
import org.apache.clerezza.rdf.utils.graphnodeprovider.GraphNodeProvider
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat
import org.apache.clerezza.rdf.core.serializedform.Parser
import org.osgi.service.component.ComponentContext
import org.osgi.service.component.annotations.Activate
import org.osgi.service.component.annotations.Component
import org.osgi.service.component.annotations.Reference
import org.osgi.service.component.annotations.ReferenceCardinality
import org.osgi.service.component.annotations.ReferencePolicy
//remove if not needed
import scala.collection.JavaConversions._

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
  def activate(context: ComponentContext) = {
    /*val cgFile = ConfigDirProvider.configDir.getSubPath("content-graph.ttl")
    if (cgFile.exists) {
      println("*********** using content-graph.ttl file");
      parser.parse(cgFile.getInputStream, SupportedFormat.TURTLE)
    }*/
  }
  
  override def existsLocal(iri: IRI): Boolean = {
    !new GraphNode(iri, g).getNodeContext.isEmpty
  }

  override def get(iri: IRI): GraphNode = {
    getLocal(iri)
  }

  override def getLocal(iri: IRI): GraphNode = {
    new GraphNode(iri, g)
  }
}

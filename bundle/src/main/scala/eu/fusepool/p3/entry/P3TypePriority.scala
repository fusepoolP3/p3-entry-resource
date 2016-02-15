package eu.fusepool.p3.entry

import org.apache.clerezza.commons.rdf.Graph
import org.apache.clerezza.commons.rdf.impl.utils.simple.SimpleGraph
import org.apache.clerezza.platform.typepriority.TypePrioritizer
import org.osgi.service.component.annotations.Component
//remove if not needed
import scala.collection.JavaConversions._

@Component(service = Array(classOf[TypePrioritizer]), immediate = true)
class P3TypePriority extends TypePrioritizer {

  bindSystemGraph(new SimpleGraph())

  protected override def unbindSystemGraph(arg0: Graph) {
  }
}

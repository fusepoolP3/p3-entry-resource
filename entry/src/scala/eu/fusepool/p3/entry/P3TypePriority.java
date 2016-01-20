package eu.fusepool.p3.entry;

import org.apache.clerezza.commons.rdf.Graph;
import org.apache.clerezza.commons.rdf.impl.utils.simple.SimpleGraph;
import org.apache.clerezza.platform.typepriority.TypePrioritizer;
import org.osgi.service.component.annotations.Component;

@Component(service = TypePrioritizer.class, immediate = true)
public class P3TypePriority extends TypePrioritizer {
	public P3TypePriority() {
		bindSystemGraph(new SimpleGraph());
	}
	@Override
	protected void unbindSystemGraph(Graph arg0) {
	}

}

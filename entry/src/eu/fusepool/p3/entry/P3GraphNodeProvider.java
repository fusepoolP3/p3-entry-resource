package eu.fusepool.p3.entry;

import org.apache.clerezza.commons.rdf.IRI;
import org.apache.clerezza.rdf.utils.GraphNode;
import org.apache.clerezza.rdf.utils.graphnodeprovider.GraphNodeProvider;
import org.osgi.service.component.annotations.Component;

@Component(service = GraphNodeProvider.class, immediate = true)
public class P3GraphNodeProvider implements GraphNodeProvider {

	@Override
	public boolean existsLocal(IRI iri) {
		System.out.println("********************** existLocal:"+iri);
		return false;
	}

	@Override
	public GraphNode get(IRI iri) {
		System.out.println("********************** get:"+iri);
		return null;
	}

	@Override
	public GraphNode getLocal(IRI iri) {
		System.out.println("********************** getLocal:"+iri);
		return null;
	}

}

package eu.fusepool.p3.entry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.clerezza.commons.rdf.Graph;
import org.apache.clerezza.commons.rdf.IRI;
import org.apache.clerezza.commons.rdf.impl.utils.simple.SimpleGraph;
import org.apache.clerezza.rdf.ontologies.RDF;
import org.apache.clerezza.rdf.utils.GraphNode;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import eu.fusepool.p3.vocab.FP3;



@Component(service = Object.class, property = "javax.ws.rs=true", immediate = true)
@Path("")
public class RootResource {
	
    IRI platformIri = new IRI("http://example.org/");
	private File myFile;
    
	@Activate
	protected void activate(ComponentContext context) throws IOException {
		myFile = context.getBundleContext().getDataFile("hello.txt");
		System.out.println("activating........"+myFile);
		FileWriter w = new FileWriter(myFile);
		w.write("hello\n");
		w.close();
	}
    
    @GET
    //@Produces("application/rdf+xml")
    public GraphNode root() {
    	System.out.println("======**********************************!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Graph g = new SimpleGraph();
        GraphNode node = new GraphNode(platformIri, g);
        node.addProperty(RDF.type, FP3.Platform);
        return node;
    }
	
	@GET
	@Path("hello")
	public String sayHello() {
		return "hallo "+myFile+" ... "+System.getenv("P3_CONFIG")+" in "+System.getProperty("user.dir");
	}

}

package eu.fusepool.p3.entry;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.osgi.service.component.annotations.Component;



@Component(service = Object.class, property = "javax.ws.rs=true")
@Path("")
public class RootResource {
	
	@GET
	public String sayHello() {
		return "hullo";
	}

}

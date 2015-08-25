package eu.fusepool.p3.entry;

import org.osgi.service.component.annotations.*;

@Component
public class ExampleComponent {


	@Activate
	void activate() {
		System.out.println("activating....");
	}

}

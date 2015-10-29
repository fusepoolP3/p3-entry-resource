function P3BackendConfigurator() {
}

P3BackendConfigurator.prototype

P3BackendConfigurator.prototype.initialize = function(platformEntryConfigurator) {
	return platformEntryConfigurator.ifUnconfigured(this.unconditionedInitialize);
}

P3BackendConfigurator.prototype.unconditionedInitialize = function(platformEntryConfigurator) {
	var configurator = this;
	var getLdpRoot = new Promise(function(resolve, reject) {
		//in a distribution we should resolve with the hard code ldp root
		var postTfrRequest = $.ajax({type: 'POST',
	        url: "http://sandbox.fusepool.info:8181/ldp",
	        headers: {'Content-Type': 'text/turtle', 'Slug': "test-config"},
	        async: true,
	        data: '<> a <http://www.w3.org/ns/ldp#BasicContainer> ; ' +
	        ' <http://www.w3.org/2000/01/rdf-schema#label> "The LDP root for a test instance"@en . ',
	        contentType: 'text/turtle'
	    });
	
	    postTfrRequest.done(function (response, textStatus, responseObj) {
	        console.info("Created platform LDP Root at" + responseObj.getResponseHeader('Location'));
	        //configurator.baseURI = responseObj.getResponseHeader('Location');
	        resolve(responseObj.getResponseHeader('Location'));
	    });
	    postTfrRequest.fail(function(failure) {
	        reject(Error(failure));
	    });
	});    
	return getLdpRoot.then(function(baseURI) { 
		var irldpcUri = baseURI + '/uir';
		var putIrldpcRequest = $.ajax({type: 'PUT',
	        url: irldpcUri,
	        headers: {'Content-Type': 'text/turtle'},
	        data: '<> a <http://www.w3.org/ns/ldp#BasicContainer> ; ' +
	                ' <http://www.w3.org/2000/01/rdf-schema#label> "Interaction Request Container"@en ; ' +
	                ' <http://www.w3.org/2000/01/rdf-schema#comment> "Contains Interaction Requests"@en . ',
	        async: true
	    });
	
		var registrations = [];
		registrations.push(putIrldpcRequest.then(function () {
			return platformEntryConfigurator.registerIRLDPC(irldpcUri);
		}));
		
		var tfrUri = baseURI + '/tfr';
		var putTfrRequest = $.ajax({type: 'PUT',
            url: tfrUri,
            headers: {'Content-Type': 'text/turtle'},
            data: '<> a <http://www.w3.org/ns/ldp#BasicContainer> ; ' +
                    ' <http://www.w3.org/2000/01/rdf-schema#label> "Transformer Factory Registry"@en ; ' +
                    ' <http://www.w3.org/2000/01/rdf-schema#comment> "Contains Transformer Factories"@en . ',
            async: true
        });
		
		registrations.push(putTfrRequest.then(function () {
			return platformEntryConfigurator.registerTFR(tfrUri);
		}));
		
		var trUri = baseURI + '/tr';
		var putTrRequest = $.ajax({type: 'PUT',
            url: trUri,
            headers: {'Content-Type': 'text/turtle'},
            data: '<> a <http://www.w3.org/ns/ldp#BasicContainer> ; ' +
                    ' <http://www.w3.org/2000/01/rdf-schema#label> "Transformer Registry"@en ; ' +
                    ' <http://www.w3.org/2000/01/rdf-schema#comment> "Contains Transformers"@en . ',
            async: true
        });
		
		registrations.push(putTrRequest.then(function () {
			return platformEntryConfigurator.registerTR(trUri);
		}));
		
		var dcrUri = baseURI + '/dcr';
		var putDcrRequest = $.ajax({type: 'PUT',
            url: dcrUri,
            headers: {'Content-Type': 'text/turtle'},
            data: '<> a <http://www.w3.org/ns/ldp#BasicContainer> ; ' +
                    ' <http://www.w3.org/2000/01/rdf-schema#label> "Dashboard Config Registry"@en ; ' +
                    ' <http://www.w3.org/2000/01/rdf-schema#comment> "Contains dashboard configurations"@en . ',
            async: true
        });
		
		registrations.push(putDcrRequest.then(function () {
			return platformEntryConfigurator.registerDCR(dcrUri);
		}));
		
		
		registrations.push(platformEntryConfigurator.registerSparql("http://sandbox.fusepool.info:8181/sparql/select"));
		
		registrations.push(platformEntryConfigurator.registerLdpRoot("http://sandbox.fusepool.info:8181/ldp/"));
		
		registrations.push(platformEntryConfigurator.registerDashboard("http://sandbox.fusepool.info:8200/?platformURI="+window.location));
		registrations.push(platformEntryConfigurator.registerApplication("http://sandbox.fusepool.info:8205/?platformURI="+window.location, 
				"P3 Resource GUI",
				"This is a graphical user interface to deal with Linked-Data-Platform-Collections."));
		
		registrations.push(platformEntryConfigurator.registerApplication("http://sandbox.fusepool.info:8151/?transformer=http%3A%2F%2Fsandbox.fusepool.info%3A8301%2F%3Ftaxonomy%3Dhttp%3A%2F%2Fdata.nytimes.com%2Fdescriptors.rdf&resource=http://www.bbc.com/news/science-environment-30005268", 
				"Transformer web client",
				"With the provided parameter it transforms the resource at <code>http://www.bbc.com/news/science-environment-30005268</code> using the transformer at <code>http://sandbox.fusepool.info:8301/?taxonomy=http%3A%2F%2Fdata.nytimes.com%2Fdescriptors.rdf</code>"));
	
		var platformPreparation = Promise.all(registrations);
		return platformPreparation.then(function() {
			return P3Platform.getPlatform(window.location).then(function(platform) {
				platformEntryConfigurator.lock();
				platform.transformerRegistry.registerTransformer("http://sandbox.fusepool.info:8303/", "Any23 Transformer", "Transform data using Apache Any23");
				platform.transformerFactoryRegistry.registerTransformerFactory(
                        "http://sandbox.fusepool.info:8201/?transformerBase=http://sandbox.fusepool.info:8300&platformURI="+window.location, "Pipeline UI", "Allows to create pipeline transformers.");
                platform.transformerFactoryRegistry.registerTransformerFactory(
                        "http://sandbox.fusepool.info:8202/?transformerBase=http://sandbox.fusepool.info:8301&platformURI="+window.location, "Dictionary Matcher UI", "Allows to create dictionary matcher transformers.");
                platform.transformerFactoryRegistry.registerTransformerFactory(
                        "http://sandbox.fusepool.info:8203/?transformerBase=http://sandbox.fusepool.info:8310&platformURI="+window.location, "Batchrefine UI", "Allows to create transformers using Openrefine configurations.");
                platform.transformerFactoryRegistry.registerTransformerFactory(
                        "http://sandbox.fusepool.info:8204/?transformerBase=http://sandbox.fusepool.info:8307&platformURI="+window.location, "XSLT Transformer UI", "Allows to create XSLT transformers.");
			});
		});

	});
};
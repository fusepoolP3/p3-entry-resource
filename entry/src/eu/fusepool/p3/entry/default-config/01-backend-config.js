function P3BackendConfigurator(platformEntryConfigurator) {
	this.platformEntryConfigurator = platformEntryConfigurator;
}

P3BackendConfigurator.initialize = function(platformEntryConfigurator) {
	return platformEntryConfigurator.ifUnconfigured(function(platformEntryConfigurator) {
		return new P3BackendConfigurator(platformEntryConfigurator).unconditionedInitialize();
	});
}

P3BackendConfigurator.prototype.serviceHost = "sandbox.fusepool.info";

P3BackendConfigurator.prototype.getLdpRoot = function() {
	var self = this;
	return new Promise(function(resolve, reject) {
		//in a distribution we should resolve with the hard code ldp root
		var postTfrRequest = $.ajax({type: 'POST',
	        url: "http://"+self.serviceHost+":8181/ldp",
	        headers: {'Content-Type': 'text/turtle', 'Slug': "test-config", 'Link': "<http://www.w3.org/ns/ldp#BasicContainer>; rel='type'"},
	        async: true,
	        data: '<> a <http://www.w3.org/ns/ldp#BasicContainer> ; ' +
	        ' <http://www.w3.org/2000/01/rdf-schema#label> "The LDP root for a test instance"@en . ',
	        contentType: 'text/turtle'
	    });
	
	    postTfrRequest.done(function (response, textStatus, responseObj) {
	        console.info("Created platform LDP Root at" + responseObj.getResponseHeader('Location'));
	        //configurator.ldpRoot = responseObj.getResponseHeader('Location');
	        resolve(responseObj.getResponseHeader('Location'));
	    });
	    postTfrRequest.fail(function(failure) {
	        reject(Error(failure));
	    });
	});
}

P3BackendConfigurator.prototype.registerRegistries = function(ldpRoot) {
	var self = this;
	var irldpcUri = ldpRoot + '/uir';
	var putIrldpcRequest = $.ajax({type: 'PUT',
        url: irldpcUri,
        headers: {'Content-Type': 'text/turtle', 'Link': "<http://www.w3.org/ns/ldp#BasicContainer>; rel='type'"},
        data: '<> a <http://www.w3.org/ns/ldp#BasicContainer> ; ' +
                ' <http://www.w3.org/2000/01/rdf-schema#label> "Interaction Request Container"@en ; ' +
                ' <http://www.w3.org/2000/01/rdf-schema#comment> "Contains Interaction Requests"@en . ',
        async: true
    });

	var ldpDependetRegistrations = [];
	ldpDependetRegistrations.push(putIrldpcRequest.then(function () {
		return self.platformEntryConfigurator.registerIRLDPC(irldpcUri);
	}));
	
	var tfrUri = ldpRoot + '/tfr';
	var putTfrRequest = $.ajax({type: 'PUT',
        url: tfrUri,
        headers: {'Content-Type': 'text/turtle', 'Link': "<http://www.w3.org/ns/ldp#BasicContainer>; rel='type'"},
        data: '<> a <http://www.w3.org/ns/ldp#BasicContainer> ; ' +
                ' <http://www.w3.org/2000/01/rdf-schema#label> "Transformer Factory Registry"@en ; ' +
                ' <http://www.w3.org/2000/01/rdf-schema#comment> "Contains Transformer Factories"@en . ',
        async: true
    });
	
	ldpDependetRegistrations.push(putTfrRequest.then(function () {
		return self.platformEntryConfigurator.registerTFR(tfrUri);
	}));
	
	var trUri = ldpRoot + '/tr';
	var putTrRequest = $.ajax({type: 'PUT',
        url: trUri,
        headers: {'Content-Type': 'text/turtle', 'Link': "<http://www.w3.org/ns/ldp#BasicContainer>; rel='type'"},
        data: '<> a <http://www.w3.org/ns/ldp#BasicContainer> ; ' +
                ' <http://www.w3.org/2000/01/rdf-schema#label> "Transformer Registry"@en ; ' +
                ' <http://www.w3.org/2000/01/rdf-schema#comment> "Contains Transformers"@en . ',
        async: true
    });
	
	ldpDependetRegistrations.push(putTrRequest.then(function () {
		return self.platformEntryConfigurator.registerTR(trUri);
	}));
	
	var dcrUri = ldpRoot + '/dcr';
	var putDcrRequest = $.ajax({type: 'PUT',
        url: dcrUri,
        headers: {'Content-Type': 'text/turtle', 'Link': "<http://www.w3.org/ns/ldp#BasicContainer>; rel='type'"},
        data: '<> a <http://www.w3.org/ns/ldp#BasicContainer> ; ' +
                ' <http://www.w3.org/2000/01/rdf-schema#label> "Dashboard Config Registry"@en ; ' +
                ' <http://www.w3.org/2000/01/rdf-schema#comment> "Contains dashboard configurations"@en . ',
        async: true
    });
	
	ldpDependetRegistrations.push(putDcrRequest.then(function () {
		return self.platformEntryConfigurator.registerDCR(dcrUri);
	}));
	
	return Promise.all(ldpDependetRegistrations);
}

P3BackendConfigurator.prototype.registerBackendfeatures = function(ldpRoot) {
	var registrations = [];
	registrations.push(this.platformEntryConfigurator.registerSparql("http://"+this.serviceHost+":8181/sparql/select"));
	registrations.push(this.platformEntryConfigurator.registerLdpRoot("http://"+this.serviceHost+":8181/ldp/"));
	return Promise.all(registrations);
}

P3BackendConfigurator.prototype.registerApplications = function(ldpRoot) {
	var registrations = [];
	registrations.push(this.platformEntryConfigurator.registerDashboard("http://"+this.serviceHost+":8200/?platformURI="+window.location));
	registrations.push(this.platformEntryConfigurator.registerApplication("http://"+this.serviceHost+":8205/?platformURI="+window.location, 
			"P3 Resource GUI",
			"This is a graphical user interface to deal with Linked-Data-Platform-Collections."));
	
	registrations.push(this.platformEntryConfigurator.registerApplication("http://"+this.serviceHost+":8151/?transformer=http%3A%2F%2F"+this.serviceHost+"%3A8301%2F%3Ftaxonomy%3Dhttp%3A%2F%2Fdata.nytimes.com%2Fdescriptors.rdf&resource=http://www.bbc.com/news/science-environment-30005268", 
			"Transformer web client",
			"With the provided parameter it transforms the resource at <code>http://www.bbc.com/news/science-environment-30005268</code> using the transformer at <code>http://"+this.serviceHost+":8301/?taxonomy=http%3A%2F%2Fdata.nytimes.com%2Fdescriptors.rdf</code>"));

	return Promise.all(registrations);
}

P3BackendConfigurator.prototype.registerTransfomersAndFactories = function(platform) {
	var platformRegsitrations = [];
	platformRegsitrations.push(platform.transformerRegistry.registerTransformer("http://"+this.serviceHost+":8303/", "Any23 Transformer", "Transform data using Apache Any23"));
	platformRegsitrations.push(platform.transformerFactoryRegistry.registerTransformerFactory(
            "http://"+this.serviceHost+":8201/?transformerBase=http://"+this.serviceHost+":8300&platformURI="+window.location, "Pipeline UI", "Allows to create pipeline transformers."));
	platformRegsitrations.push(platform.transformerFactoryRegistry.registerTransformerFactory(
            "http://"+this.serviceHost+":8202/?transformerBase=http://"+this.serviceHost+":8301&platformURI="+window.location, "Dictionary Matcher UI", "Allows to create dictionary matcher transformers."));
	platformRegsitrations.push(platform.transformerFactoryRegistry.registerTransformerFactory(
            "http://"+this.serviceHost+":8203/?transformerBase=http://"+this.serviceHost+":8310&platformURI="+window.location, "Batchrefine UI", "Allows to create transformers using Openrefine configurations."));
	platformRegsitrations.push(platform.transformerFactoryRegistry.registerTransformerFactory(
            "http://"+this.serviceHost+":8204/?transformerBase=http://"+this.serviceHost+":8307&platformURI="+window.location, "XSLT Transformer UI", "Allows to create XSLT transformers."));
    return Promise.all(platformRegsitrations);
}

P3BackendConfigurator.prototype.unconditionedInitialize = function() {
	var self = this;
	return this.getLdpRoot().then(function(ldpRoot) { 
		self.registerRegistries(ldpRoot).then(function() {
			var registrations = [];
			registrations.push(self.registerBackendfeatures(ldpRoot));
			registrations.push(self.registerApplications(ldpRoot));
			var platformPreparation = Promise.all(registrations);
			return platformPreparation.then(function() {
				return P3Platform.getPlatform(window.location).then(function(platform) {
					self.platformEntryConfigurator.lock();
					return self.registerTransfomersAndFactories(platform);
				});
			});
		});
	});
};
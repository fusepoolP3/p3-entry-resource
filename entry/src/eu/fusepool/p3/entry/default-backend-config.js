function P3BackendConfigurator() {
}

P3BackendConfigurator.prototype

P3BackendConfigurator.prototype.initialize = function(platformEntryConfigurator) {
	return platformEntryConfigurator.ifUnconfigured(this.unconditionedInitialize);
}

P3BackendConfigurator.prototype.unconditionedInitialize = function(platformEntryConfigurator) {
	var configurator = this;
	var createRoot = new Promise(function(resolve, reject) {
		var postTfrRequest = $.ajax({type: 'POST',
	        url: "http://sandbox.fusepool.info:8181/ldp",
	        headers: {'Content-Type': 'text/turtle', 'Slug': "test-config"},
	        async: true,
	        data: '<> a <http://www.w3.org/ns/ldp#BasicContainer> ; ' +
	        ' <http://www.w3.org/2000/01/rdf-schema#label> "A test instance"@en . ',
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
	return createRoot.then(function(baseURI) {
		var irldpcUri = baseURI + '/uir';
		var putIrldpcRequest = $.ajax({type: 'PUT',
	        url: irldpcUri,
	        headers: {'Content-Type': 'text/turtle'},
	        data: '<> a <http://www.w3.org/ns/ldp#BasicContainer> ; ' +
	                ' <http://www.w3.org/2000/01/rdf-schema#label> "Interaction Request Container"@en ; ' +
	                ' <http://www.w3.org/2000/01/rdf-schema#comment> "Contains Interaction Requests"@en . ',
	        async: true
	    });
	
		var irldpcRegistration = putIrldpcRequest.then(function () {
			platformEntryConfigurator.registerIRLDPC(irldpcUri);
		})
		
		var tfrUri = baseURI + '/tfr';
		var putTfrRequest = $.ajax({type: 'PUT',
            url: tfrUri,
            headers: {'Content-Type': 'text/turtle'},
            data: '<> a <http://www.w3.org/ns/ldp#BasicContainer> ; ' +
                    ' <http://www.w3.org/2000/01/rdf-schema#label> "Transformer Factory Registry"@en ; ' +
                    ' <http://www.w3.org/2000/01/rdf-schema#comment> "Contains Transformer Factories"@en . ',
            async: true
        });
		
		var tfrRegistration = putTfrRequest.then(function () {
			platformEntryConfigurator.registerTFR(tfrUri);
		});
		
		var trUri = baseURI + '/tr';
		var putTrRequest = $.ajax({type: 'PUT',
            url: trUri,
            headers: {'Content-Type': 'text/turtle'},
            data: '<> a <http://www.w3.org/ns/ldp#BasicContainer> ; ' +
                    ' <http://www.w3.org/2000/01/rdf-schema#label> "Transformer Registry"@en ; ' +
                    ' <http://www.w3.org/2000/01/rdf-schema#comment> "Contains Transformers"@en . ',
            async: true
        });
		
		var trRegistration = putTrRequest.then(function () {
			platformEntryConfigurator.registerTR(trUri);
		});
		
		var dcrUri = baseURI + '/dcr';
		var putDcrRequest = $.ajax({type: 'PUT',
            url: dcrUri,
            headers: {'Content-Type': 'text/turtle'},
            data: '<> a <http://www.w3.org/ns/ldp#BasicContainer> ; ' +
                    ' <http://www.w3.org/2000/01/rdf-schema#label> "Dashboard Config Registry"@en ; ' +
                    ' <http://www.w3.org/2000/01/rdf-schema#comment> "Contains dashboard configurations"@en . ',
            async: true
        });
		
		var dcrRegistration = putDcrRequest.then(function () {
			platformEntryConfigurator.registerDCR(dcrUri);
		});
		
		/*return new Promise(function (resolve, reject) {
			platformEntryConfigurator.registerTLDPC("http:example.rg/ldp");
			resolve();
		});*/
	
		return Promise.all([irldpcRegistration, tfrRegistration, trRegistration, dcrRegistration]);
	});
};
function PlatformEntryConfigurator(baseUri) {
	this.baseUri = baseUri;
	console.log("PlatformEntryConfigurator created for: "+baseUri);
}

/**
 * returns a promise that is fullfilled with conditional if the backend
 * is not yet fully configured, a fulfilled promise otherwise 
 */
PlatformEntryConfigurator.prototype.ifUnconfigured = function(conditional) {
	var configurator = this;
	var configuredRequest = $.get(this.baseUri+"fullyConfigured");
	return new Promise(function(resolve, reject) {
		configuredRequest.done(function (response) {
			if (response === "true") {
				resolve(Promise.resolve());
			} else {
				resolve(conditional(configurator));
			}
		});
		configuredRequest.fail(function(failure) {
	        reject(Error(failure));
	    });
	});
}

PlatformEntryConfigurator.prototype.registerTFR = function(tfr) {
	var configurator = this;
	return $.post(this.baseUri+"tfr", tfr, function(data, textstatus) {
		console.log("Registering TFR" +tfr+" with: "+configurator.baseUri+". Status: "+textstatus);
	})
}

PlatformEntryConfigurator.prototype.registerTR = function(tr) {
	var configurator = this;
	return $.post(this.baseUri+"tr", tr, function(data, textstatus) {
		console.log("Registering TR" +tr+" with: "+configurator.baseUri+". Status: "+textstatus);
	})
}

PlatformEntryConfigurator.prototype.registerIRLDPC = function(irldpc) {
	var configurator = this;
	return $.post(this.baseUri+"irldpc", irldpc, function(data, textstatus) {
		console.log("Registering irldpc" +irldpc+" with: "+configurator.baseUri+". Status: "+textstatus);
	})
}
PlatformEntryConfigurator.prototype.registerDCR = function(dcr) {
	var configurator = this;
	return $.post(this.baseUri+"dcr", dcr, function(data, textstatus) {
		console.log("Registering DCR" +dcr+" with: "+configurator.baseUri+". Status: "+textstatus);
	})
}

PlatformEntryConfigurator.prototype.registerSparql = function(sparqlEndpoint) {
	var configurator = this;
	return $.post(this.baseUri+"registerSparql", sparqlEndpoint, function(data, textstatus) {
		console.log("Registering sparql" +sparqlEndpoint+" with: "+configurator.baseUri+". Status: "+textstatus);
	})
}

PlatformEntryConfigurator.prototype.registerLdpRoot = function(ldpRoot) {
	var configurator = this;
	return $.post(this.baseUri+"registerLdpRoot", ldpRoot, function(data, textstatus) {
		console.log("Registering LDP Root" +ldpRoot+" with: "+configurator.baseUri+". Status: "+textstatus);
	})
}

PlatformEntryConfigurator.prototype.registerDashboard = function(dashboard) {
	var configurator = this;
	return $.post(this.baseUri+"registerDashboard", dashboard, function(data, textstatus) {
		console.log("Registering dashboard" +dashboard+" with: "+configurator.baseUri+". Status: "+textstatus);
	})
}

//TODO add support for description
PlatformEntryConfigurator.prototype.registerApplication = function(application) {
	var configurator = this;
	return $.post(this.baseUri+"registerApplication", application, function(data, textstatus) {
		console.log("Registering application" +application+" with: "+configurator.baseUri+". Status: "+textstatus);
	})
}



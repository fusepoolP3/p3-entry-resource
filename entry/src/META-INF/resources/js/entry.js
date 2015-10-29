$(function() {
	var store = new rdf.LdpStore({
		parsers : {
			'text/turtle' : rdf.parseTurtle
		}
	});
	LD2h.store = store;
	var configurator = new PlatformEntryConfigurator(window.location);
	new P3BackendConfigurator().initialize(configurator)
			.then(
					function() {
						$("#wait").hide();
						$("#content").css('visibility', 'visible');
						LD2h.expand();
					});
});
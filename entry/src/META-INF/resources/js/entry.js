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
						var host = window.location.hostname;
						$(".dashboard-link").attr(
								"href",
								"http://" + host + ":8200/?platformURI="
										+ window.location);
						$(".host").each(function() {
							$(this).text(host)
						});
						$("a").each(function() {
							var origHref = $(this).attr("href");
							var newHref = origHref.replace(/{host}/g, host);
							$(this).attr("href", newHref);
						});
						$("#wait").hide();
						$("#content").css('visibility', 'visible');
						LD2h.expand();
					});
});
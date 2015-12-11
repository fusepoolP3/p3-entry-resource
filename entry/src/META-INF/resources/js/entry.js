$(function() {
	var store = new LdpStore({
		parsers : new LdpStore.ParserUtil({
			'text/turtle' : LdpStore.parsers.findParsers("text/turtle")[0]
		})
	});
	LD2h.store = store;
	var configurator = new PlatformEntryConfigurator(window.location);
	P3BackendConfigurator.initialize(configurator)
			.then(
				function() {
					localStorage.setItem("refreshCount", 0);
					$("#wait").hide();
					$("#content").css('visibility', 'visible');
					LD2h.expand();
				}, 
				function(error) {
					var refreshCount = parseInt(localStorage.getItem("refreshCount"));
					if (refreshCount > 10) {
						localStorage.setItem("refreshCount", 0);
						$("#wait").html("<b>Sorry!</b> We have been waiting to long for backend initialization to succeed, it seems something went wrong.")
					} else {
						localStorage.setItem("refreshCount", refreshCount + 1);
						console.log("Reloading in 4s as initialization failed ("+refreshCount+")", error);
	                    var reload = function () {
	                        location.reload();
	                    }
	                    setTimeout(reload, 4000);
					}
				});
});
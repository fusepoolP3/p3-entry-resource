package eu.fusepool.p3.entry.style

import org.apache.clerezza.platform.typerendering._
import org.apache.clerezza.rdf.scala.utils.RichGraphNode
import org.apache.clerezza.rdf.ontologies._
import org.apache.clerezza.rdf.utils._
import org.apache.clerezza.rdf.scala.utils.Preamble._
import org.apache.clerezza.platform.typerendering.scala._
import org.osgi.service.component.annotations._
import eu.fusepool.p3.entry.BootScripts
import eu.fusepool.p3.vocab.FP3

/**
 * Renders resources using ld2h on the client.
 */
@Component(service = Array(classOf[TypeRenderlet]))
class ResourceRenderlet extends SRenderlet {
  
  var bootScripts: BootScripts = null
  
  @Reference(
    cardinality = ReferenceCardinality.MANDATORY,
    policy = ReferencePolicy.STATIC,
    unbind = "unsetBootScripts"
  )
  def setBootScripts(tcManager: BootScripts) {
    this.bootScripts = tcManager;
  }

  def unsetBootScripts(tcManager: BootScripts) {
    this.bootScripts = null;
  }

  val getRdfType = FP3.Platform

  override def getModePattern = "(?!.*naked)(?!.*menu).*"

  protected def defaultTitle(res: RichGraphNode) = "A Resource" + (res*)

  override def renderedPage(arguments: XmlResult.Arguments) = {
    new XmlResult(arguments) {
      def menuLink(href: String, label: String) =
        if ((res*).endsWith(href) || (res*).endsWith(href + "index")) {
          <a href={ href } class="active">{ label }</a>
        } else {
          <a href={ href }>{ label }</a>
        }
      override def content = {
        <html xmlns="http://www.w3.org/1999/xhtml" >
          <head>
            <title>Fusepool P3 Platform</title>
						<link rel="stylesheet" type="text/css" href="/style/platform.css" />
            <link rel="matchers" href="/rdf2h/matchers.ttl" type="text/turtle" />
            <link rel="matchers" href="/rdf2h/platform-matchers.ttl" type="text/turtle"/>
            <link rel="matchers" href="//cdn.rawgit.com/rdf2h/rdf2h.github.io/v0.0.1/2015/rdf2h-points.ttl" type="text/turtle"/>
            <script src="//cdn.rawgit.com/rdf2h/rdf2h/v0.2.1/dist/rdf-ext.js"></script>
            <script src="//cdn.rawgit.com/rdf2h/rdf2h/v0.2.1/dist/rdf2h.js"></script>
            <script src="//code.jquery.com/jquery-2.1.4.min.js"></script>
            <script src="//cdn.rawgit.com/rdf2h/ld2h/v0.2.0/dist/ld2h.js"></script>
            <script src="//cdn.rawgit.com/retog/rdf-store-ldp-browser/v0.3.0-rc2f/dist/rdf-store-ldp.js"></script>
            <script src="//cdn.rawgit.com/retog/clownface-browser/v0.3.0-rc2/dist/clownface.js"></script>
            <script>
              <!-- limited backward compatibility with rdf-ext 0.2 for p3-platform-js -->
              rdf.parseTurtle = function(turtle, callback) {{
							  LdpStore.parsers.findParsers("text/turtle")[0].parse(turtle, callback)
							}}
							rdf.cf = Clownface
            </script>
            <script src="/js/p3-platform-js/p3-platform-js.js"></script>
            <script src="/js/PlatformEntryConfigurator.js"></script>
            {
              for (script <- bootScripts.scriptList.sorted) yield {
                <script src={ "js/boot-scripts/" + script }></script>

              }
            }
            <script src="/js/entry.js"></script>
          </head>
          <body>
				<div id="wait">
            Please wait, the platform is not quite ready yet.
        </div>
        <div id="content" style="visibility:  hidden">
            <div id="platform" class="fetch" resource="">
				</div>

        </div>
          </body>
        </html>
      }
    }
  }

}

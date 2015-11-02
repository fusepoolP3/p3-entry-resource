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
  
  @Reference
  var bootScripts: BootScripts = null 

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
						<link rel="matchers" href="/rdf2h/platform-matchers.ttl" type="text/turtle" />
						<link rel="matchers" href="https://rawgit.com/rdf2h/rdf2h.github.io/master/2015/rdf2h-points.ttl" type="text/turtle" />
            <script src="/js/ld2h/js/libs/rdf2h/rdf2h.js"></script>
            <script src="/js/ld2h/js/libs/jquery/jquery.min.js"></script>
            <script src="/js/ld2h/js/ld2h.js"></script>
						<script src="/js/rdfstore/rdfstore.js"></script>
						<script src="/js/p3-platform-js/p3-platform-js.js"></script>
						<script src="/js/PlatformEntryConfigurator.js"></script>
						{
						  for (script <- bootScripts.scriptList.sorted) yield {
						    <script src={ "js/boot-scripts/"+script }></script>
						    
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

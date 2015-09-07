package eu.fusepool.p3.entry.style

import org.apache.clerezza.platform.typerendering._
import org.apache.clerezza.rdf.scala.utils.RichGraphNode
import org.apache.clerezza.rdf.ontologies._
import org.apache.clerezza.rdf.utils._
import org.apache.clerezza.rdf.scala.utils.Preamble._
import org.apache.clerezza.platform.typerendering.scala._
import org.osgi.service.component.annotations._
import eu.fusepool.p3.vocab.FP3

/**
 * Renders resources using ld2h on the client.
 */
@Component(service = Array(classOf[TypeRenderlet]))
class ResourceRenderlet extends SRenderlet {

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
        <html xmlns="http://www.w3.org/1999/xhtml" class="fetch" resource="">
          <head>
            <title>Fusepool P3 Platform</title>
            <link rel="matchers" href="/rdf2h/matchers.ttl" type="text/turtle" />
            <script src="/js/ld2h/js/libs/rdf2h/rdf2h.js"></script>
            <script src="/js/ld2h/js/libs/jquery/jquery.min.js"></script>
            <script src="/js/ld2h/js/ld2h.js"></script>
						<script src="/js/PlatformEntryConfigurator.js"></script>
						<script src="/js/backend-config.js"></script>   
     
            <script type="text/javascript">
$(function () {{
   var store = new rdf.LdpStore({{parsers: {{
       //'application/ld+json': rdf.parseJsonLd,
       //'application/n-triples': rdf.parseTurtle,
       'text/turtle': rdf.parseTurtle
   }}}});
   LD2h.store = store;
   LD2h.expand();
}});
            </script>
          </head>
          <body>
<div id="wait">
            Please wait, the platform is not quite ready yet.
        </div>
        <div id="content" style="visibility:  hidden">
            <h1>Welcome to P3</h1>
            <p>Check out the <a class="dashboard-link">Dashboard</a> which allows access
                to most of the functionality of the P3 Platform.</p>

            <h2>P3 Resource GUI</h2>

            <p>This is a graphical user interface to deal with Linked-Data-Platform-Collections.</p>
            <a href="http://{host}:8205/?defaultContainer=http://{host}:8181/ldp"><span class="host"></span>:8205/?defaultContainer=<span class="host"></span>:8181/ldp</a>

            <h2>P3 Dashboard</h2>

            <p>P3 administrator dashboard is the main user interface for uploading files easily to the platform using transformers. 
                <a class="dashboard-link"><span class="host"></span>:8200/?platformURI=http://<span class="host"></span>:8181/ldp/platform</a></p>

            <h2>Transformer Web Client</h2>
            
            <p>By default a transformers is used by uploading the data that needs 
                to be transformed via HTTP POST. The Transformer Web Client allows
                to transform resources that are already on the Web.</p>
            <p>The following URI transforms the resource at <code>http://www.bbc.com/news/science-environment-30005268</code>
                uisng the transformer at 
                <code>http://<span class="host"></span>:8301/?taxonomy=http%3A%2F%2Fdata.nytimes.com%2Fdescriptors.rdf</code>:
                <ul>
                    <li><a href="http://{{host}}:8151/?transformer=http%3A%2F%2F{{host}}%3A8301%2F%3Ftaxonomy%3Dhttp%3A%2F%2Fdata.nytimes.com%2Fdescriptors.rdf&amp;resource=http://www.bbc.com/news/science-environment-30005268">http://<span class="host"></span>:8151/?transformer=http%3A%2F%2F<span class="host"></span>%3A8301%2F%3Ftaxonomy%3Dhttp%3A%2F%2Fdata.nytimes.com%2Fdescriptors.rdf&amp;resource=http://www.bbc.com/news/science-environment-30005268</a></li>
                </ul>
            </p>

            <h2>All components in this Docker</h2>
            
            <p>You can directly access the individual components. note that 
                some components need to invoked with some query parameters, check their
                documentation on github:</p>

            <ul class="task-list">
                <li><a href="http://{host}:8181/ldp/"><span class="host"></span>:8181</a> - Fusepool's <a href="https://github.com/fusepoolP3/p3-ldp-marmotta">Marmotta LDP</a> over <a href="https://github.com/fusepoolP3/p3-proxy">p3-proxy</a>
                </li>
                <li><a href="http://{host}:8151/"><span class="host"></span>:8151</a> - <a href="https://github.com/fusepoolP3/p3-transformer-web-client">p3-transformer-web-client</a>
                </li>
                <li><a class="dashboard-link"><span class="host"></span>:8200</a> - <a href="https://github.com/fusepoolP3/p3-dashboard">p3-dashboard</a>
                </li>
                <li><a href="http://{host}:8201/"><span class="host"></span>:8201</a> - <a href="https://github.com/fusepoolP3/p3-pipeline-gui-js">p3-pipeline-gui-js</a>
                </li>
                <li><a href="http://{host}:8202/"><span class="host"></span>:8202</a> - <a href="https://github.com/fusepoolP3/p3-dictionary-matcher-factory-gui">p3-dictionary-matcher-factory-gui</a>
                </li>
                <li><a href="http://{host}:8203/"><span class="host"></span>:8203</a> - <a href="https://github.com/fusepoolP3/p3-batchrefine-factory-gui">p3-batchrefine-factory-gui</a>
                </li>
                <li><a href="http://{host}:8204/"><span class="host"></span>:8204</a> - <a href="https://github.com/fusepoolP3/p3-xslt-factory-gui">p3-xslt-factory-gui</a>
                </li>
                <li><a href="http://{host}:8205/"><span class="host"></span>:8205</a> - <a href="https://github.com/fusepoolP3/p3-resource-gui">p3-resource-gui</a>
                </li>
                <li><a href="http://{host}:8300/"><span class="host"></span>:8300</a> - <a href="https://github.com/fusepoolP3/p3-pipeline-transformer">p3-pipeline-transformer</a>
                </li>
                <li><a href="http://{host}:8301/"><span class="host"></span>:8301</a> - <a href="https://github.com/fusepoolP3/p3-dictionary-matcher-transformer">p3-dictionary-matcher-transformer</a>
                </li>
                <li><a href="http://{host}:8302/"><span class="host"></span>:8302</a> - <a href="https://github.com/fusepoolP3/p3-geo-enriching-transformer">p3-geo-enriching-transformer</a>
                </li>
                <li><a href="http://{host}:8303/"><span class="host"></span>:8303</a> - <a href="https://github.com/fusepoolP3/p3-any23-transformer">p3-any23-transformer</a>
                </li>
                <li><a href="http://{host}:8304/"><span class="host"></span>:8304</a> - <a href="https://github.com/fusepoolP3/p3-stanbol-launcher">p3-stanbol-launcher</a>
                </li>
                <li><a href="http://{host}:8305/"><span class="host"></span>:8305</a> - <a href="https://github.com/fusepoolP3/p3-literal-extraction-transformer">p3-literal-extraction-transformer</a>
                </li>
                <li><a href="http://{host}:8306/"><span class="host"></span>:8306</a> - <a href="https://github.com/fusepoolP3/p3-silkdedup">p3-silkdedup</a>
                </li>
                <li><a href="http://{host}:8307/"><span class="host"></span>:8307</a> - <a href="https://github.com/fusepoolP3/p3-xslt-transformer">p3-xslt-transformer</a>
                </li>
                <li><a href="http://{host}:8308/"><span class="host"></span>:8308</a> - <a href="https://github.com/fusepoolP3/p3-geocoordinates-transformer">p3-geocoordinates-transformer</a>
                </li>
                <li><a href="http://{host}:8310/"><span class="host"></span>:8310</a> - <a href="https://github.com/fusepoolP3/p3-batchrefine">p3-batchrefine</a>
                </li>
                <li><a href="http://{host}:8386/"><span class="host"></span>:8386</a> - <a href="https://github.com/fusepoolP3/punditTransformer">p3-pundi-tranformer</a>
                </li>
                <li><a href="http://{host}:8387/"><span class="host"></span>:8387</a> - Log Analysis
                </li>
                <li><a href="http://{host}:8388/"><span class="host"></span>:8387</a> - Real-time log monitoring
                </li>

            </ul>

            <h2>Platform details</h2>
            <div id="platform"></div>

        </div>
          </body>
        </html>
      }
    }
  }

}

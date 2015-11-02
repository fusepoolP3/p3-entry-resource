package eu.fusepool.p3.entry

import org.apache.clerezza.rdf.utils.GraphNode
import org.apache.clerezza.utils.osgi.BundlePathNode
import org.osgi.service.component.annotations._
import javax.ws.rs._
import javax.ws.rs.core._
import org.apache.clerezza.rdf.scala.utils._
import org.apache.clerezza.commons.rdf._
import org.apache.clerezza.rdf.ontologies._
import eu.fusepool.p3.vocab.FP3
import Preamble._
import java.io.InputStreamReader
import java.io.Reader
import java.io.File
import java.io.FileReader
import org.wymiwyg.commons.util.dirbrowser.PathNode
import org.wymiwyg.commons.util.dirbrowser.PathNodeFactory
import org.wymiwyg.commons.util.dirbrowser.PathNameFilter
import org.osgi.framework.BundleContext
import org.wymiwyg.commons.util.dirbrowser.FilePathNode
import org.wymiwyg.commons.util.dirbrowser.MultiPathNode

/**
 * This JAX-RS resource returns a js-scripts to configure the backend, i.e. to create the various registries.
 *
 * It takes scripts from the following locations, where scripts at earlier locations override the later
 * ~/.fusepool-p3/boot-scripts
 * /etc/fusepool-p3/boot-scripts
 * /eu/fusepool/p3/entry/default-config/ in this bundle
 */
@Component(service = Array(classOf[Object], classOf[BootScripts]), property = Array("javax.ws.rs=true"))
@Path("js/boot-scripts")
class BootScripts {

  private val homeDir = new File(System.getProperty("user.home"));
  
  private def userConfigDir = new File(homeDir, ".fusepool-p3/boot-scripts/");
  
  private def systemConfigDir = new File("/etc/fusepool-p3/boot-scripts/");
  
  private var configDir: PathNode = null;
  
  @Activate
  protected def activate(context: BundleContext) {
    val nodes = new scala.collection.mutable.ListBuffer[PathNode]
    val defaultConfigDir = new BundlePathNode(context.getBundle, "eu/fusepool/p3/entry/default-config/");
    nodes +=  defaultConfigDir;
    if (systemConfigDir.exists()) {
      val systemConfigNode: PathNode =new FilePathNode(systemConfigDir);
      nodes += systemConfigNode;
    }
    if (userConfigDir.exists()) {
      val userConfigNode: PathNode =new FilePathNode(userConfigDir);
      nodes += userConfigNode;
    }
    configDir = new MultiPathNode(nodes.toList.reverse:_*);
  }
  
  @GET
  @Path("{script}")
  def getScript(@PathParam("script") script: String) : Reader = {
    val scriptNode = configDir.getSubPath(script);
    if (!scriptNode.exists()) {
      throw new WebApplicationException(404);
    }
    return new InputStreamReader(scriptNode.getInputStream);
  }
  
  def scriptList: Array[String] =  {
    configDir.list(new PathNameFilter() {
      def accept(node:PathNode, name: String) = name.endsWith(".js");
    });
  }
}
package eu.fusepool.p3.entry

import java.io.File
import org.wymiwyg.commons.util.dirbrowser.PathNode
import org.wymiwyg.commons.util.dirbrowser.PathNodeFactory
import org.wymiwyg.commons.util.dirbrowser.PathNameFilter
import org.wymiwyg.commons.util.dirbrowser.FilePathNode
import org.wymiwyg.commons.util.dirbrowser.MultiPathNode

object ConfigDirProvider {

  private val homeDir = new File(System.getProperty("user.home"));

  def userConfigDir = new File(homeDir, ".fusepool-p3/");

  private def systemConfigDir = new File("/etc/fusepool-p3/");
  
  def configDir = {
    val nodes = new scala.collection.mutable.ListBuffer[PathNode]
    if (!userConfigDir.exists()) {
      userConfigDir.mkdirs();
    }
    val userConfigNode: PathNode =new FilePathNode(userConfigDir);
    nodes += userConfigNode;
    if (systemConfigDir.exists()) {
      val systemConfigNode: PathNode =new FilePathNode(systemConfigDir);
      nodes += systemConfigNode;
    }
    new MultiPathNode(nodes.toList:_*);
  }
}
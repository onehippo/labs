package org.hippoecm.frontend.plugins.cms.dev.updater

import org.onehippo.repository.update.BaseNodeUpdateVisitor
import nl.company.cms.fundamentals.cms.utils.ResourceBundleBootstrapUtilities
import nl.company.cms.fundamentals.cms.utils.ResourceBundleItem
import javax.jcr.Node


/**
* AUTHOR: Bart Vreeken
* LAST CHANGE: 10-02-2015
* DESCRIPTION:
* The Groovy script automates the process of adding / deleting resource label keys
* to an EXISTING resourcebundle.
*
* Using the Hippo XML bootstrap mechanism to add extra message keys is quite cumbersome. This script uses
* a Java helper class (the ResourceBundleBootstrapUtilities) to find new messages in config property files, and
* and adds these to the existing bundle.
*
* USAGE:
* Add *.properties files to any Hippo cms plugin, into the -boostrap section. Put all your property files in the
* /src/main/resources/labels path (create a 'labels' package if not already exist).
*
* QUERY:
* Simply walk through all resourcebundle nodes.
*
* /jcr:root/content/*\//element(*,resourcebundle:resourcebundle) (remove the backslash)
*
*
*/
class UpdaterTemplate extends BaseNodeUpdateVisitor {

  boolean doUpdate(Node node) {
    String bundleId = node.getProperty("resourcebundle:id").getString();
    String nodeStatus = node.getProperty("hippostd:state").getString();
    def today = new Date();

    log.debug "Starting at ${today}, visit bundle ${bundleId} (status ${nodeStatus}) \n"
    List bundleList = ResourceBundleBootstrapUtilities.findResourceBundleConfig(bundleId);
    for(item in bundleList) {
    //log.debug "Possible candidate key: '${item.getKey()}'"
    boolean doesAlreadyExist = doesKeyAlreadyExist(node, item.getKey())
    if(doesAlreadyExist == false && item.getAction().toString() == "ADD") {

      log.debug "Found non-existing key: '${item.getKey()}'. Will be ADDED"
      addItem(node, item)


      } else if(doesAlreadyExist == true && item.getAction().toString() == "DELETE"){
        log.debug "Found existing key: '${item.getKey()}'. Will be REMOVED"
        removeItem(node, item)
      }
    }


    return false
  }



  void removeItem(Node node, ResourceBundleItem item) {

    //prepare arraylist
    List keyList = new ArrayList()
    List descList = new ArrayList()
    List messageList = new ArrayList()
    List messageNlList = new ArrayList()
    //fill lists
    loadLists(node, keyList, descList, messageList, messageNlList)

    Property keysProp = node.getProperty("resourcebundle:keys")
    Value[] keyValues = keysProp.getValues()
    keyValues.eachWithIndex { value, index ->
    if(value.getString() == item.getKey()) {
        //log.debug "DELETE ${index}"
        //log.debug "Item key to delete: ${item.getKey()}"
        keyList.remove(index)
        descList.remove(index)
        messageList.remove(index)
        messageNlList.remove(index)
      }
    }

    //remove KEY property
    Value[] newKeyValues = (Value[]) keyList.toArray(new Value[keyList.size()])
    node.setProperty("resourcebundle:keys", newKeyValues)

    //remove DESCRIPTION property
    Value[] newDescValues = (Value[]) descList.toArray(new Value[descList.size()])
    node.setProperty("resourcebundle:descriptions", newDescValues)

    //remove MESSAGE property
    Value[] newMessageValues = (Value[]) messageList.toArray(new Value[messageList.size()])
    node.setProperty("resourcebundle:messages", newMessageValues)

    //remove MESSAGE_NL property
    Value[] newMessageNlValues = (Value[]) messageNlList.toArray(new Value[messageNlList.size()])
    node.setProperty("resourcebundle:messages_nl", newMessageNlValues)

    log.debug "Removing messagekey '${item.getKey()}' COMPLETED.\n"
  }


  void addItem(Node node, ResourceBundleItem item) {

      ValueFactory vFactory= node.getSession().getValueFactory()
      //prepare arraylist
      List keyList = new ArrayList()
      List descList = new ArrayList()
      List messageList = new ArrayList()
      List messageNlList = new ArrayList()
      //fill lists
      loadLists(node, keyList, descList, messageList, messageNlList)


      //add KEY property
      Value newKeyValue=vFactory.createValue(item.getKey())
      keyList.add(newKeyValue)
      Value[] newKeyValues = (Value[]) keyList.toArray(new Value[keyList.size()])
      node.setProperty("resourcebundle:keys", newKeyValues)


      //add DESCRIPTION property
      Value newDescValue=vFactory.createValue(item.getDescription())
      descList.add(newDescValue)
      Value[] newDescValues = (Value[]) descList.toArray(new Value[descList.size()])
      node.setProperty("resourcebundle:descriptions", newDescValues)

      //add MESSAGE property
      Value newMessageValue=vFactory.createValue(item.getMessage())
      messageList.add(newMessageValue)
      Value[] newMessageValues = (Value[]) messageList.toArray(new Value[messageList.size()])
      node.setProperty("resourcebundle:messages", newMessageValues)

      //add MESSAGE_NL property
      Value newMessageNlValue=vFactory.createValue(item.getMessageNl())
      messageNlList.add(newMessageNlValue)
      Value[] newMessageNlValues = (Value[]) messageNlList.toArray(new Value[messageNlList.size()])
      node.setProperty("resourcebundle:messages_nl", newMessageNlValues)

      log.debug "Adding messagekey '${item.getKey()}' COMPLETED.\n"
  }

  void loadLists(Node node, List keyList, List descList, List messageList, List messageNlList) {

      //load KEY property
      Property keysProp = node.getProperty("resourcebundle:keys")
      Value[] keyValues = keysProp.getValues()
      for(value in keyValues) {
        keyList.add(value)
      }


      //load DESCRIPTION property
      Property descProp = node.getProperty("resourcebundle:descriptions")
      Value[] descValues = descProp.getValues()
      for(value in descValues) {
        descList.add(value)
      }

      //load MESSAGE property
      Property messageProp = node.getProperty("resourcebundle:messages")
      Value[] messageValues = messageProp.getValues()
      for(value in messageValues) {
        messageList.add(value)
      }

      //load MESSAGE_NL property
      Property messageNlProp = node.getProperty("resourcebundle:messages_nl")
      Value[] messageNlValues = messageNlProp.getValues()
      for(value in messageNlValues) {
        messageNlList.add(value)
      }

  }


  boolean doesKeyAlreadyExist(Node node, String key) {

    Property keysProp = node.getProperty("resourcebundle:keys")
    Value[] values = keysProp.getValues()
    for(val in values) {
      //log.debug "Key found: ${val.getString()}"
      // == means equals in Groovy
      if(val.getString() == key) {
        return true
      }
    }
    return false
   }

   boolean undoUpdate(Node node) {
      throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
   }

}
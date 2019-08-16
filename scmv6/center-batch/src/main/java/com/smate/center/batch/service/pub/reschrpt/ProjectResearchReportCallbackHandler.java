/**
 * ProjectResearchReportCallbackHandler.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.5 Built on : Apr 30, 2009
 * (06:07:24 EDT)
 */

package com.smate.center.batch.service.pub.reschrpt;

/**
 * ProjectResearchReportCallbackHandler Callback class, Users can extend this class and implement
 * their own receiveResult and receiveError methods.
 */
public abstract class ProjectResearchReportCallbackHandler {

  protected Object clientData;

  /**
   * User can pass in any object that needs to be accessed once the NonBlocking Web service call is
   * finished and appropriate method of this CallBack is called.
   * 
   * @param clientData Object mechanism by which the user can pass in user data that will be avilable
   *        at the time this callback is called.
   */
  public ProjectResearchReportCallbackHandler(Object clientData) {
    this.clientData = clientData;
  }

  /**
   * Please use this constructor if you don't want to set any clientData
   */
  public ProjectResearchReportCallbackHandler() {
    this.clientData = null;
  }

  /**
   * Get the client data
   */

  public Object getClientData() {
    return clientData;
  }

  /**
   * auto generated Axis2 call back method for getProjectReportFinalPubs method override this method
   * for handling normal response from getProjectReportFinalPubs operation
   */
  public void receiveResultgetProjectReportFinalPubs(GetProjectReportFinalPubsResponse result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * getProjectReportFinalPubs operation
   */
  public void receiveErrorgetProjectReportFinalPubs(java.lang.Exception e) {}

}


/**
 * ProjectResearchReport.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.5 Built on : Apr 30, 2009
 * (06:07:24 EDT)
 */

package com.smate.center.batch.service.pub.reschrpt;

/*
 * ProjectResearchReport java interface
 */

public interface ProjectResearchReport {

  /**
   * Auto generated method signature
   * 
   * @param getProjectReportFinalPubs0
   */

  public GetProjectReportFinalPubsResponse getProjectReportFinalPubs(

      GetProjectReportFinalPubs getProjectReportFinalPubs0) throws java.rmi.RemoteException;

  /**
   * Auto generated method signature for Asynchronous Invocations
   * 
   * @param getProjectReportFinalPubs0
   */
  public void startgetProjectReportFinalPubs(

      GetProjectReportFinalPubs getProjectReportFinalPubs0,

      final ProjectResearchReportCallbackHandler callback)

      throws java.rmi.RemoteException;

  //
}

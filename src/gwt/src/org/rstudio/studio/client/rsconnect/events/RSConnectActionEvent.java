/*
 * RSConnectActionEvent.java
 *
 * Copyright (C) 2009-15 by RStudio, Inc.
 *
 * Unless you have received this program directly from RStudio pursuant
 * to the terms of a commercial license agreement with RStudio, then
 * this program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
 *
 */
package org.rstudio.studio.client.rsconnect.events;

import org.rstudio.studio.client.rmarkdown.model.RmdPreviewParams;
import org.rstudio.studio.client.rsconnect.RSConnect;
import org.rstudio.studio.client.rsconnect.model.RSConnectDeploymentRecord;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class RSConnectActionEvent extends GwtEvent<RSConnectActionEvent.Handler>
{ 
   public interface Handler extends EventHandler
   {
      void onRSConnectAction(RSConnectActionEvent event);
   }

   public static final GwtEvent.Type<RSConnectActionEvent.Handler> TYPE =
      new GwtEvent.Type<RSConnectActionEvent.Handler>();
   
   public static RSConnectActionEvent ConfigureAppEvent(String path)
   {
      return new RSConnectActionEvent(ACTION_TYPE_CONFIGURE, 
            RSConnect.CONTENT_TYPE_APP, path, null, null, null);
   }

   public static RSConnectActionEvent DeployAppEvent(String path, 
         RSConnectDeploymentRecord fromPrevious)
   {
      return new RSConnectActionEvent(ACTION_TYPE_DEPLOY, 
            RSConnect.CONTENT_TYPE_APP, path, null, null, fromPrevious);
   }
   
   public static RSConnectActionEvent DeployRmdEvent(RmdPreviewParams params,
         RSConnectDeploymentRecord fromPrevious)
   {
      return new RSConnectActionEvent(ACTION_TYPE_DEPLOY,
            RSConnect.CONTENT_TYPE_RMD, 
            params.getTargetFile(),
            params.getOutputFile(), 
            params,
            fromPrevious);
   }
   
   public static RSConnectActionEvent DeployPlotEvent(String htmlFile)
   {
      return new RSConnectActionEvent(ACTION_TYPE_DEPLOY, 
            RSConnect.CONTENT_TYPE_PLOT, null, htmlFile, null, null);
   }

   private RSConnectActionEvent(int action, int contentType, String path, 
                               String htmlFile, RmdPreviewParams fromRmdPreview,
                               RSConnectDeploymentRecord fromPrevious)
   {
      action_ = action;
      contentType_ = contentType;
      path_ = path;
      fromPrevious_ = fromPrevious;
      rmdPreview_ = fromRmdPreview;
      
      // determine location of static content, if any
      if (htmlFile != null)
         htmlFile_ = htmlFile;
      else if (fromRmdPreview != null)
         htmlFile_ = fromRmdPreview.getOutputFile();
      else
         htmlFile_ = null;
   }
   
   public String getPath()
   {
      return path_;
   }
   
   public int getAction()
   {
      return action_;
   }
   
   public RmdPreviewParams getFromRmdPreview()
   {
      return rmdPreview_;
   }
   
   public RSConnectDeploymentRecord getFromPrevious()
   {
      return fromPrevious_;
   }
   
   public int getContentType()
   {
      return contentType_;
   }
   
   public String getHtmlFile()
   {
      return htmlFile_;
   }
   
   @Override
   protected void dispatch(RSConnectActionEvent.Handler handler)
   {
      handler.onRSConnectAction(this);
   }

   @Override
   public GwtEvent.Type<RSConnectActionEvent.Handler> getAssociatedType()
   {
      return TYPE;
   }
   
   public static int ACTION_TYPE_DEPLOY = 0;
   public static int ACTION_TYPE_CONFIGURE = 1;
   
   private final String path_;
   private final RmdPreviewParams rmdPreview_;
   private final int action_;
   private final RSConnectDeploymentRecord fromPrevious_;
   private final int contentType_;
   private final String htmlFile_;
}
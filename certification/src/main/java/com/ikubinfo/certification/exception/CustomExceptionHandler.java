package com.ikubinfo.certification.exception;

import java.util.Iterator;
import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import org.apache.log4j.Logger;
import com.ikubinfo.certification.dao.impl.CertificationDaoImpl;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {
	 
	 private static Logger log = Logger.getLogger(CertificationDaoImpl.class);

	 private ExceptionHandler wrapped;
	  
	  
	  public CustomExceptionHandler(ExceptionHandler wrapped) {
	    this.wrapped = wrapped;
	  }
	 
	  @Override
	  public ExceptionHandler getWrapped() {
	    return wrapped;
	  }

	  @SuppressWarnings("rawtypes")
	  @Override
	  public void handle() throws FacesException {
	    Iterator iterator = getUnhandledExceptionQueuedEvents().iterator();
	    
	    while (iterator.hasNext()) {
	      ExceptionQueuedEvent event = (ExceptionQueuedEvent) iterator.next();
	      ExceptionQueuedEventContext context = (ExceptionQueuedEventContext)event.getSource();
	      Throwable throwable = context.getException();
	      FacesContext fc = FacesContext.getCurrentInstance();
	      System.out.println("--------------------------------+++---------------------------------");
	      System.out.println(throwable.getMessage());
	      try {
	    	  
	    	 if(throwable instanceof ELException || throwable instanceof FacesException ) {
	    		 if(throwable.getCause().getMessage().contains("org.hibernate.exception.JDBCConnectionException") ||
	    				 throwable.getCause().getMessage().contains("javax.faces.application.ViewExpiredException"))
	    		 {
	    			 log.fatal("Internal Server Error! The exception thrown is :"+throwable.getMessage());
	    			 NavigationHandler navigationHandler = fc.getApplication().getNavigationHandler();
 	   	          	 navigationHandler.handleNavigation(fc, null, "/error500?faces-redirect=true");
		   	         fc.renderResponse(); 
	    		 }
	    	 }
	    		  
	      } finally {
	          iterator.remove();
	      }
	    }
	    // Let the parent handle the rest
	    getWrapped().handle();
	  }
}


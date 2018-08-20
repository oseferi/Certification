package com.ikubinfo.certification.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikubinfo.certification.model.Status;
import com.ikubinfo.certification.model.Technology;
import com.ikubinfo.certification.service.StatusService;
import com.ikubinfo.certification.service.TechnologyService;

@Service
public class StatusConverter implements Converter {


	@Autowired
	StatusService statusService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
		if(submittedValue==null || submittedValue.isEmpty()) {
			return null;
		}
		try {
			Status status = statusService.findById(Integer.valueOf(submittedValue));
			return status; 
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error converting Status","Invalid status format. Error: "+e.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            e.printStackTrace();
            throw new ConverterException(msg);
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
		  if (modelValue == null) {
	            return "";
	        }
	       if (modelValue instanceof Status) {
	            return String.valueOf(((Status) modelValue).getId());
	        } else {
	            Throwable e = null;
				throw new ConverterException(new FacesMessage(String.format("%s is not a valid Status", modelValue)), e);
	        }
	}

}

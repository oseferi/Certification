package com.ikubinfo.certification.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikubinfo.certification.model.Certificate;
import com.ikubinfo.certification.model.Technology;
import com.ikubinfo.certification.service.CertificateService;

@Service
public class CertificateConverter implements Converter {
	@Autowired
	CertificateService certificateService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
		if(submittedValue==null || submittedValue.isEmpty()) {
			return null;
		}
		try {
			return certificateService.findById(Integer.valueOf(submittedValue));
			 
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error converting certificate","Invalid certificate format. Error: "+e.getMessage());
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
	       if (modelValue instanceof Certificate) {
	            return String.valueOf(((Certificate) modelValue).getId());
	        } else {
	            Throwable e = null;
				throw new ConverterException(new FacesMessage(String.format("%s is not a valid Certificate", modelValue)), e);
	        }
	}
	
	
}

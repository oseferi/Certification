package com.ikubinfo.certification.converter;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ikubinfo.certification.dao.TechnologyDao;
import com.ikubinfo.certification.model.Technology;
import com.ikubinfo.certification.service.TechnologyService;


@Service
public class TechnologyConverter implements Converter {

	@Autowired
	TechnologyService technologyService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
		if(submittedValue==null || submittedValue.isEmpty()) {
			return null;
		}
		try {
			Technology technology = technologyService.find(Integer.valueOf(submittedValue));
			return technology; 
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error converting technology","Invalid technology format. Error: "+e.getMessage());
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
	       if (modelValue instanceof Technology) {
	            return String.valueOf(((Technology) modelValue).getId());
	        } else {
	            Throwable e = null;
				throw new ConverterException(new FacesMessage(String.format("%s is not a valid Technology", modelValue)), e);
	        }
	}

}

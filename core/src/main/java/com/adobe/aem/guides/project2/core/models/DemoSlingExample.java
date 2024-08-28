package com.adobe.aem.guides.project2.core.models;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ResourcePath;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables=Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DemoSlingExample {
	@Inject
	private String firstname;
	@Inject
	private String lastname;
	@ValueMapValue
	@Default(values="2024-08-06")
	private String expirydate;
	@ValueMapValue
	private String dob;
	@ValueMapValue
	private String textfield;
	@ResourcePath(path="/content/evil/us/jcr:content")
	Resource pageResource;
	@ValueMapValue
	@Named("sling:resourceType")
	private String slingResourceType;
	public String getPagePath() {
		return pageResource.getPath();
	}
	public String getFirstName() {
		return firstname;
	}
	public String getLastName() {
		return lastname;
	}
	public String getExpiryDate() {
		return expirydate;
	}
	public String getDateOfBirth() {
		return dob;
	}
	public String getTextField() {
		return textfield;
	}
	public String getSlingResourceType() {
		return slingResourceType;
	}
}

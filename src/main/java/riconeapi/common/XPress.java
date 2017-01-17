/**
 * @author      Andrew Pieniezny <andrew.pieniezny@neric.org>
 * @version     1.5
 * @since       Jan 13, 2017
 * Filename		XPress.java
 */

package riconeapi.common;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;

import riconeapi.exceptions.AuthenticationException;
import riconeapi.models.xpress.XCalendarCollectionType;
import riconeapi.models.xpress.XCalendarType;
import riconeapi.models.xpress.XContactCollectionType;
import riconeapi.models.xpress.XContactType;
import riconeapi.models.xpress.XCourseCollectionType;
import riconeapi.models.xpress.XCourseType;
import riconeapi.models.xpress.XLeaCollectionType;
import riconeapi.models.xpress.XLeaType;
import riconeapi.models.xpress.XRosterCollectionType;
import riconeapi.models.xpress.XRosterType;
import riconeapi.models.xpress.XSchoolCollectionType;
import riconeapi.models.xpress.XSchoolType;
import riconeapi.models.xpress.XStaffCollectionType;
import riconeapi.models.xpress.XStaffType;
import riconeapi.models.xpress.XStudentCollectionType;
import riconeapi.models.xpress.XStudentType;

/**
 * Static class allowing access to xPress data model objects
 * 
 */
public class XPress
{
	private RestTemplate rt;
	private String baseApiUrl;

	public XPress(String baseApiUrl)
	{
		this.baseApiUrl = baseApiUrl;
		this.rt = new RestTemplate();
	}

	// #################### xLeas ####################
	/**
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return All Leas with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XLeaType> getXLeas(int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XLeaCollectionType> response = null;
		ResponseMulti<XLeaType> output = new ResponseMulti<XLeaType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
    		headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
    		headers.set("navigationPage", Integer.toString(navigationPage));
    		headers.set("navigationPageSize", Integer.toString(navigationPageSize));
    		//headers.set("Content-Type", "application/json");
			
    		HttpEntity<?> entity = new HttpEntity<Object>(headers);

    		response = rt.exchange(baseApiUrl + "xLeas", HttpMethod.GET, entity, XLeaCollectionType.class);
			
    		if(response.getBody() != null)
			{
    			output.setData(response.getBody().getXLea());
			}
    		output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	/**
	 * 
	 * @param opaqueMarker
	 * @return All Lea value changes from a given point
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XLeaType> getXLeas(String opaqueMarker) throws AuthenticationException
	{
		ResponseEntity<XLeaCollectionType> response = null;
		ResponseMulti<XLeaType> output = new ResponseMulti<XLeaType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
    		headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
    		//headers.set("Content-Type", "application/json");
    		
    		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseApiUrl)
    				.path("xLeas")
    				.queryParam("changesSinceMarker", opaqueMarker);
			
    		HttpEntity<?> entity = new HttpEntity<Object>(headers);

    		response = rt.exchange(builder.build().encode().toUriString(), HttpMethod.GET, entity, XLeaCollectionType.class);
			
    		if(response.getBody() != null)
			{
    			output.setData(response.getBody().getXLea());
			}
    		output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * @return All Leas
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XLeaType> getXLeas() throws AuthenticationException
	{
		ResponseEntity<XLeaCollectionType> response = null;
		ResponseMulti<XLeaType> output = new ResponseMulti<XLeaType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			////headers.set("Content-Type", "application/json");
			
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xLeas", HttpMethod.GET, entity, XLeaCollectionType.class);

			if(response.getBody() != null)
			{
    			output.setData(response.getBody().getXLea());

//    			ObjectMapper jsonMapper = new ObjectMapper();
//    			String json = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.getBody());
//    			output.setJson(json);

//    			JaxbAnnotationModule module = new JaxbAnnotationModule();
//    			ObjectMapper xmlMapper = new XmlMapper();
//    			xmlMapper.registerModule(module);
//    			String xml = xmlMapper.writeValueAsString(response.getBody());
//    			output.setXml(xml);
    			
//    			output.setXml(response.getBody().toString());

			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(((HttpStatusCodeException) e).getStatusText());
			output.setStatusCode(((HttpStatusCodeException) e).getStatusCode().value());
			output.setHeader(((HttpStatusCodeException) e).getResponseHeaders().toString());
		}

		return output;	
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Single Lea by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseSingle<XLeaType> getXLea(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XLeaType> response = null;
		ResponseSingle<XLeaType> output = new ResponseSingle<XLeaType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xLeas/{refId}", HttpMethod.GET, entity, XLeaType.class, refId);

			if(response.getBody() != null)
			{
    			output.setData(response.getBody());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Single Lea by refId
	 * @throws AuthenticationException 
	 */
	public ResponseSingle<XLeaType> getXLea(String refId) throws AuthenticationException
	{
		ResponseEntity<XLeaType> response = null;
		ResponseSingle<XLeaType> output = new ResponseSingle<XLeaType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xLeas/{refId}", HttpMethod.GET, entity, XLeaType.class, refId);
			
			if(response.getBody() != null)
			{
    			output.setData(response.getBody());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;	
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Leas associated to a specific School by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XLeaType> getXLeasByXSchool(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XLeaCollectionType> response = null;
		ResponseMulti<XLeaType> output = new ResponseMulti<XLeaType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xSchools/{refId}/xLeas", HttpMethod.GET, entity, XLeaCollectionType.class, refId);

			if(response.getBody() != null)
			{
    			output.setData(response.getBody().getXLea());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Leas associated to a specific School by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XLeaType> getXLeasByXSchool(String refId) throws AuthenticationException
	{
		ResponseEntity<XLeaCollectionType> response = null;
		ResponseMulti<XLeaType> output = new ResponseMulti<XLeaType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xSchools/{refId}/xLeas", HttpMethod.GET, entity, XLeaCollectionType.class, refId);

			if(response.getBody() != null)
			{
    			output.setData(response.getBody().getXLea());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Leas associated to a specific Roster by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XLeaType> getXLeasByXRoster(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XLeaCollectionType> response = null;
		ResponseMulti<XLeaType> output = new ResponseMulti<XLeaType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xRosters/{refId}/xLeas", HttpMethod.GET, entity, XLeaCollectionType.class, refId);

			if(response.getBody() != null)
			{
    			output.setData(response.getBody().getXLea());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Leas associated to a specific Roster by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XLeaType> getXLeasByXRoster(String refId) throws AuthenticationException
	{
		ResponseEntity<XLeaCollectionType> response = null;
		ResponseMulti<XLeaType> output = new ResponseMulti<XLeaType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xRosters/{refId}/xLeas", HttpMethod.GET, entity, XLeaCollectionType.class, refId);

			if(response.getBody() != null)
			{
    			output.setData(response.getBody().getXLea());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Leas associated to a specific Staff by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XLeaType> getXLeasByXStaff(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XLeaCollectionType> response = null;
		ResponseMulti<XLeaType> output = new ResponseMulti<XLeaType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStaffs/{refId}/xLeas", HttpMethod.GET, entity, XLeaCollectionType.class, refId);

			if(response.getBody() != null)
			{
    			output.setData(response.getBody().getXLea());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Leas associated to a specific Staff by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XLeaType> getXLeasByXStaff(String refId) throws AuthenticationException
	{
		ResponseEntity<XLeaCollectionType> response = null;
		ResponseMulti<XLeaType> output = new ResponseMulti<XLeaType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStaffs/{refId}/xLeas", HttpMethod.GET, entity, XLeaCollectionType.class, refId);

			if(response.getBody() != null)
			{
    			output.setData(response.getBody().getXLea());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Leas associated to a specific Student by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XLeaType> getXLeasByXStudent(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XLeaCollectionType> response = null;
		ResponseMulti<XLeaType> output = new ResponseMulti<XLeaType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStudents/{refId}/xLeas", HttpMethod.GET, entity, XLeaCollectionType.class, refId);

			if(response.getBody() != null)
			{
    			output.setData(response.getBody().getXLea());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Leas associated to a specific Student by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XLeaType> getXLeasByXStudent(String refId) throws AuthenticationException
	{
		ResponseEntity<XLeaCollectionType> response = null;
		ResponseMulti<XLeaType> output = new ResponseMulti<XLeaType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStudents/{refId}/xLeas", HttpMethod.GET, entity, XLeaCollectionType.class, refId);

			if(response.getBody() != null)
			{
    			output.setData(response.getBody().getXLea());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Leas associated to a specific Contact by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XLeaType> getXLeasByXContact(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XLeaCollectionType> response = null;
		ResponseMulti<XLeaType> output = new ResponseMulti<XLeaType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xContacts/{refId}/xLeas", HttpMethod.GET, entity, XLeaCollectionType.class, refId);

			if(response.getBody() != null)
			{
    			output.setData(response.getBody().getXLea());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Leas associated to a specific Contact by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XLeaType> getXLeasByXContact(String refId) throws AuthenticationException
	{
		ResponseEntity<XLeaCollectionType> response = null;
		ResponseMulti<XLeaType> output = new ResponseMulti<XLeaType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xContacts/{refId}/xLeas", HttpMethod.GET, entity, XLeaCollectionType.class, refId);

			if(response.getBody() != null)
			{
    			output.setData(response.getBody().getXLea());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	// #################### xSchools ####################
	/**
	 * 
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return All Schools with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XSchoolType> getXSchools(int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XSchoolCollectionType> response = null;
		ResponseMulti<XSchoolType> output = new ResponseMulti<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xSchools", HttpMethod.GET, entity, XSchoolCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXSchool());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * 
	 * @param opaqueMarker
	 * @return All School value changes from a given point
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XSchoolType> getXSchools(String opaqueMarker) throws AuthenticationException
	{
		ResponseEntity<XSchoolCollectionType> response = null;
		ResponseMulti<XSchoolType> output = new ResponseMulti<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
    		headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
    		//headers.set("Content-Type", "application/json");
    		
    		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseApiUrl)
    				.path("xSchools")
    				.queryParam("changesSinceMarker", opaqueMarker);
			
    		HttpEntity<?> entity = new HttpEntity<Object>(headers);

    		response = rt.exchange(builder.build().encode().toUriString(), HttpMethod.GET, entity, XSchoolCollectionType.class);
			
    		if(response.getBody() != null)
			{
    			output.setData(response.getBody().getXSchool());
			}
    		output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @return All Schools
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XSchoolType> getXSchools() throws AuthenticationException
	{
		ResponseEntity<XSchoolCollectionType> response = null;
		ResponseMulti<XSchoolType> output = new ResponseMulti<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xSchools", HttpMethod.GET, entity, XSchoolCollectionType.class);
			
			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXSchool());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;	
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Single School by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseSingle<XSchoolType> getXSchool(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XSchoolType> response = null;
		ResponseSingle<XSchoolType> output = new ResponseSingle<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");
			
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xSchools/{refId}", HttpMethod.GET, entity, XSchoolType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());	
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Single School by refId
	 * @throws AuthenticationException 
	 */
	public ResponseSingle<XSchoolType> getXSchool(String refId) throws AuthenticationException
	{
		ResponseEntity<XSchoolType> response = null;
		ResponseSingle<XSchoolType> output = new ResponseSingle<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xSchools/{refId}", HttpMethod.GET, entity, XSchoolType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Schools associated to a specific Lea by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XSchoolType> getXSchoolsByXLea(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XSchoolCollectionType> response = null;
		ResponseMulti<XSchoolType> output = new ResponseMulti<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xLeas/{refId}/xSchools", HttpMethod.GET, entity, XSchoolCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXSchool());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Schools associated to a specific Lea by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XSchoolType> getXSchoolsByXLea(String refId) throws AuthenticationException
	{
		ResponseEntity<XSchoolCollectionType> response = null;
		ResponseMulti<XSchoolType> output = new ResponseMulti<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xLeas/{refId}/xSchools", HttpMethod.GET, entity, XSchoolCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXSchool());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Schools associated to a specific Calendar by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XSchoolType> getXSchoolsByXCalendar(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XSchoolCollectionType> response = null;
		ResponseMulti<XSchoolType> output = new ResponseMulti<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xCalendars/{refId}/xSchools", HttpMethod.GET, entity, XSchoolCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXSchool());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Schools associated to a specific Calendar by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XSchoolType> getXSchoolsByXCalendar(String refId) throws AuthenticationException
	{
		ResponseEntity<XSchoolCollectionType> response = null;
		ResponseMulti<XSchoolType> output = new ResponseMulti<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xCalendars/{refId}/xSchools", HttpMethod.GET, entity, XSchoolCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXSchool());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Schools associated to a specific Course by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XSchoolType> getXSchoolsByXCourse(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XSchoolCollectionType> response = null;
		ResponseMulti<XSchoolType> output = new ResponseMulti<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{	
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xCourses/{refId}/xSchools", HttpMethod.GET, entity, XSchoolCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXSchool());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Schools associated to a specific Course by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XSchoolType> getXSchoolsByXCourse(String refId) throws AuthenticationException
	{
		ResponseEntity<XSchoolCollectionType> response = null;
		ResponseMulti<XSchoolType> output = new ResponseMulti<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xCourses/{refId}/xSchools", HttpMethod.GET, entity, XSchoolCollectionType.class, refId);
			
			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXSchool());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Schools associated to a specific Roster by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XSchoolType> getXSchoolsByXRoster(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XSchoolCollectionType> response = null;
		ResponseMulti<XSchoolType> output = new ResponseMulti<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xRosters/{refId}/xSchools", HttpMethod.GET, entity, XSchoolCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXSchool());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Schools associated to a specific Roster by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XSchoolType> getXSchoolsByXRoster(String refId) throws AuthenticationException
	{
		ResponseEntity<XSchoolCollectionType> response = null;
		ResponseMulti<XSchoolType> output = new ResponseMulti<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xRosters/{refId}/xSchools", HttpMethod.GET, entity, XSchoolCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXSchool());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Schools associated to a specific Staff by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XSchoolType> getXSchoolsByXStaff(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XSchoolCollectionType> response = null;
		ResponseMulti<XSchoolType> output = new ResponseMulti<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStaffs/{refId}/xSchools", HttpMethod.GET, entity, XSchoolCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXSchool());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Schools associated to a specific Staff by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XSchoolType> getXSchoolsByXStaff(String refId) throws AuthenticationException
	{
		ResponseEntity<XSchoolCollectionType> response = null;
		ResponseMulti<XSchoolType> output = new ResponseMulti<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStaffs/{refId}/xSchools", HttpMethod.GET, entity, XSchoolCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXSchool());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Schools associated to a specific Student by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XSchoolType> getXSchoolsByXStudent(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XSchoolCollectionType> response = null;
		ResponseMulti<XSchoolType> output = new ResponseMulti<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStudents/{refId}/xSchools", HttpMethod.GET, entity, XSchoolCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXSchool());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Schools associated to a specific Student by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XSchoolType> getXSchoolsByXStudent(String refId) throws AuthenticationException
	{
		ResponseEntity<XSchoolCollectionType> response = null;
		ResponseMulti<XSchoolType> output = new ResponseMulti<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStudents/{refId}/xSchools", HttpMethod.GET, entity, XSchoolCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXSchool());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Schools associated to a specific Contact by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XSchoolType> getXSchoolsByXContact(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XSchoolCollectionType> response = null;
		ResponseMulti<XSchoolType> output = new ResponseMulti<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xContacts/{refId}/xSchools", HttpMethod.GET, entity, XSchoolCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXSchool());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Schools associated to a specific Contact by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XSchoolType> getXSchoolsByXContact(String refId) throws AuthenticationException
	{
		ResponseEntity<XSchoolCollectionType> response = null;
		ResponseMulti<XSchoolType> output = new ResponseMulti<XSchoolType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xContacts/{refId}/xSchools", HttpMethod.GET, entity, XSchoolCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXSchool());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	// #################### xCalendars ####################
	/**
	 * 
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return All Calendars with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XCalendarType> getXCalendars(int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XCalendarCollectionType> response = null;
		ResponseMulti<XCalendarType> output = new ResponseMulti<XCalendarType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xCalendars", HttpMethod.GET, entity, XCalendarCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXCalendar());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * 
	 * @param opaqueMarker
	 * @return All Calendar value changes from a given point
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XCalendarType> getXCalendars(String opaqueMarker) throws AuthenticationException
	{
		ResponseEntity<XCalendarCollectionType> response = null;
		ResponseMulti<XCalendarType> output = new ResponseMulti<XCalendarType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseApiUrl)
    				.path("xCalendars")
    				.queryParam("changesSinceMarker", opaqueMarker);

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(builder.build().encode().toUriString(), HttpMethod.GET, entity, XCalendarCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXCalendar());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @return All Calendars
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XCalendarType> getXCalendars() throws AuthenticationException
	{
		ResponseEntity<XCalendarCollectionType> response = null;
		ResponseMulti<XCalendarType> output = new ResponseMulti<XCalendarType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xCalendars", HttpMethod.GET, entity, XCalendarCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXCalendar());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Single Calendar by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseSingle<XCalendarType> getXCalendar(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XCalendarType> response = null;
		ResponseSingle<XCalendarType> output = new ResponseSingle<XCalendarType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xCalendars/{refId}", HttpMethod.GET, entity, XCalendarType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Single Calendar by refId
	 * @throws AuthenticationException 
	 */
	public ResponseSingle<XCalendarType> getXCalendar(String refId) throws AuthenticationException
	{
		ResponseEntity<XCalendarType> response = null;
		ResponseSingle<XCalendarType> output = new ResponseSingle<XCalendarType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xCalendars/{refId}", HttpMethod.GET, entity, XCalendarType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Calendars associated to a specific Lea by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XCalendarType> getXCalendarsByXLea(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XCalendarCollectionType> response = null;
		ResponseMulti<XCalendarType> output = new ResponseMulti<XCalendarType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xLeas/{refId}/xCalendars", HttpMethod.GET, entity, XCalendarCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXCalendar());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Calendars associated to a specific Lea by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XCalendarType> getXCalendarsByXLea(String refId) throws AuthenticationException
	{
		ResponseEntity<XCalendarCollectionType> response = null;
		ResponseMulti<XCalendarType> output = new ResponseMulti<XCalendarType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xLeas/{refId}/xCalendars", HttpMethod.GET, entity, XCalendarCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXCalendar());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Calendars associated to a specific School by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XCalendarType> getXCalendarsByXSchool(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XCalendarCollectionType> response = null;
		ResponseMulti<XCalendarType> output = new ResponseMulti<XCalendarType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xSchools/{refId}/xCalendars", HttpMethod.GET, entity, XCalendarCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXCalendar());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Calendars associated to a specific School by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XCalendarType> getXCalendarsByXSchool(String refId) throws AuthenticationException
	{
		ResponseEntity<XCalendarCollectionType> response = null;
		ResponseMulti<XCalendarType> output = new ResponseMulti<XCalendarType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xSchools/{refId}/xCalendars", HttpMethod.GET, entity, XCalendarCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXCalendar());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	// #################### xCourses ####################
	/**
	 * 
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return All Courses with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XCourseType> getXCourses(int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XCourseCollectionType> response = null;
		ResponseMulti<XCourseType> output = new ResponseMulti<XCourseType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xCourses", HttpMethod.GET, entity, XCourseCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXCourse());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * 
	 * @param opaqueMarker
	 * @return All Course value changes from a given point
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XCourseType> getXCourses(String opaqueMarker) throws AuthenticationException
	{
		ResponseEntity<XCourseCollectionType> response = null;
		ResponseMulti<XCourseType> output = new ResponseMulti<XCourseType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseApiUrl)
    				.path("xCourses")
    				.queryParam("changesSinceMarker", opaqueMarker);
			
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(builder.build().encode().toUriString(), HttpMethod.GET, entity, XCourseCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXCourse());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @return All Courses
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XCourseType> getXCourses() throws AuthenticationException
	{
		ResponseEntity<XCourseCollectionType> response = null;
		ResponseMulti<XCourseType> output = new ResponseMulti<XCourseType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xCourses", HttpMethod.GET, entity, XCourseCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXCourse());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Single Course by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseSingle<XCourseType> getXCourse(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XCourseType> response = null;
		ResponseSingle<XCourseType> output = new ResponseSingle<XCourseType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xCourses/{refId}", HttpMethod.GET, entity, XCourseType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Single Course by refId
	 * @throws AuthenticationException 
	 */
	public ResponseSingle<XCourseType> getXCourse(String refId) throws AuthenticationException
	{
		ResponseEntity<XCourseType> response = null;
		ResponseSingle<XCourseType> output = new ResponseSingle<XCourseType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xCourses/{refId}", HttpMethod.GET, entity, XCourseType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Courses associated to a specific Lea by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XCourseType> getXCoursesByXLea(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XCourseCollectionType> response = null;
		ResponseMulti<XCourseType> output = new ResponseMulti<XCourseType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xLeas/{refId}/xCourses", HttpMethod.GET, entity, XCourseCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXCourse());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Courses associated to a specific Lea by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XCourseType> getXCoursesByXLea(String refId) throws AuthenticationException
	{
		ResponseEntity<XCourseCollectionType> response = null;
		ResponseMulti<XCourseType> output = new ResponseMulti<XCourseType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xLeas/{refId}/xCourses", HttpMethod.GET, entity, XCourseCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXCourse());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Courses associated to a specific School by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XCourseType> getXCoursesByXSchool(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XCourseCollectionType> response = null;
		ResponseMulti<XCourseType> output = new ResponseMulti<XCourseType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xSchools/{refId}/xCourses", HttpMethod.GET, entity, XCourseCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXCourse());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Courses associated to a specific School by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XCourseType> getXCoursesByXSchool(String refId) throws AuthenticationException
	{
		ResponseEntity<XCourseCollectionType> response = null;
		ResponseMulti<XCourseType> output = new ResponseMulti<XCourseType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xSchools/{refId}/xCourses", HttpMethod.GET, entity, XCourseCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXCourse());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Courses associated to a specific Roster by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XCourseType> getXCoursesByXRoster(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XCourseCollectionType> response = null;
		ResponseMulti<XCourseType> output = new ResponseMulti<XCourseType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xRosters/{refId}/xCourses", HttpMethod.GET, entity, XCourseCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXCourse());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Courses associated to a specific Roster by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XCourseType> getXCoursesByXRoster(String refId) throws AuthenticationException
	{
		ResponseEntity<XCourseCollectionType> response = null;
		ResponseMulti<XCourseType> output = new ResponseMulti<XCourseType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xRosters/{refId}/xCourses", HttpMethod.GET, entity, XCourseCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXCourse());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	// #################### xRosters ####################
	/**
	 * 
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return All Rosters with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XRosterType> getXRosters(int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XRosterCollectionType> response = null;
		ResponseMulti<XRosterType> output = new ResponseMulti<XRosterType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xRosters", HttpMethod.GET, entity, XRosterCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXRoster());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * 
	 * @param opaqueMarker
	 * @return All Roster value changes from a given point
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XRosterType> getXRosters(String opaqueMarker) throws AuthenticationException
	{
		ResponseEntity<XRosterCollectionType> response = null;
		ResponseMulti<XRosterType> output = new ResponseMulti<XRosterType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseApiUrl)
    				.path("xRosters")
    				.queryParam("changesSinceMarker", opaqueMarker);
			
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(builder.build().encode().toUriString(), HttpMethod.GET, entity, XRosterCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXRoster());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @return All Rosters
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XRosterType> getXRosters() throws AuthenticationException
	{
		ResponseEntity<XRosterCollectionType> response = null;
		ResponseMulti<XRosterType> output = new ResponseMulti<XRosterType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xRosters", HttpMethod.GET, entity, XRosterCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXRoster());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Single Roster by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseSingle<XRosterType> getXRoster(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XRosterType> response = null;
		ResponseSingle<XRosterType> output = new ResponseSingle<XRosterType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xRosters/{refId}", HttpMethod.GET, entity, XRosterType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());	
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Single Roster by refId
	 * @throws AuthenticationException 
	 */
	public ResponseSingle<XRosterType> getXRoster(String refId) throws AuthenticationException
	{
		ResponseEntity<XRosterType> response = null;
		ResponseSingle<XRosterType> output = new ResponseSingle<XRosterType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xRosters/{refId}", HttpMethod.GET, entity, XRosterType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Rosters associated to a specific Lea by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XRosterType> getXRostersByXLea(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XRosterCollectionType> response = null;
		ResponseMulti<XRosterType> output = new ResponseMulti<XRosterType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xLeas/{refId}/xRosters", HttpMethod.GET, entity, XRosterCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXRoster());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Rosters associated to a specific Lea by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XRosterType> getXRostersByXLea(String refId) throws AuthenticationException
	{
		ResponseEntity<XRosterCollectionType> response = null;
		ResponseMulti<XRosterType> output = new ResponseMulti<XRosterType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xLeas/{refId}/xRosters", HttpMethod.GET, entity, XRosterCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXRoster());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Rosters associated to a specific School by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XRosterType> getXRostersByXSchool(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XRosterCollectionType> response = null;
		ResponseMulti<XRosterType> output = new ResponseMulti<XRosterType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xSchools/{refId}/xRosters", HttpMethod.GET, entity, XRosterCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXRoster());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Rosters associated to a specific School by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XRosterType> getXRostersByXSchool(String refId) throws AuthenticationException
	{
		ResponseEntity<XRosterCollectionType> response = null;
		ResponseMulti<XRosterType> output = new ResponseMulti<XRosterType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xSchools/{refId}/xRosters", HttpMethod.GET, entity, XRosterCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXRoster());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Rosters associated to a specific Course by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XRosterType> getXRostersByXCourse(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XRosterCollectionType> response = null;
		ResponseMulti<XRosterType> output = new ResponseMulti<XRosterType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xCourses/{refId}/xRosters", HttpMethod.GET, entity, XRosterCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXRoster());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Rosters associated to a specific Course by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XRosterType> getXRostersByXCourse(String refId) throws AuthenticationException
	{
		ResponseEntity<XRosterCollectionType> response = null;
		ResponseMulti<XRosterType> output = new ResponseMulti<XRosterType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xCourses/{refId}/xRosters", HttpMethod.GET, entity, XRosterCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXRoster());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Rosters associated to a specific Staff by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XRosterType> getXRostersByXStaff(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XRosterCollectionType> response = null;
		ResponseMulti<XRosterType> output = new ResponseMulti<XRosterType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStaffs/{refId}/xRosters", HttpMethod.GET, entity, XRosterCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXRoster());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Rosters associated to a specific Staff by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XRosterType> getXRostersByXStaff(String refId) throws AuthenticationException
	{
		ResponseEntity<XRosterCollectionType> response = null;
		ResponseMulti<XRosterType> output = new ResponseMulti<XRosterType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStaffs/{refId}/xRosters", HttpMethod.GET, entity, XRosterCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXRoster());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Rosters associated to a specific Student by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XRosterType> getXRostersByXStudent(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XRosterCollectionType> response = null;
		ResponseMulti<XRosterType> output = new ResponseMulti<XRosterType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStudents/{refId}/xRosters", HttpMethod.GET, entity, XRosterCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXRoster());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Rosters associated to a specific Student by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XRosterType> getXRostersByXStudent(String refId) throws AuthenticationException
	{
		ResponseEntity<XRosterCollectionType> response = null;
		ResponseMulti<XRosterType> output = new ResponseMulti<XRosterType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStudents/{refId}/xRosters", HttpMethod.GET, entity, XRosterCollectionType.class, refId);
			
			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXRoster());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	// #################### xStaffs ####################
	/**
	 * 
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return All Staffs with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStaffType> getXStaffs(int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XStaffCollectionType> response = null;
		ResponseMulti<XStaffType> output = new ResponseMulti<XStaffType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStaffs", HttpMethod.GET, entity, XStaffCollectionType.class);
			
			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStaff());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * 
	 * @param opaqueMarker
	 * @return All Staff value changes from a given point
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStaffType> getXStaffs(String opaqueMarker) throws AuthenticationException
	{
		ResponseEntity<XStaffCollectionType> response = null;
		ResponseMulti<XStaffType> output = new ResponseMulti<XStaffType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseApiUrl)
    				.path("xStaffs")
    				.queryParam("changesSinceMarker", opaqueMarker);

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(builder.build().encode().toUriString(), HttpMethod.GET, entity, XStaffCollectionType.class);
			
			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStaff());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @return All Staffs
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStaffType> getXStaffs() throws AuthenticationException
	{
		ResponseEntity<XStaffCollectionType> response = null;
		ResponseMulti<XStaffType> output = new ResponseMulti<XStaffType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStaffs", HttpMethod.GET, entity, XStaffCollectionType.class);
			
			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStaff());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Single Staffs by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseSingle<XStaffType> getXStaff(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XStaffType> response = null;
		ResponseSingle<XStaffType> output = new ResponseSingle<XStaffType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStaffs/{refId}", HttpMethod.GET, entity, XStaffType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Single Staffs by refId
	 * @throws AuthenticationException 
	 */
	public ResponseSingle<XStaffType> getXStaff(String refId) throws AuthenticationException
	{
		ResponseEntity<XStaffType> response = null;
		ResponseSingle<XStaffType> output = new ResponseSingle<XStaffType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStaffs/{refId}", HttpMethod.GET, entity, XStaffType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Staffs associated to a specific Lea by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStaffType> getXStaffsByXLea(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XStaffCollectionType> response = null;
		ResponseMulti<XStaffType> output = new ResponseMulti<XStaffType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xLeas/{refId}/xStaffs", HttpMethod.GET, entity, XStaffCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStaff());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Staffs associated to a specific Lea by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStaffType> getXStaffsByXLea(String refId) throws AuthenticationException
	{
		ResponseEntity<XStaffCollectionType> response = null;
		ResponseMulti<XStaffType> output = new ResponseMulti<XStaffType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xLeas/{refId}/xStaffs", HttpMethod.GET, entity, XStaffCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStaff());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Staffs associated to a specific School by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStaffType> getXStaffsByXSchool(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XStaffCollectionType> response = null;
		ResponseMulti<XStaffType> output = new ResponseMulti<XStaffType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xSchools/{refId}/xStaffs", HttpMethod.GET, entity, XStaffCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStaff());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Staffs associated to a specific School by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStaffType> getXStaffsByXSchool(String refId) throws AuthenticationException
	{
		ResponseEntity<XStaffCollectionType> response = null;
		ResponseMulti<XStaffType> output = new ResponseMulti<XStaffType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xSchools/{refId}/xStaffs", HttpMethod.GET, entity, XStaffCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStaff());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Staffs associated to a specific Course by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStaffType> getXStaffsByXCourse(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XStaffCollectionType> response = null;
		ResponseMulti<XStaffType> output = new ResponseMulti<XStaffType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xCourses/{refId}/xStaffs", HttpMethod.GET, entity, XStaffCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStaff());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Staffs associated to a specific Course by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStaffType> getXStaffsByXCourse(String refId) throws AuthenticationException
	{
		ResponseEntity<XStaffCollectionType> response = null;
		ResponseMulti<XStaffType> output = new ResponseMulti<XStaffType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xCourses/{refId}/xStaffs", HttpMethod.GET, entity, XStaffCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStaff());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Staffs associated to a specific Roster by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStaffType> getXStaffsByXRoster(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XStaffCollectionType> response = null;
		ResponseMulti<XStaffType> output = new ResponseMulti<XStaffType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xRosters/{refId}/xStaffs", HttpMethod.GET, entity, XStaffCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStaff());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Staffs associated to a specific Roster by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStaffType> getXStaffsByXRoster(String refId) throws AuthenticationException
	{
		ResponseEntity<XStaffCollectionType> response = null;
		ResponseMulti<XStaffType> output = new ResponseMulti<XStaffType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			//headers.set("Content-Type", "application/json");
			
			response = rt.exchange(baseApiUrl + "xRosters/{refId}/xStaffs", HttpMethod.GET, entity, XStaffCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStaff());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Staffs associated to a specific Student by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStaffType> getXStaffsByXStudent(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XStaffCollectionType> response = null;
		ResponseMulti<XStaffType> output = new ResponseMulti<XStaffType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStudents/{refId}/xStaffs", HttpMethod.GET, entity, XStaffCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStaff());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Staffs associated to a specific Student by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStaffType> getXStaffsByXStudent(String refId) throws AuthenticationException
	{
		ResponseEntity<XStaffCollectionType> response = null;
		ResponseMulti<XStaffType> output = new ResponseMulti<XStaffType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			//headers.set("Content-Type", "application/json");
			
			response = rt.exchange(baseApiUrl + "xStudents/{refId}/xStaffs", HttpMethod.GET, entity, XStaffCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStaff());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	// #################### xStudents ####################
	/**
	 * 
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return All Students with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStudentType> getXStudents(int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XStudentCollectionType> response = null;
		ResponseMulti<XStudentType> output = new ResponseMulti<XStudentType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStudents", HttpMethod.GET, entity, XStudentCollectionType.class);
			
			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStudent());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * 
	 * @param opaqueMarker
	 * @return All Student value changes from a given point
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStudentType> getXStudents(String opaqueMarker) throws AuthenticationException
	{
		ResponseEntity<XStudentCollectionType> response = null;
		ResponseMulti<XStudentType> output = new ResponseMulti<XStudentType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseApiUrl)
    				.path("xStudents")
    				.queryParam("changesSinceMarker", opaqueMarker);
			
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(builder.build().encode().toUriString(), HttpMethod.GET, entity, XStudentCollectionType.class);
			
			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStudent());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @return All Students
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStudentType> getXStudents() throws AuthenticationException
	{
		ResponseEntity<XStudentCollectionType> response = null;
		ResponseMulti<XStudentType> output = new ResponseMulti<XStudentType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStudents", HttpMethod.GET, entity, XStudentCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStudent());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	} 
	
	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Single Student by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseSingle<XStudentType> getXStudent(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XStudentType> response = null;
		ResponseSingle<XStudentType> output = new ResponseSingle<XStudentType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStudents/{refId}", HttpMethod.GET, entity, XStudentType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Single Student by refId
	 * @throws AuthenticationException 
	 */
	public ResponseSingle<XStudentType> getXStudent(String refId) throws AuthenticationException
	{
		ResponseEntity<XStudentType> response = null;
		ResponseSingle<XStudentType> output = new ResponseSingle<XStudentType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStudents/{refId}", HttpMethod.GET, entity, XStudentType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Students associated to a specific Lea by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStudentType> getXStudentsByXLea(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XStudentCollectionType> response = null;
		ResponseMulti<XStudentType> output = new ResponseMulti<XStudentType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xLeas/{refId}/xStudents", HttpMethod.GET, entity, XStudentCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStudent());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Students associated to a specific Lea by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStudentType> getXStudentsByXLea(String refId) throws AuthenticationException
	{
		ResponseEntity<XStudentCollectionType> response = null;
		ResponseMulti<XStudentType> output = new ResponseMulti<XStudentType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xLeas/{refId}/xStudents", HttpMethod.GET, entity, XStudentCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStudent());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Students associated to a specific School by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStudentType> getXStudentsByXSchool(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XStudentCollectionType> response = null;
		ResponseMulti<XStudentType> output = new ResponseMulti<XStudentType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xSchools/{refId}/xStudents", HttpMethod.GET, entity, XStudentCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStudent());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Students associated to a specific School by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStudentType> getXStudentsByXSchool(String refId) throws AuthenticationException
	{
		ResponseEntity<XStudentCollectionType> response = null;
		ResponseMulti<XStudentType> output = new ResponseMulti<XStudentType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xSchools/{refId}/xStudents", HttpMethod.GET, entity, XStudentCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStudent());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Students associated to a specific Roster by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStudentType> getXStudentsByXRoster(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XStudentCollectionType> response = null;
		ResponseMulti<XStudentType> output = new ResponseMulti<XStudentType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xRosters/{refId}/xStudents", HttpMethod.GET, entity, XStudentCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStudent());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Students associated to a specific Roster by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStudentType> getXStudentsByXRoster(String refId) throws AuthenticationException
	{
		ResponseEntity<XStudentCollectionType> response = null;
		ResponseMulti<XStudentType> output = new ResponseMulti<XStudentType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xRosters/{refId}/xStudents", HttpMethod.GET, entity, XStudentCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStudent());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Students associated to a specific Staff by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStudentType> getXStudentsByXStaff(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XStudentCollectionType> response = null;
		ResponseMulti<XStudentType> output = new ResponseMulti<XStudentType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStaffs/{refId}/xStudents", HttpMethod.GET, entity, XStudentCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStudent());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Students associated to a specific Staff by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStudentType> getXStudentsByXStaff(String refId) throws AuthenticationException
	{
		ResponseEntity<XStudentCollectionType> response = null;
		ResponseMulti<XStudentType> output = new ResponseMulti<XStudentType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStaffs/{refId}/xStudents", HttpMethod.GET, entity, XStudentCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStudent());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Students associated to a specific Contact by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStudentType> getXStudentsByXContact(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XStudentCollectionType> response = null;
		ResponseMulti<XStudentType> output = new ResponseMulti<XStudentType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xContacts/{refId}/xStudents", HttpMethod.GET, entity, XStudentCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStudent());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Students associated to a specific Contact by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStudentType> getXStudentsByXContact(String refId) throws AuthenticationException
	{
		ResponseEntity<XStudentCollectionType> response = null;
		ResponseMulti<XStudentType> output = new ResponseMulti<XStudentType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xContacts/{refId}/xStudents", HttpMethod.GET, entity, XStudentCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStudent());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	// #################### xContacts ####################
	/**
	 * 
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return All Contacts with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XContactType> getXContacts(int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XContactCollectionType> response = null;
		ResponseMulti<XContactType> output = new ResponseMulti<XContactType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xContacts", HttpMethod.GET, entity, XContactCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXContact());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * 
	 * @param opaqueMarker
	 * @return All Contact value changes from a given point
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XContactType> getXContacts(String opaqueMarker) throws AuthenticationException
	{
		ResponseEntity<XContactCollectionType> response = null;
		ResponseMulti<XContactType> output = new ResponseMulti<XContactType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseApiUrl)
    				.path("xContacts")
    				.queryParam("changesSinceMarker", opaqueMarker);
			
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(builder.build().encode().toUriString(), HttpMethod.GET, entity, XContactCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXContact());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @return All Contacts
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XContactType> getXContacts() throws AuthenticationException
	{
		ResponseEntity<XContactCollectionType> response = null;
		ResponseMulti<XContactType> output = new ResponseMulti<XContactType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xContacts", HttpMethod.GET, entity, XContactCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXContact());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Single Contact by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseSingle<XContactType> getXContact(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XContactType> response = null;
		ResponseSingle<XContactType> output = new ResponseSingle<XContactType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xContacts/{refId}", HttpMethod.GET, entity, XContactType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Single Contact by refId
	 * @throws AuthenticationException 
	 */
	public ResponseSingle<XContactType> getXContact(String refId) throws AuthenticationException
	{
		ResponseEntity<XContactType> response = null;
		ResponseSingle<XContactType> output = new ResponseSingle<XContactType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xContacts/{refId}", HttpMethod.GET, entity, XContactType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Contacts associated to a specific Lea by refId wtih paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XContactType> getXContactsByXLea(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XContactCollectionType> response = null;
		ResponseMulti<XContactType> output = new ResponseMulti<XContactType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xLeas/{refId}/xContacts", HttpMethod.GET, entity, XContactCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXContact());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Contacts associated to a specific Lea by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XContactType> getXContactsByXLea(String refId) throws AuthenticationException
	{
		ResponseEntity<XContactCollectionType> response = null;
		ResponseMulti<XContactType> output = new ResponseMulti<XContactType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xLeas/{refId}/xContacts", HttpMethod.GET, entity, XContactCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXContact());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Contacts associated to a specific School by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XContactType> getXContactsByXSchool(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XContactCollectionType> response = null;
		ResponseMulti<XContactType> output = new ResponseMulti<XContactType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xSchools/{refId}/xContacts", HttpMethod.GET, entity, XContactCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXContact());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Contacts associated to a specific School by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XContactType> getXContactsByXSchool(String refId) throws AuthenticationException
	{
		ResponseEntity<XContactCollectionType> response = null;
		ResponseMulti<XContactType> output = new ResponseMulti<XContactType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xSchools/{refId}/xContacts", HttpMethod.GET, entity, XContactCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXContact());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @param navigationPage
	 * @param navigationPageSize
	 * @return Contacts associated to a specific Student by refId with paging
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XContactType> getXContactsByXStudent(String refId, int navigationPage, int navigationPageSize) throws AuthenticationException
	{
		ResponseEntity<XContactCollectionType> response = null;
		ResponseMulti<XContactType> output = new ResponseMulti<XContactType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			headers.set("navigationPage", Integer.toString(navigationPage));
			headers.set("navigationPageSize", Integer.toString(navigationPageSize));
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStudents/{refId}/xContacts", HttpMethod.GET, entity, XContactCollectionType.class, refId);
			
			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXContact());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	/**
	 * 
	 * @param refId
	 * @return Contacts associated to a specific Student by refId
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XContactType> getXContactsByXStudent(String refId) throws AuthenticationException
	{
		ResponseEntity<XContactCollectionType> response = null;
		ResponseMulti<XContactType> output = new ResponseMulti<XContactType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(baseApiUrl + "xStudents/{refId}/xContacts", HttpMethod.GET, entity, XContactCollectionType.class, refId);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXContact());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}

	// #################### navigationLastPage ####################
	/**
	 * Enumerator used to retrieve service path object for max page size
	 *
	 */
	public enum ServicePath
	{
		GETXLEAS("xLeas"), GETXLEASBYXSCHOOL("xSchools/{refId}/xLeas"), GETXLEASBYXROSTER("xRosters/{refId}/xLeas"), GETXLEASBYXSTAFF("xStaffs/{refId}/xLeas"), GETXLEASBYXSTUDENT("xStudents/{refId}/xLeas"), GETXLEASBYXCONTACT("xContacts/{refId}/xLeas"), GETXSCHOOLS("xSchools"), GETXSCHOOLSBYXLEA("xLeas/{refId}/xSchools"), GETXSCHOOLSBYXCALENDAR("xCalendars/{refId}/xSchools"), GETXSCHOOLSBYXCOURSE(
				"xCourses/{refId}/xSchools"), GETXSCHOOLSBYXROSTER("xRosters/{refId}/xSchools"), GETXSCHOOLSBYXSTAFF("xStaffs/{refId}/xSchools"), GETXSCHOOLSBYXSTUDENT("xStudents/{refId}/xSchools"), GETXSCHOOLSBYXCONTACT("xContacts/{refId}/xSchools"), GETXCALENDARS("xCalendars"), GETXCALENDARSBYXLEA("xLeas/{refId}/xCalendars"), GETXCALENDARSBYXSCHOOL(
				"xSchools/{refId}/xCalendars"), GETXCOURSES("xCourses"), GETXCOURSESBYXLEA("xLeas/{refId}/xCourses"), GETXCOURSESBYXSCHOOL("xSchools/{refId}/xCourses"), GETXCOURSESBYXROSTER("xRosters/{refId}/xCourses"), GETXROSTERS("xRosters"), GETXROSTERSBYXLEA("xLeas/{refId}/xRosters"), GETXROSTERSBYXSCHOOL("xSchools/{refId}/xRosters"), GETXROSTERSBYXCOURSE("xCourses/{refId}/xRosters"), GETXROSTERSBYXSTAFF(
				"xStaffs/{refId}/xRosters"), GETXROSTERSBYXSTUDENT("xStudents/{refId}/xRosters"), GETXSTAFFS("xStaffs"), GETXSTAFFSBYXLEA("xLeas/{refId}/xStaffs"), GETXSTAFFSBYXSCHOOL("xSchools/{refId}/xStaffs"), GETXSTAFFSBYXCOURSE("xCourses/{refId}/xStaffs"), GETXSTAFFSBYXROSTER(
				"xRosters/{refId}/xStaffs"), GETXSTAFFSBYXSTUDENT("xStudents/{refId}/xStaffs"), GETXSTUDENTS("xStudents"), GETXSTUDENTSBYXLEA("xLeas/{refId}/xStudents"), GETXSTUDENTSBYXSCHOOL("xSchools/{refId}/xStudents"), GETXSTUDENTSBYXROSTER("xRosters/{refId}/xStudents"), GETXSTUDENTSBYXSTAFF("xStaffs/{refId}/xStudents"), GETXSTUDENTSBYXCONTACT(
				"xContacts/{refId}/xStudents"), GETXCONTACTS("xContacts"), GETXCONTACTSBYXLEA("xLeas/{refId}/xContacts"), GETXCONTACTSBYXSCHOOL("xSchools/{refId}/xContacts"), GETXCONTACTSBYXSTUDENT("xStudents/{refId}/xContacts");

		private String value;

		ServicePath(String value)
		{
			this.value = value;
		}

		public String getValue()
		{
			return value;
		}
	}

	/**
	 * 
	 * @param navigationPageSize
	 * @param p
	 * @param refId
	 * @return Max page value for specified service path object
	 */
	public int getLastPage(int navigationPageSize, ServicePath p, String refId)
	{
		int navigationPage = 1;
		Integer navigationLastPage = null;
		
		try
		{	
			if (p.name().equals("GETXLEASBYXSCHOOL") || p.name().equals("GETXLEASBYXROSTER") || p.name().equals("GETXLEASBYXSTAFF") || p.name().equals("GETXLEASBYXSTUDENT") || p.name().equals("GETXLEASBYXCONTACT"))
			{
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
				headers.set("navigationPage", Integer.toString(navigationPage));
				headers.set("navigationPageSize", Integer.toString(navigationPageSize));
				//headers.set("Content-Type", "application/json");

				HttpEntity<?> entity = new HttpEntity<Object>(headers);
				
				ResponseEntity<XLeaCollectionType> response = rt.exchange(baseApiUrl + p.value, HttpMethod.GET, entity, XLeaCollectionType.class, refId);

				navigationLastPage = Integer.parseInt(response.getHeaders().getFirst("navigationLastPage"));	
			}
			else if (p.name().equals("GETXSCHOOLSBYXLEA") || p.name().equals("GETXSCHOOLSBYXCALENDAR") || p.name().equals("GETXSCHOOLSBYXCOURSE") || p.name().equals("GETXSCHOOLSBYXROSTER") || p.name().equals("GETXSCHOOLSBYXSTAFF") || p.name().equals("GETXSCHOOLSBYXSTUDENT")
					|| p.name().equals("GETXSCHOOLSBYXCONTACT"))
			{
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
				headers.set("navigationPage", Integer.toString(navigationPage));
				headers.set("navigationPageSize", Integer.toString(navigationPageSize));
				//headers.set("Content-Type", "application/json");

				HttpEntity<?> entity = new HttpEntity<Object>(headers);
				
				ResponseEntity<XSchoolCollectionType> response = rt.exchange(baseApiUrl + p.value, HttpMethod.GET, entity, XSchoolCollectionType.class, refId);

				navigationLastPage = Integer.parseInt(response.getHeaders().getFirst("navigationLastPage"));
			}
			else if (p.name().equals("GETXCALENDARSBYXLEA") || p.name().equals("GETXCALENDARSBYXSCHOOL"))
			{
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
				headers.set("navigationPage", Integer.toString(navigationPage));
				headers.set("navigationPageSize", Integer.toString(navigationPageSize));
				//headers.set("Content-Type", "application/json");

				HttpEntity<?> entity = new HttpEntity<Object>(headers);
				
				ResponseEntity<XCalendarCollectionType> response = rt.exchange(baseApiUrl + p.value, HttpMethod.GET, entity, XCalendarCollectionType.class, refId);

				navigationLastPage = Integer.parseInt(response.getHeaders().getFirst("navigationLastPage"));
			}
			else if (p.name().equals("GETXCOURSESBYXLEA") || p.name().equals("GETXCOURSESBYXSCHOOL") || p.name().equals("GETXCOURSESBYXROSTER"))
			{
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
				headers.set("navigationPage", Integer.toString(navigationPage));
				headers.set("navigationPageSize", Integer.toString(navigationPageSize));
				//headers.set("Content-Type", "application/json");

				HttpEntity<?> entity = new HttpEntity<Object>(headers);
				
				ResponseEntity<XCourseCollectionType> response = rt.exchange(baseApiUrl + p.value, HttpMethod.GET, entity, XCourseCollectionType.class, refId);

				navigationLastPage = Integer.parseInt(response.getHeaders().getFirst("navigationLastPage"));	
			}
			else if (p.name().equals("GETXROSTERSBYXLEA") || p.name().equals("GETXROSTERSBYXSCHOOL") || p.name().equals("GETXROSTERSBYXCOURSE") || p.name().equals("GETXROSTERSBYXSTAFF") || p.name().equals("GETXROSTERSBYXSTUDENT"))
			{
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
				headers.set("navigationPage", Integer.toString(navigationPage));
				headers.set("navigationPageSize", Integer.toString(navigationPageSize));
				//headers.set("Content-Type", "application/json");

				HttpEntity<?> entity = new HttpEntity<Object>(headers);
				
				ResponseEntity<XRosterCollectionType> response = rt.exchange(baseApiUrl + p.value, HttpMethod.GET, entity, XRosterCollectionType.class, refId);

				navigationLastPage = Integer.parseInt(response.getHeaders().getFirst("navigationLastPage"));
			}
			else if (p.name().equals("GETXSTAFFSBYXLEA") || p.name().equals("GETXSTAFFSBYXSCHOOL") || p.name().equals("GETXSTAFFSBYXCOURSE") || p.name().equals("GETXSTAFFSBYXROSTER") || p.name().equals("GETXSTAFFSBYXSTUDENT"))
			{
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
				headers.set("navigationPage", Integer.toString(navigationPage));
				headers.set("navigationPageSize", Integer.toString(navigationPageSize));
				//headers.set("Content-Type", "application/json");

				HttpEntity<?> entity = new HttpEntity<Object>(headers);
				
				ResponseEntity<XStaffCollectionType> response = rt.exchange(baseApiUrl + p.value, HttpMethod.GET, entity, XStaffCollectionType.class, refId);

				navigationLastPage = Integer.parseInt(response.getHeaders().getFirst("navigationLastPage"));
			}
			else if (p.name().equals("GETXSTUDENTSBYXLEA") || p.name().equals("GETXSTUDENTSBYXSCHOOL") || p.name().equals("GETXSTUDENTSBYXROSTER") || p.name().equals("GETXSTUDENTSBYXSTAFF") || p.name().equals("GETXSTUDENTSBYXCONTACT"))
			{
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
				headers.set("navigationPage", Integer.toString(navigationPage));
				headers.set("navigationPageSize", Integer.toString(navigationPageSize));
				//headers.set("Content-Type", "application/json");

				HttpEntity<?> entity = new HttpEntity<Object>(headers);
				
				ResponseEntity<XStudentCollectionType> response = rt.exchange(baseApiUrl + p.value, HttpMethod.GET, entity, XStudentCollectionType.class, refId);

				navigationLastPage = Integer.parseInt(response.getHeaders().getFirst("navigationLastPage"));
			}
			else if (p.name().equals("GETXCONTACTSBYXLEA") || p.name().equals("GETXCONTACTSBYXSCHOOL") || p.name().equals("GETXCONTACTSBYXSTUDENT"))
			{
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
				headers.set("navigationPage", Integer.toString(navigationPage));
				headers.set("navigationPageSize", Integer.toString(navigationPageSize));
				//headers.set("Content-Type", "application/json");

				HttpEntity<?> entity = new HttpEntity<Object>(headers);
				
				ResponseEntity<XContactCollectionType> response = rt.exchange(baseApiUrl + p.value, HttpMethod.GET, entity, XContactCollectionType.class, refId);

				navigationLastPage = Integer.parseInt(response.getHeaders().getFirst("navigationLastPage"));
			}

			return navigationLastPage;
		}
		catch(HttpClientErrorException e)
		{
			return 0;
		}
	}
	
	/**
	 * 
	 * @param navigationPageSize
	 * @param p
	 * @return Max page value for specified service path object
	 */
	public int getLastPage(int navigationPageSize, ServicePath p)
	{
		int navigationPage = 1;
		Integer navigationLastPage = null;
		
		try
		{
			if (p.name().equals("GETXLEAS"))
			{
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
				headers.set("navigationPage", Integer.toString(navigationPage));
				headers.set("navigationPageSize", Integer.toString(navigationPageSize));
				//headers.set("Content-Type", "application/json");

				HttpEntity<?> entity = new HttpEntity<Object>(headers);
				
				ResponseEntity<XLeaCollectionType> response = rt.exchange(baseApiUrl + p.value, HttpMethod.GET, entity, XLeaCollectionType.class);

				navigationLastPage = Integer.parseInt(response.getHeaders().getFirst("navigationLastPage"));
			}
			else if (p.name().equals("GETXSCHOOLS"))
			{
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
				headers.set("navigationPage", Integer.toString(navigationPage));
				headers.set("navigationPageSize", Integer.toString(navigationPageSize));
				//headers.set("Content-Type", "application/json");

				HttpEntity<?> entity = new HttpEntity<Object>(headers);
				
				ResponseEntity<XSchoolCollectionType> response = rt.exchange(baseApiUrl + p.value, HttpMethod.GET, entity, XSchoolCollectionType.class);

				navigationLastPage = Integer.parseInt(response.getHeaders().getFirst("navigationLastPage"));
			}
			else if (p.name().equals("GETXCALENDARS"))
			{
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
				headers.set("navigationPage", Integer.toString(navigationPage));
				headers.set("navigationPageSize", Integer.toString(navigationPageSize));
				//headers.set("Content-Type", "application/json");

				HttpEntity<?> entity = new HttpEntity<Object>(headers);
				
				ResponseEntity<XCalendarCollectionType> response = rt.exchange(baseApiUrl + p.value, HttpMethod.GET, entity, XCalendarCollectionType.class);

				navigationLastPage = Integer.parseInt(response.getHeaders().getFirst("navigationLastPage"));
			}
			else if (p.name().equals("GETXCOURSES"))
			{
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
				headers.set("navigationPage", Integer.toString(navigationPage));
				headers.set("navigationPageSize", Integer.toString(navigationPageSize));
				//headers.set("Content-Type", "application/json");

				HttpEntity<?> entity = new HttpEntity<Object>(headers);
				
				ResponseEntity<XCourseCollectionType> response = rt.exchange(baseApiUrl + p.value, HttpMethod.GET, entity, XCourseCollectionType.class);

				navigationLastPage = Integer.parseInt(response.getHeaders().getFirst("navigationLastPage"));
			}
			else if (p.name().equals("GETXROSTERS"))
			{
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
				headers.set("navigationPage", Integer.toString(navigationPage));
				headers.set("navigationPageSize", Integer.toString(navigationPageSize));
				//headers.set("Content-Type", "application/json");

				HttpEntity<?> entity = new HttpEntity<Object>(headers);
				
				ResponseEntity<XRosterCollectionType> response = rt.exchange(baseApiUrl + p.value, HttpMethod.GET, entity, XRosterCollectionType.class);

				navigationLastPage = Integer.parseInt(response.getHeaders().getFirst("navigationLastPage"));
			}
			else if (p.name().equals("GETXSTAFFS"))
			{
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
				headers.set("navigationPage", Integer.toString(navigationPage));
				headers.set("navigationPageSize", Integer.toString(navigationPageSize));
				//headers.set("Content-Type", "application/json");

				HttpEntity<?> entity = new HttpEntity<Object>(headers);
				
				ResponseEntity<XStaffCollectionType> response = rt.exchange(baseApiUrl + p.value, HttpMethod.GET, entity, XStaffCollectionType.class);

				navigationLastPage = Integer.parseInt(response.getHeaders().getFirst("navigationLastPage"));
				
//				System.out.println(restTemplate.exchange(baseApiUrl + p.value, HttpMethod.GET, entity, XStaffCollectionType.class, navigationPage, navigationPageSize).toString());
//				System.out.println(response.getHeaders().toString());
//				System.out.println(baseApiUrl + p.value, HttpMethod.GET, entity);
//				System.out.println(navigationLastPage);
				
			}
			else if (p.name().equals("GETXSTUDENTS"))
			{
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
				headers.set("navigationPage", Integer.toString(navigationPage));
				headers.set("navigationPageSize", Integer.toString(navigationPageSize));
				//headers.set("Content-Type", "application/json");

				HttpEntity<?> entity = new HttpEntity<Object>(headers);
				
				ResponseEntity<XStudentCollectionType> response = rt.exchange(baseApiUrl + p.value, HttpMethod.GET, entity, XStudentCollectionType.class);

				navigationLastPage = Integer.parseInt(response.getHeaders().getFirst("navigationLastPage"));
			}
			else if (p.name().equals("GETXCONTACTS"))
			{
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
				headers.set("navigationPage", Integer.toString(navigationPage));
				headers.set("navigationPageSize", Integer.toString(navigationPageSize));
				//headers.set("Content-Type", "application/json");

				HttpEntity<?> entity = new HttpEntity<Object>(headers);
				
				ResponseEntity<XContactCollectionType> response = rt.exchange(baseApiUrl + p.value, HttpMethod.GET, entity, XContactCollectionType.class);

				navigationLastPage = Integer.parseInt(response.getHeaders().getFirst("navigationLastPage"));
			}

			return navigationLastPage;
		}
		catch(HttpClientErrorException e)
		{
			return 0;
		}
	}

	/**
	 * Create staff usernames and passwords by school
	 * @param refId
	 * @return 
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStaffType> createXStaffUsers(String refId) throws AuthenticationException
	{
		ResponseEntity<XStaffCollectionType> response = null;
		ResponseMulti<XStaffType> output = new ResponseMulti<XStaffType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseApiUrl)
					.path("xSchools/" + refId + "/xStaffs")
					.queryParam("createUsers", "true");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(builder.build().encode().toUriString(), HttpMethod.GET, entity, XStaffCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStaff());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * Delete generated staff passwords by school
	 * @param refId
	 * @return
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStaffType> deleteXStaffUsers(String refId) throws AuthenticationException
	{
		ResponseEntity<XStaffCollectionType> response = null;
		ResponseMulti<XStaffType> output = new ResponseMulti<XStaffType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseApiUrl)
					.path("xSchools/" + refId + "/xStaffs")
					.queryParam("deleteUsers", "true");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(builder.build().encode().toUriString(), HttpMethod.GET, entity, XStaffCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStaff());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * Return generated staff usernames and passwords by school
	 * @param refId
	 * @return
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStaffType> getXStaffUsers(String refId) throws AuthenticationException
	{
		ResponseEntity<XStaffCollectionType> response = null;
		ResponseMulti<XStaffType> output = new ResponseMulti<XStaffType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseApiUrl)
					.path("xSchools/" + refId + "/xStaffs")
					.queryParam("getUsers", "true");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(builder.build().encode().toUriString(), HttpMethod.GET, entity, XStaffCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStaff());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * Create student usernames and passwords by school
	 * @param refId
	 * @return
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStudentType> createXStudentUsers(String refId) throws AuthenticationException
	{
		ResponseEntity<XStudentCollectionType> response = null;
		ResponseMulti<XStudentType> output = new ResponseMulti<XStudentType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseApiUrl)
					.path("xSchools/" + refId + "/xStudents")
					.queryParam("createUsers", "true");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(builder.build().encode().toUriString(), HttpMethod.GET, entity, XStudentCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStudent());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * Delete generated student passwords by school
	 * @param refId
	 * @return
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStudentType> deleteXStudentUsers(String refId) throws AuthenticationException
	{
		ResponseEntity<XStudentCollectionType> response = null;
		ResponseMulti<XStudentType> output = new ResponseMulti<XStudentType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseApiUrl)
					.path("xSchools/" + refId + "/xStudents")
					.queryParam("deleteUsers", "true");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);

			response = rt.exchange(builder.build().encode().toUriString(), HttpMethod.GET, entity, XStudentCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStudent());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * Return generated student usernames and passwords by school
	 * @param refId
	 * @return
	 * @throws AuthenticationException 
	 */
	public ResponseMulti<XStudentType> getXStudentUsers(String refId) throws AuthenticationException
	{
		ResponseEntity<XStudentCollectionType> response = null;
		ResponseMulti<XStudentType> output = new ResponseMulti<XStudentType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseApiUrl)
					.path("xSchools/" + refId + "/xStudents")
					.queryParam("getUsers", "true");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			
			response = rt.exchange(builder.build().encode().toUriString(), HttpMethod.GET, entity, XStudentCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXStudent());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * Create contact usernames and passwords by school
	 * @param refId
	 * @return
	 * @throws AuthenticationException 
	 */
	private ResponseMulti<XContactType> createXContactUsers(String refId) throws AuthenticationException
	{
		ResponseEntity<XContactCollectionType> response = null;
		ResponseMulti<XContactType> output = new ResponseMulti<XContactType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseApiUrl)
					.path("xSchools/" + refId + "/xContacts")
					.queryParam("createUsers", "true");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
//			System.out.println(builder.build().encode().toUriString());
			response = rt.exchange(builder.build().encode().toUriString(), HttpMethod.GET, entity, XContactCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXContact());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * Delete generated contact passwords by school
	 * @param refId
	 * @return
	 * @throws AuthenticationException 
	 */
	private ResponseMulti<XContactType> deleteXContactUsers(String refId) throws AuthenticationException
	{
		ResponseEntity<XContactCollectionType> response = null;
		ResponseMulti<XContactType> output = new ResponseMulti<XContactType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseApiUrl)
					.path("xSchools/" + refId + "/xContacts")
					.queryParam("deleteUsers", "true");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
//			System.out.println(builder.build().encode().toUriString());
			response = rt.exchange(builder.build().encode().toUriString(), HttpMethod.GET, entity, XContactCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXContact());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
	
	/**
	 * Return generated contact usernames and passwords by school
	 * @param refId
	 * @return
	 * @throws AuthenticationException 
	 */
	private ResponseMulti<XContactType> getXContactUsers(String refId) throws AuthenticationException
	{
		ResponseEntity<XContactCollectionType> response = null;
		ResponseMulti<XContactType> output = new ResponseMulti<XContactType>();
		
		Authenticator.getInstance().refreshToken(Authenticator.getInstance().getToken());
		
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + Authenticator.getInstance().getToken());
			//headers.set("Content-Type", "application/json");
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseApiUrl)
					.path("xSchools/" + refId + "/xContacts")
					.queryParam("getUsers", "true");

			HttpEntity<?> entity = new HttpEntity<Object>(headers);
//			System.out.println(builder.build().encode().toUriString());
			response = rt.exchange(builder.build().encode().toUriString(), HttpMethod.GET, entity, XContactCollectionType.class);

			if(response.getBody() != null)
			{
				output.setData(response.getBody().getXContact());
			}
			output.setMessage(response.getStatusCode().getReasonPhrase());
			output.setStatusCode(response.getStatusCode().value());
			output.setHeader(response.getHeaders().toString());
		}
		catch(HttpClientErrorException e)
		{
			output.setMessage(e.getStatusText());
			output.setStatusCode(e.getStatusCode().value());
			output.setHeader(e.getResponseHeaders().toString());
		}

		return output;
	}
}

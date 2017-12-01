/**
 * @author      Andrew Pieniezny <andrew.pieniezny@neric.org>
 * @version     x.x.x
 * @since       Jan 9, 2017
 * @filename	JsonXmlReturn.java
 */
package cases;

import com.fasterxml.jackson.core.JsonProcessingException;

import riconeapi.authentication.Authenticator;
import riconeapi.common.XPress;
import riconeapi.common.XPress2;
import riconeapi.exceptions.AuthenticationException;
import riconeapi.models.authentication.Endpoint;
import riconeapi.models.xpress.XLeaType;

public class JsonXmlReturn
{
	final static String authUrl = "https://auth.test.ricone.org/login";
	final static String clientId = "dpaDemo";
	final static String clientSecret = "deecd889bff5ed0101a86680752f5f9";
//	final static String clientSecret = "deecd889bff5ed0101a86680752f5f";
	final static String providerId = "localhost";
	final static int  navigationPage = 1;
	final static int navigationPageSize = 1;
	
	public static void main(String[] args) throws AuthenticationException, JsonProcessingException
	{
		Authenticator auth = Authenticator.getInstance();
		auth.authenticate(authUrl, clientId, clientSecret);
		
		for(Endpoint e : auth.getEndpoints(providerId))
		{
			XPress2 xpress2 = new XPress2(e.getHref());

			System.out.println(xpress2.getXLeas().getJson());
			System.out.println(xpress2.getXLeas().getXml());
			System.out.println(xpress2.getXLeas().getData().toString());

//			for(XLeaType l : xpress2.getXLeas().getData())
//			{
//				System.out.println(l.getRefId());
//				System.out.println(l.getLeaName());
//			}



//			XPress xPress = new XPress(e.getHref());
//
//			if(xPress.getXLeas().getData() != null)
//			{
//				System.err.println("not null");
//				System.err.println(xPress.getXLeas().getJson());
//				System.err.println(xPress.getXLeas().getXml());
//
//				for(XLeaType l : xPress.getXLeas().getData())
//				{
//					System.err.println("in loop");
//					System.err.println(l.getRefId());
//					System.out.println(l.getLeaName());
//				}
//			}
		}
	}

}

package fr.insat.om2m.tp3.ipe;

import java.io.IOException;

import org.eclipse.om2m.commons.resource.AE;
import org.eclipse.om2m.commons.resource.Container;
import org.eclipse.om2m.commons.resource.ContentInstance;

import fr.insat.om2m.tp3.client.Client;
import fr.insat.om2m.tp3.client.ClientInterface;
import fr.insat.om2m.tp3.client.Response;
import fr.insat.om2m.tp3.mapper.Mapper;
import fr.insat.om2m.tp3.mapper.MapperInterface;

/**
 * The monitor will retrieve information from the devices and push them to the
 * middleware
 *
 */
public class Monitor {
	
	/** Point of access of the http server part */
	private static final String SERVER_POA = "http://localhost:1400/monitor";
	private static final String ORIGINATOR = "admin:admin";
	private ClientInterface client = new Client();
	private MapperInterface mapper = new Mapper();

	/**
	 * Constructor
	 */
	public Monitor() {
	}

	public void pushState(String lampId, String state, String color, int hue) {
		ContentInstance dataInstance = new ContentInstance();
		dataInstance.setContent(Model.getDataRep("Home", state, color, hue));
		dataInstance.setContentInfo("application/obix:0");
		try {
			client.create("http://localhost:8080/~/mn-cse/mn-name/" + "LAMP_" + lampId + "/DATA", mapper.marshal(dataInstance), ORIGINATOR, "4");
		} catch (IOException e) {
			System.err.println("Error creating the content instance");
			e.printStackTrace();
		}
	}

	/**
	 * Creates all required resources.
	 * 
	 * @param lampId
	 *            - Application ID
	 */
	public void createResources(String lampId) {
		System.out.println("Initializing OM2M lamp ID " + lampId);

		// Create the Application resource
		AE ae = new AE();
		ae.setAppID(lampId);
		ae.setRequestReachability(true);
		ae.getPointOfAccess().add(SERVER_POA);
		ae.setName("LAMP_" + lampId);
		ae.getLabels().add("Type/Lamp");
		ae.getLabels().add("Location/INSA");
		ae.getLabels().add("Manufacturer/PhilipsHue");
		
		try {
			// Creating the AE representing the device
			Response resp = client.create("http://localhost:8080/~/mn-cse/mn-name", mapper.marshal(ae), ORIGINATOR, "2");
			if(resp.getStatusCode() != 201){
				System.out.println("Problem on AE creation (or conflict): " + resp.getRepresentation());
				return;
			}
			
			// Create the DESCRIPTOR container
			Container cnt = new Container();
			cnt.setName("DESCRIPTOR");
			client.create("http://localhost:8080/~/mn-cse/mn-name/" + "LAMP_" + lampId , mapper.marshal(cnt), ORIGINATOR, "3");
			
			// Push a description into a content instance
			ContentInstance descriptor = new ContentInstance();
			descriptor.setContent(Model.getDescriptorRep("mn-cse", "LAMP_" + lampId, "Home"));
			descriptor.setContentInfo("application/obix:0");
			client.create("http://localhost:8080/~/mn-cse/mn-name/" + "LAMP_" + lampId + "/DESCRIPTOR", mapper.marshal(descriptor), ORIGINATOR, "4");
			
			// Create the DATA container
			cnt.setName("DATA");
			client.create("http://localhost:8080/~/mn-cse/mn-name/" + "LAMP_" + lampId , mapper.marshal(cnt), ORIGINATOR, "3");
			
		} catch (IOException e) {
			System.err.println("Error on creating resources");
			e.printStackTrace();
		}

	}

}

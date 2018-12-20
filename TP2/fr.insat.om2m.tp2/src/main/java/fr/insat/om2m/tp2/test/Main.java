package fr.insat.om2m.tp2.test;

import fr.insat.om2m.tp2.client.Response;
import fr.insat.om2m.tp2.util.RequestLoader;
import fr.insat.om2m.tp2.client.Client;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		Client cl = new Client();
		Response res;

		// 1) Retrieve the attributes of the CSE Base with child resources (query string rcn=5)
		res = cl.retrieve("http://localhost:8080/~/in-cse", "admin:admin");

		// 2) Register an "appTest" resource (create an AE)
		String ae = RequestLoader.getRequestFromFile("create_ae.xml");
		res = cl.create("http://localhost:8080/~/in-cse", ae, "admin:admin", "2");

		// 3) Retrieve the "appTest" resource attributes
		res = cl.retrieve("http://localhost:8080/~/in-cse/in-name/appTest", "admin:admin");

		// 4) Update the "appTest" resource
		String up_ae = RequestLoader.getRequestFromFile("update_ae.xml");
		res = cl.update("http://localhost:8080/~/in-cse/in-name/appTest", up_ae,"admin:admin");

		// 5) Retrieve the new "appTest" resource
		res = cl.retrieve("http://localhost:8080/~/in-cse/in-name/appTest", "admin:admin");

		// 6) Create a "cntTest" container resource
		String cnt = RequestLoader.getRequestFromFile("create_cnt.xml");
		res = cl.create("http://localhost:8080/~/in-cse/in-name/appTest", cnt, "admin:admin", "3");

		// 7) Retrieve the "cntTest" resource
		res = cl.retrieve("http://localhost:8080/~/in-cse/in-name/appTest/cntTest", "admin:admin");

		// 8) Create a "cinTest" content instance resource
		String cin = RequestLoader.getRequestFromFile("create_cin.xml");
		res = cl.create("http://localhost:8080/~/in-cse/in-name/appTest/cntTest", cin, "admin:admin", "4");

		// 9) Retrieve the "cinTest" resource
		res = cl.retrieve("http://localhost:8080/~/in-cse/in-name/appTest/cntTest/cinTest", "admin:admin");
	}

}
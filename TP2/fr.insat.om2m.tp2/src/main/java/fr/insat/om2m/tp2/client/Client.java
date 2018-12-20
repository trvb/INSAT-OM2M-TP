package fr.insat.om2m.tp2.client;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Client implements ClientInterface {

	public Response retrieve(String url, String originator) throws IOException {
		Response response = new Response();
		// Instantiate a new Client
		CloseableHttpClient client = HttpClients.createDefault();
		// Instantiate the correct Http Method
		HttpGet get = new HttpGet(url);
		// add headers
		get.addHeader("X-M2M-Origin", originator);
		get.addHeader("Accept", "application/xml");

		try {
			// send request
			CloseableHttpResponse reqResp = client.execute(get);
			response.setStatusCode(reqResp.getStatusLine().getStatusCode());
			response.setRepresentation(IOUtils.toString(reqResp.getEntity().getContent(), "UTF-8"));
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			client.close();
		}
		// return response
		return response;
	}

	public Response create(String url, String representation, String originator, String type) throws IOException {
		Response response = new Response();

		// Instantiate a new Client
		CloseableHttpClient client = HttpClients.createDefault();
		// Instantiate the correct Http Method
		HttpPost post = new HttpPost(url);
		// add headers
		post.addHeader("X-M2M-Origin", originator);
		post.addHeader("Content-Type", "application/xml;ty=" + type);

		// add the body to the request
		post.setEntity(new StringEntity(representation));
		try {
			// execute the HTTP request and receive the HTTP response
			CloseableHttpResponse reqResp = client.execute(post);
			// get the status code of the response
			response.setStatusCode(reqResp.getStatusLine().getStatusCode());
			// get the body of the response
			response.setRepresentation(IOUtils.toString(reqResp.getEntity().getContent(),"UTF-8"));
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			client.close();
		}

		return response;
	}

	public Response update(String url, String representation, String originator) throws IOException {
		Response response = new Response();

		// Instantiate a new Client
		CloseableHttpClient client = HttpClients.createDefault();
		// Instantiate the correct Http Method
		HttpPut put = new HttpPut(url);
		// add headers
		put.addHeader("X-M2M-Origin", originator);
		put.addHeader("Content-Type", "application/xml");

		// add the body to the request
		put.setEntity(new StringEntity(representation));
		try {
			// execute the HTTP request and receive the HTTP response
			CloseableHttpResponse reqResp = client.execute(put);
			// get the status code of the response
			response.setStatusCode(reqResp.getStatusLine().getStatusCode());
			// get the body of the response
			response.setRepresentation(IOUtils.toString(reqResp.getEntity().getContent(),"UTF-8"));
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			client.close();
		}

		return response;
	}

	public Response delete(String url, String originator) throws IOException {
		Response response = new Response();
		// Instantiate a new Client
		CloseableHttpClient client = HttpClients.createDefault();
		// Instantiate the correct Http Method
		HttpDelete del = new HttpDelete(url);
		// add headers
		del.addHeader("X-M2M-Origin", originator);
		del.addHeader("Accept", "application/xml");

		try {
			// send request
			CloseableHttpResponse reqResp = client.execute(del);
			response.setStatusCode(reqResp.getStatusLine().getStatusCode());
			response.setRepresentation(IOUtils.toString(reqResp.getEntity().getContent(), "UTF-8"));
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			client.close();
		}
		// return response
		return response;
	}

}

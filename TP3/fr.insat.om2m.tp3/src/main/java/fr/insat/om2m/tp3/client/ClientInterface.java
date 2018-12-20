package fr.insat.om2m.tp3.client;

import java.io.IOException;

/**
 * Interface for the client part
 *
 */
public interface ClientInterface {
	public Response retrieve(String url, String originator) throws IOException;

	public Response create(String url, String representation, String originator, String type)
			throws IOException;

	public Response update(String url, String representation, String originator)
			throws IOException;

	public Response delete(String url, String originator) throws IOException;
}

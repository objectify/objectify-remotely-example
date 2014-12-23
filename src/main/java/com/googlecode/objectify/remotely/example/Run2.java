package com.googlecode.objectify.remotely.example;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;
import com.googlecode.objectify.remotely.RemoteCheck;
import com.googlecode.objectify.remotely.Remotely;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 */
public class Run2 extends HttpServlet {

	private Remotely remotely;

	@Override
	public void init() throws ServletException {
		RemoteApiOptions options = new RemoteApiOptions()
				.server("voodoodyne1.appspot.com", 443)
//				.server("localhost", 8080)
				.credentials("asdf", "asdf");

		remotely = new Remotely(options, new RemoteCheck() {
			@Override
			public boolean isRemote(String namespace) {
				return true;
			}
		});
	}

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		try {
			AsyncDatastoreService ds = remotely.intercept(DatastoreServiceFactory.getAsyncDatastoreService());
			Entity ent = new Entity("fun");
			ds.put(ent).get();
			write(resp, "Saved: " + ent);

			Entity fetched = ds.get(ent.getKey()).get();
			write(resp, "Fetched: " + fetched);

			write(resp, "All:");
			for (Entity e : ds.prepare(new Query("fun")).asQueryResultIterable()) {
				write(resp, e.toString());
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new ServletException(e);
		}
	}

	private void write(HttpServletResponse resp, String someText) {
		try {
			resp.getWriter().println(someText);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

package com.googlecode.objectify.remotely.example;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.remotely.RemoteWrapper;
import com.googlecode.objectify.remotely.Remotely;
import com.googlecode.objectify.remotely.VoidCallable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 */
public class Run2 extends HttpServlet {

	@Override
	public void init() throws ServletException {
		ObjectifyService.setFactory(new OurObjectifyFactory());

		RemoteApiOptions options = new RemoteApiOptions()
				.server("localhost", 8080)
				.credentials("asdf", "asdf");
		Remotely.setOptions(options);
	}

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		Remotely.execute(new VoidCallable() {
			@Override
			public void run() throws Exception {
				AsyncDatastoreService ds = (AsyncDatastoreService)RemoteWrapper.create(DatastoreServiceFactory.getAsyncDatastoreService());
				Entity ent = new Entity("fun");
				ds.put(ent).get();
				write(resp, "Saved: " + ent);

				Entity fetched = ds.get(ent.getKey()).get();
				write(resp, "Fetched: " + fetched);

				write(resp, "All:");
				for (Entity e : ds.prepare(new Query("fun")).asQueryResultIterable()) {
					write(resp, e.toString());
				}
			}
		});
	}

	private void write(HttpServletResponse resp, String someText) {
		try {
			resp.getWriter().println(someText);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

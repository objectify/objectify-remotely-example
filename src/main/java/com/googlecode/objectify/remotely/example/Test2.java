package com.googlecode.objectify.remotely.example;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;
import com.googlecode.objectify.remotely.RemoteWrapper;
import com.googlecode.objectify.remotely.Remotely;
import com.googlecode.objectify.remotely.VoidCallable;

/**
 * A main method for probing the remote api installer by hand.
 */
public class Test2 {

	public static void main(String[] args) throws Exception {
		RemoteApiOptions options = new RemoteApiOptions()
//				.server("localhost", 8080)
//				.credentials("", "");
				.server("voodoodyne1.appspot.com", 443)
				.credentials("asdf", "asdf");

		Remotely.setOptions(options);
		Remotely.execute(new VoidCallable() {
			@Override
			public void run() throws Exception {
				AsyncDatastoreService ds = (AsyncDatastoreService)RemoteWrapper.create(DatastoreServiceFactory.getAsyncDatastoreService());
				Entity ent = new Entity("fun");
				ds.put(ent).get();
				System.out.println("Saved: " + ent);

				Entity fetched = ds.get(ent.getKey()).get();
				System.out.println("Fetched: " + fetched);

				System.out.println("All:");
				for (Entity e : ds.prepare(new Query("fun")).asIterable()) {
					System.out.println(e.toString());
				}

			}
		});

	}
}

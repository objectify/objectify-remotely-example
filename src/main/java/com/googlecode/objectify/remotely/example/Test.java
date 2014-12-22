package com.googlecode.objectify.remotely.example;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.remoteapi.CachingRemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;

/**
 * A main method for probing the remote api installer by hand.
 */
public class Test {

	public static void main(String[] args) throws Exception {
		RemoteApiOptions options = new RemoteApiOptions()
				.server("localhost", 8080)
				.credentials("", "");

		RemoteApiInstaller foo = new CachingRemoteApiInstaller();
		foo.install(options);

		AsyncDatastoreService ds = DatastoreServiceFactory.getAsyncDatastoreService();
		Entity ent = new Entity("fun");
		ds.put(ent).get();
		System.out.println("Saved: " + ent);

		foo.uninstall();

		//

		foo.install(options);

		Entity fetched = ds.get(ent.getKey()).get();
		System.out.println("Fetched: " + fetched);
	}
}

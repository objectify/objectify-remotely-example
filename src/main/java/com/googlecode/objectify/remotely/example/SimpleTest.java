package com.googlecode.objectify.remotely.example;

import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;
import com.googlecode.objectify.remotely.Remotely;

/**
 * A main method for probing the remote api installer by hand.
 */
public class SimpleTest {

	public static void main(String[] args) throws Exception {
		RemoteApiOptions options = new RemoteApiOptions()
				.server("localhost", 8080)
				.credentials("", "");
		Remotely.setOptions(options);

		RemoteApiInstaller foo = new RemoteApiInstaller();
		foo.install(options);
		foo.uninstall();
	}
}

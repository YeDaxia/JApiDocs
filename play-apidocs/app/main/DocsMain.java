package main;

import apidoc.Docs;

public class DocsMain {
	public static void main(String[] args) {
		Docs docs = new Docs("D:/play-apidocs");
		docs.buildHtmlDocs();
	}
}

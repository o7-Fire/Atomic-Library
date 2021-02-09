/*
 * Copyright 2020 Itzbenz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package Atom.Utility;

import Atom.File.FileUtility;
import Atom.Net.Download;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Cache {
	public static File cache = new File("cache/");
	
	static {
		cache.mkdirs();
	}
	
	public static URL tryCache(String url) {
		try {
			return tryCache(new URL(url));
		}catch (Throwable ignored) {}
		try {
			return new URL(url);
		}catch (Throwable t) {
			throw new IllegalArgumentException(t);
		}
	}
	
	public static URL tryCache(URL u) {
		try {
			return http(u);
		}catch (Throwable ignored) {}
		return u;
	}
	
	
	public static URL http(String s) throws IOException {
		return http(new URL(s));
	}
	
	public static File urlToFile(URL url) {
		return new File(cache, url.getHost() + "/" + url.getFile().replaceAll("/", "."));
	}
	
	public static boolean updateCache(URL url, byte[] bytes) {
		File target = urlToFile(url);
		return FileUtility.write(target, bytes);
	}
	
	public static URL http(URL url) throws IOException {
		if (url == null) return null;
		if (url.getProtocol().startsWith("file")) return url;
		if (!url.getProtocol().startsWith("http")) throw new MalformedURLException("URL is not http");
		File target = urlToFile(url);
		if (target.exists() && TimeUnit.DAYS.convert(System.currentTimeMillis() - Cache.urlToFile(url).lastModified(), TimeUnit.MICROSECONDS) < 2)
			return target.toURI().toURL();
		Throwable download = null;
		try {
			Download d = new Download(url, target);
			d.run();
		}catch (Throwable i) {
			download = i;
		}
		if (target.exists()) return target.toURI().toURL();
		else if (download == null) throw new FileNotFoundException(target.getAbsolutePath());
		else throw new RuntimeException(download);
	}
}

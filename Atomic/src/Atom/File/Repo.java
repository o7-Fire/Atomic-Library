/*
 * Copyright 2021 Itzbenz
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

package Atom.File;

import Atom.Encoding.Encoder;
import Atom.Net.Request;
import Atom.Struct.InstantFuture;
import Atom.Utility.Pool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Replaced by {@link Atom.File.ResourcesPool}
 */
@Deprecated()
public class Repo {
    protected ArrayList<URL> repos = new ArrayList<>();

    public Repo() {

    }

    public Repo(URL... urls) {
        repos.addAll(Arrays.asList(urls));
    }

    public static URL appendURL(URL u, String s) throws MalformedURLException {
        return new URL(u.toString() + (u.toString().endsWith("/") ? "" : "/") + s);
    }

    public static URL getResource(URL u, String s) {
        try {
            URL url = appendURL(u, s);
            url = Request.getRedirect(url);
            if (url.getContent() != null) return url;
        }catch (Throwable ignored) {}
        return null;
    }


    public ArrayList<String> readArrayString(String path, String delimiter) throws IOException {
        return new ArrayList<>(Arrays.asList(readString(path).split(delimiter)));
    }

    public Properties readProperty(String path) throws IOException {
        Properties p = new Properties();
        p.load(getResourceAsStream(path));
        return p;
    }

    public HashMap<String, String> readMap(String path) throws IOException {
        return Encoder.parseProperty(getResourceAsStream(path));
    }

    public ArrayList<String> readArrayString(String path) throws IOException {
        return readArrayString(path, System.getProperty("line.separator"));
    }

    public String readString(String path) throws IOException {
        return Encoder.readString(getResourceAsStream(path));
    }

    public void loadClasspath() {
        for (String s : System.getProperty("java.class.path").split(File.pathSeparator)) {
            try {
                addRepo(new File(s));
            }catch (MalformedURLException e) {
                // ???
            }
        }
    }

    public URL getResource(String s) {
        for (Future<URL> f : parallelSearch(s)) {
            try {
                if (f.get() != null) return f.get();
            }catch (InterruptedException | ExecutionException ignored) { }
        }
        return null;
    }

    protected ArrayList<Future<URL>> parallelSearch(String s) {
        ArrayList<Future<URL>> futures = new ArrayList<>();
        boolean concurrent = repos.size() > 5;
        for (URL u : repos) {
            if (concurrent){
                futures.add(Pool.submit(() -> getResource(u, s)));
            }else {
                futures.add((InstantFuture<URL>) () -> getResource(u, s));
            }
        }
        return futures;
    }

    public boolean resourceExists(String s) {
        for (Future<URL> f : parallelSearch(s)) {
            try {
                if (f.get() != null) return true;
            }catch (InterruptedException | ExecutionException ignored) { }
        }
        return false;
    }

    public InputStream getResourceAsStream(String s) throws IOException {
        URL u = getResource(s);
        if (u == null) throw new FileNotFoundException(s);
        return u.openStream();
    }

    public ArrayList<URL> getRepos() {
        return repos;
    }

    public void addRepo(File f) throws MalformedURLException {
        addRepo(f.toURI().toURL());
    }

    public void addRepo(URL u) {
        repos.add(u);
    }

}

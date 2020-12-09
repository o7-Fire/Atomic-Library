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

package Atom.Classloader;


import Atom.Net.HTPS;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class AtomClassLoader extends URLClassLoader {
    public static File cache = new File("lib/");

    static {
        cache.mkdirs();
        registerAsParallelCapable();
    }


    public AtomClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public AtomClassLoader(URL[] urls) {
        super(urls);
    }

    public void defineClass(String name, InputStream is) throws IOException {
        byte[] h = is.readAllBytes();
        defineClass(name, h, 0, h.length);
    }


    @Override
    public synchronized void addURL(URL url) {
        if (url.getProtocol().startsWith("http")) {
            File temp = new File(cache, url.getFile());//.substring(1).replace("/", ".")
            temp.getParentFile().mkdirs();
            if (!temp.exists()) {
                try {
                    HTPS.downloadSync(url.toExternalForm(), temp);
                }catch (IOException e) { }
            }
            if (temp.exists()) try {
                url = temp.toURI().toURL();
            }catch (Throwable ignored) { }//sometime its just dont work file to url
        }
        super.addURL(url);
    }

    public synchronized void addURL(File file) throws MalformedURLException {
        if (file.exists())
            addURL(file.toURI().toURL());
        //else Log.errTag("Ozone-LibraryLoader", file.getAbsolutePath() + " doesn't exist");
    }

    @Nullable
    @Override
    public URL getResource(String name) {
        URL u = super.getResource(name);
        if (u == null) u = ClassLoader.getSystemResource(name);
        return u;
    }


    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try { return super.loadClass(name); }catch (Throwable ignored) {}
        return ClassLoader.getSystemClassLoader().loadClass(name);
    }
}

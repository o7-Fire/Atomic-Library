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

package Atom.Net;

import Atom.File.FileUtility;
import Atom.Utility.Pool;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

//probably will be relocated to Atom library
public class Download implements Runnable {
    // Max size of download buffer.
    protected static int MAX_BUFFER_SIZE = 8192;
    protected URL url; // download URL
    protected AtomicLong downloaded; // number of bytes downloaded
    protected long size; // size of download in bytes
    protected File file;
    protected volatile boolean downloading = true;
    private Consumer<String> pw;
    
    // Constructor for Download.
    public Download(URL url, File file) {
        this.url = url;
        size = -1;
        downloaded = new AtomicLong();
        this.file = file;
        file.getParentFile().mkdirs();
        
    }
    
    protected static String getUserReading(long s) {
        if (s < 10000000) return s / 1000 + " KB";
        else return s / 1_000_000L + " MB";
    }
    
    public void print(Consumer<String> is) {
        if (pw != null) return;
        pw = is;
        Pool.submit((Runnable) this::print);
    }
    
    private void print(String s) {
        if (pw != null) pw.accept(s);
    }
    
    public long getDownloaded() {
        return downloaded.get();
    }
    
    // Get this download's size.
    public long getSize() {
        return size;
    }
    
    private void print() {
        while (downloading) {
            try {
                Thread.sleep(2000);
                if (size < 1) continue;
                print("Downloading: " + getUserReading(downloaded.get()));
            }catch (Throwable ignored) {
            
            }
        }
    }
    
    protected void setMax(long max) {
        print("Total Size: " + getUserReading(size));
    }
    
    protected void updateProgress() {
    
    }
    
    // Download file.
    private void download() throws IOException {
        
        InputStream stream;
        FileOutputStream outputStream;
        File temp = new File(file.getParent(), System.currentTimeMillis() + ".temp");
        temp.deleteOnExit();
        print("Output: " + file.getAbsolutePath());
        print("URL: " + url.toExternalForm());
        outputStream = new FileOutputStream(temp);
        // Open connection to URL.
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // Specify what portion of file to download.
        connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
        
        // Connect to server.
        connection.connect();
        
        // Make sure response code is in the 200 range.
        if (connection.getResponseCode() / 100 != 2) {
            throw new IOException("Response Code: " + connection.getResponseCode());
        }
        
        // Check for valid content length.
        long contentLength = Long.parseLong(connection.getHeaderField("content-length"));
        if (contentLength < 1) {
            throw new IOException("Invalid content length");
        }
        
        if (size == -1) {
            size = contentLength;
            
        }
        setMax(size);
        stream = connection.getInputStream();
        while (true) {
            byte[] buffer;
            if (size - downloaded.get() > MAX_BUFFER_SIZE) {
                buffer = new byte[MAX_BUFFER_SIZE];
            }else {
                buffer = new byte[(int) (size - downloaded.get())];
            }
            
            // Read from server into buffer.
            int read = stream.read(buffer);
            if (read == -1) break;
            
            // Write buffer to file.
            outputStream.write(buffer, 0, read);
            downloaded.addAndGet(read);
            updateProgress();
        }
        
        // Close file.
        try {
            outputStream.close();
        }catch (Exception ignored) {
        }
        
        // Close connection to server.
        try {
            stream.close();
        }catch (Exception ignored) {
        }
        
        
        file.getParentFile().mkdirs();
        if (temp.exists()) FileUtility.replace(temp, file);
        else throw new FileNotFoundException(temp.getAbsolutePath() + " not found. transfer failure ?");
        if (pw != null) pw.accept("Finished");
    }
    
    public Future<?> runAsync() {
        return Pool.submit(this);
    }
    
    @Override
    public void run() {
        try {
            download();
            close();
        }catch (IOException e) {
            close();
            throw new RuntimeException(e);
        }
    }
    
    protected void close() {
        downloading = false;
        
    }
}
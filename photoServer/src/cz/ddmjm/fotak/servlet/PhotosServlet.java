package cz.ddmjm.fotak.servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// goglbla1235123
// lopata358
public class        PhotosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Query query = new Query("Photo");
        query.addSort("created", Query.SortDirection.DESCENDING);
        List<Entity> photos = DatastoreServiceFactory.getDatastoreService().prepare(query).asList(FetchOptions.Builder.withLimit(20));
        
        resp.setHeader("Content-Type", "text/html");  
        for (Entity photo : photos) {
             resp.getWriter().println("<a href='https://storage.googleapis.com/internet3.appspot.com/" + photo.getKey().getId() + ".png'><img style='width: 300px;' src='https://storage.googleapis.com/internet3.appspot.com/" + photo.getKey().getId() + ".png' /></a><br />");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Entity photo = new Entity("Photo");
        photo.setProperty("created", new Date());
        long id = DatastoreServiceFactory.getDatastoreService().put(photo).getId();

        GcsService gcStorage = GcsServiceFactory.createGcsService(new RetryParams.Builder().initialRetryDelayMillis(10).retryMaxAttempts(10).totalRetryPeriodMillis(15000).build());
        GcsFilename fileName = new GcsFilename("internet3.appspot.com", id + ".png");
        GcsOutputChannel outputChannel = gcStorage.createOrReplace(fileName, new GcsFileOptions.Builder().acl("public-read").mimeType("image/png").cacheControl("51056").build());
        copy(req.getInputStream(), Channels.newOutputStream(outputChannel));

    }

    private static void copy(InputStream input, OutputStream output) throws IOException {
        try {
            byte[] buffer = new byte[2 * 1024 * 1024];
            int bytesRead = input.read(buffer);
            while (bytesRead != -1) {
                output.write(buffer, 0, bytesRead);
                bytesRead = input.read(buffer);
            }
        } finally {
            input.close();
            output.close();
        }
    }
}

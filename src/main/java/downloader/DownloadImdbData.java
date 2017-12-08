package downloader;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Class for downloading name.basics.tsv.gz from the 'current' folder in the
 * imdb-datasets s3 bucket.
 *
 * Use with AWS Java SDK 1.11.156 or later.
 */

public class DownloadImdbData {

//    private static String bucketName = "imdb-datasets";
//    private static String key        = "documents/v1/current/title.principals.tsv.gz";

    private static String bucketName = System.getProperty("aws.s3.bucket");
    private static String key = System.getProperty("aws.file.path") + System.getProperty("download.file.name");

    public static void downloadFromS3() throws IOException, InterruptedException {

        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider(
                System.getProperty("user.home") + "/.aws/credentials",
                "default");
        AmazonS3 s3Client = new AmazonS3Client(credentialsProvider);

        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key)
                    .withRequesterPays(true);

            System.out.println("Downloading object");

            S3Object s3object = s3Client.getObject(getObjectRequest);

            System.out.println("Content-Type: "  +
                    s3object.getObjectMetadata().getContentType());

            writeFile(s3object.getObjectContent());
        } catch (AmazonServiceException ase) {
            handleASEException(ase);
        } catch (AmazonClientException ace) {
            handleACEException(ace);
        }
    }

    private static void handleACEException(AmazonClientException ace) {
        System.out.println("Caught an AmazonClientException, which means"+
                " the client encountered " +
                "an internal error while trying to " +
                "communicate with S3, " +
                "such as not being able to access the network.");
        System.out.println("Error Message: " + ace.getMessage());
    }

    private static void handleASEException(AmazonServiceException ase) {
        System.out.println("Caught an AmazonServiceException, which" +
                " means your request made it " +
                "to Amazon S3, but was rejected with an error response" +
                " for some reason.");
        System.out.println("Error Message:    " + ase.getMessage());
        System.out.println("HTTP Status Code: " + ase.getStatusCode());
        System.out.println("AWS Error Code:   " + ase.getErrorCode());
        System.out.println("Error Type:       " + ase.getErrorType());
        System.out.println("Request ID:       " + ase.getRequestId());
    }

    private static void writeFile(InputStream input) throws IOException, InterruptedException {
        byte[] buf = new byte[1024 * 1024];
        //OutputStream out = new FileOutputStream("/Users/adityahandadi/Documents/AWS/title.principals.tsv.gz");
        OutputStream out = new FileOutputStream(System.getProperty("download.output.file.path") + System.getProperty("download.file.name"));
        int count;
        while ((count = input.read(buf)) != -1) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            out.write(buf, 0, count);
        }
        out.close();
        input.close();
    }
}
package com.xevensolutions.baseapp.utils;

import android.os.AsyncTask;


import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.io.File;
import java.io.FileInputStream;

public class BlobImageUploader extends AsyncTask {
    public static final String storageContainer = "ihakeem";
    private static final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=ihakeem0storage;AccountKey=uMl3DviY2gCEhX5gm2Ax+Nxp9rTy2fsrJFQKaDXOjLXCBjZyHuN/L8C+0HzJV1jBhlIQiePZBicatFLg324fpA==;EndpointSuffix=core.windows.net";

    String selectedFilePath;

    BlobImageUploadListener blobImageUploadListener;
    private boolean isUploadingSuccessful;
    private String blobName;


    public BlobImageUploader(String selectedFilePath, BlobImageUploadListener blobImageUploadListener) {
        this.selectedFilePath = selectedFilePath;
        this.blobImageUploadListener = blobImageUploadListener;
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            // Create the blob client.
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            // Retrieve reference to a previously created container.
            CloudBlobContainer container = blobClient.getContainerReference(storageContainer);

            // Create the container if it does not exist
            container.createIfNotExists();

            String filePath = null;

            filePath = selectedFilePath;

            blobName = FileUtils.getFileNameFromPath(filePath);

            // Create or overwrite the blob (with the name "example.jpeg") with contents from a local file.
            CloudBlockBlob blob = container.getBlockBlobReference(blobName);
            File source = new File(filePath);

            blob.upload(new FileInputStream(source), source.length());

            isUploadingSuccessful = true;
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
            isUploadingSuccessful = false;

        }
        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        blobImageUploadListener.onCompletion(isUploadingSuccessful, blobName, Constants.BLOB_PATH + "/" + blobName);
    }

    public void upload() {
        execute();
    }

}

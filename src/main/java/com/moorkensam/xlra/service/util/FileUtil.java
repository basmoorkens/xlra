package com.moorkensam.xlra.service.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

public class FileUtil {

  /**
   * This parses a path+ filename ie ../folder/subfolder/file.txt and returns the last part of it.
   * 
   * @param path The path to use.
   * @return The full filename.
   */
  public static String getFileNameFromPath(String path) {
    if (path == null) {
      return "";
    }
    if (!path.contains("/")) {
      return path;
    }
    String[] parts = path.split("/");
    return parts[parts.length - 1];
  }

  /**
   * Writes a file to the httpservletrespone provided.
   * 
   * @param file The file to write out
   * @param response The response to write to
   */
  public static void serveDownloadToHttpServletResponse(final File file,
      final HttpServletResponse response) throws IOException {

    response.reset();
    response.setHeader("Content-Type", "application/pdf");
    OutputStream responseOutputStream = response.getOutputStream();
    InputStream pdfInputStream = new FileInputStream(file);
    byte[] bytesBuffer = new byte[2048];
    int bytesRead;
    while ((bytesRead = pdfInputStream.read(bytesBuffer)) > 0) {
      responseOutputStream.write(bytesBuffer, 0, bytesRead);
    }

    responseOutputStream.flush();
    pdfInputStream.close();
    responseOutputStream.close();

  }

}

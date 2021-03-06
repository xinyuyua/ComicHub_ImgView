package com.xinyuyua.comichub;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class StreamTool
{
 /**
  * get byte[] from inputstream
  * @param inStream 
  * @return byte[]
  * @throws Exception
  */
 public static byte[] read(InputStream inStream) throws Exception
 {
  ByteArrayOutputStream outStream=new ByteArrayOutputStream();
  byte[] buffer=new byte[1024];
  int len=0;
  while ((len=inStream.read(buffer))!=-1)
  {
   outStream.write(buffer, 0, len); 
  }
  inStream.close();
  return outStream.toByteArray();
 }
 
}

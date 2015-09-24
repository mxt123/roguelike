package util;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;


public class Read2D {
	
	public static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count ;
	    } finally {
	        is.close();
	    }
	}

	public static byte[][] read2dArray(final String fileName, final int arraySize) throws IOException {
	  FileInputStream fstream = new FileInputStream(fileName);
	  DataInputStream in = new DataInputStream(fstream);
	  BufferedReader br = new BufferedReader(new InputStreamReader(in));

	  String strLine;

	  byte array[][] = new byte[arraySize][];
	  int index = 0;
	  while ((strLine = br.readLine()) != null) {
	   
	   if (index >= arraySize) {
	    System.out.println("Error : Increase array size !");
	    break;
	   }
	   array[index] = strLine.getBytes(Charset.forName("UTF-8"));
	   index++;
	  }
	  return array;
	}

	 public static void printAll(byte array[][]) {

	  for (int i = 0; i < array.length; i++) {

	   if (array[i] != null) {
	    for (int j = 0; j < array[i].length; j++) {
	     System.out.print((char)array[i][j] + " ");
	    }
	    System.out.println(" ");
	   }
	  }
	 }
	 
	 public static void main(String[] args) throws IOException {
		 Read2D.printAll(Read2D.read2dArray("/home/matt/workspace/drawstring/src/map.txt",countLines("/home/matt/workspace/drawstring/src/map.txt")+1));
	 }
}

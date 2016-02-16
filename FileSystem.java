//Note: Code is only for caching the files in directory and not for further sub directories.
import java.util.HashMap;
import java.util.Map;

import java.io.*;
class FileSystem
{
    static BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    
    //main method to take user inputs for creating the cache and then read/write the file as per user choices
    
    public static void main(String args[])throws IOException
    {
      FileSystem obj=new FileSystem();
      String directory="";
      System.out.println("Enter the directory to be accessed");
      directory=br.readLine();
      directory=directory.replace("\\", "\\\\");
      System.out.println("Enter 0 for caching all the files, or 1 for caching files only with .txt extension");
      int choice=Integer.parseInt(br.readLine());
      
      Map cache=obj.createCache(directory, choice);
      if (cache!=null)
      System.out.println ("Cache Created successfully!");
      else
      {
           System.out.println ("Problems creating the cache!");
           System.exit(0);
      }
      
      System.out.println ("enter the path of the file to be accessed");
      String filePath=br.readLine();
      filePath=filePath.replace("\\", "\\\\");
      
      System.out.println("Enter 0 for reading the files, or 1 for editing the file");
      int rOrW=Integer.parseInt(br.readLine());
      
      switch (rOrW)
      {
          case 0:
          obj.readFile(filePath, cache);
          break;
          
          case 1:
          obj.writeFile(filePath, cache);
          break;
      }
      
    }
    
    //Method to edit a specific file specified by the user
    
    public void writeFile(String filePath, Map cache) throws IOException
    {
      System.out.println ("enter 0 for overwriting the file or 1 for appending the file");
      int overwriteOrAppend=Integer.parseInt(br.readLine());
      boolean append= overwriteOrAppend==1?true:false;
       try
       {
        FileOutputStream fw=new FileOutputStream(filePath, append);
        DataOutputStream fout=new DataOutputStream(fw); 
        
          System.out.println("Enter data to enter in the file");
          String data=br.readLine();
          fout.writeUTF(data);
          if (append) //1 if data is to be appended
          cache.put(filePath,cache.get(filePath)+data);
          else
          cache.put(filePath, data);
          
        fout.close();
        fw.close();
       }
       catch(Exception e)
       {
          System.out.println("File open error");
        }  
      
    }
    
    //Method to read a specific file specified by the user
    
    public void readFile(String filePath, Map cache)throws IOException
    {
      System.out.println ("enter the length of data to be read and the position of start");
      int len=Integer.parseInt(br.readLine());
      int start=Integer.parseInt(br.readLine());
      
      String cachedData = (String)cache.get(filePath);
      if(cachedData!=null) //cache entry is present for given file
      {
          String requiredContent=cachedData.substring(start,start+len);
          System.out.println("Desired content from file"+requiredContent);
      }
      else // cache doesn't has file entry
      {
          String requiredContent=giveCompleteFileContent(filePath);
          System.out.println("Desired content from file"+requiredContent);
      }
    }
    
    //Method to get complete content of the file for adding into the cache or finding specific data in it
    
    public String giveCompleteFileContent(String fileName)
    {
      String content="";
       try
      {
      FileInputStream fr=new FileInputStream(fileName);
      DataInputStream fin=new DataInputStream(fr);
      System.out.println("Output-->");
       boolean eof = false;
       while(!eof)
       {
         content=fin.readUTF();
         System.out.println("content "+content);
      }
        eof = true;
        fr.close();
        fin.close();
       }
        catch(Exception e)
       {
       } 
       return content;
    }
    
    //Method to create a cache in from of a HashMap of filename to content mapping
    
    public Map createCache(String directory, int choice)
    {
      File folder = new File(directory);
      File[] listOfFiles = folder.listFiles();
      Map cache = new HashMap();
      switch (choice)
      {
          case 0:
          for (int i = 0; i < listOfFiles.length; i++) 
              if (listOfFiles[i].isFile()) 
              {
                  System.out.println("File " + listOfFiles[i].getName());
                  cache.put(directory+"\\\\"+listOfFiles[i].getName(), giveCompleteFileContent(listOfFiles[i].getName()) );
                }
           break;
           case 1:
           for (int i = 0; i < listOfFiles.length; i++) 
              if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".txt")) 
              {
                  System.out.println("File " + listOfFiles[i].getName());
                  cache.put(directory+"\\\\"+listOfFiles[i].getName(), giveCompleteFileContent(listOfFiles[i].getName()) );
                }
           break;
        }
        return cache;
    }
}
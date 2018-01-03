package org.openapps.csr;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public final class ResultFetcher {

    private static int countLines(String str){
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }

    public static Student fetchResult(String USN) throws Exception {
        String response="";
        HttpURLConnection connection = null;
        if(USN.length()!=10 || ((!USN.substring(0,3).equals("4JC"))&&(!USN.substring(0,3).equals("4jc")))) //Use String::equals()
            throw new StudentException("Invalid USN number");

        try {
            String urlParameters = "USN=" + URLEncoder.encode(USN, "UTF-8") + "&submit_result=" + URLEncoder.encode("Fetch Result", "UTF-8");
            URL url = new URL("http://sjce.ac.in/view-results");

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                String[] subLines;
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
                //PrintWriter out = new PrintWriter("result.html");
                //out.print(response);
                //File inp=new File("result.html");
                Document doc = Jsoup.parse(response);
                Elements nameAndUSN = doc.getElementsByTag("center");
                Student student=new Student(USN);
                StringBuffer stringBuffer =new StringBuffer(nameAndUSN.select("h1").first().text());
                student.setName(stringBuffer.substring(7));
                Elements marks = doc.getElementsByTag("table");
                stringBuffer = new StringBuffer("");
                String temp = "";
                //System.out.println(marks.toString());
                for (Element mark : marks) {
                    for (Element row : mark.select("tr")) {
                        //System.out.println("*"+row.toString()+"*");
                        Elements tds = row.select("td");
                        for (Element e : tds)   {
                            temp = temp + e.text() + "|";
                        }
                        //System.out.println("^"+temp+"^");
                        //System.out.println("HH" + tds.toString()+" HH ");
                        //stringBuffer.append(tds.text());
                        stringBuffer.append(temp);
                        stringBuffer.append("\n");
                        temp = "";  //empty the temp string
                    }
                }
                //System.out.print(stringBuffer);
                stringBuffer = new StringBuffer(stringBuffer.substring(1));//removes first line...it was empty line :P
                String subCode,subName, grade;
                Scanner scanner = new Scanner(stringBuffer.toString());
                //System.out.println("NL : "+org.opensolutions.ResultFetcher.countLines(stringBuffer.toString()));
                while (scanner.hasNextLine()) { //foreach
                    line = scanner.nextLine();

                    subLines = line.split("\\|");

                    //subCode=line.substring(0,5);
                    subCode = subLines[0];
                    subName = subLines[1];
                    grade = subLines[2];
                    //System.out.println(subCode);
                    //grade=line.substring(line.lastIndexOf(" ") + 1);
                    student.addMarksInASubject(subCode, grade);
                    student.addSubject(subCode, subName);
                }
                scanner.close();
                return student;

            }
            else {
                throw new StudentException("Problem with the network bro!");

            }
        }
        catch (Exception e) {
            //if(e instanceof WebServiceException)    {
            //  throw new WebServiceException("Problem with the network bro!",e);
            //}
            if(e instanceof IOException)   {
                throw new IOException(e.getMessage(),e);
            }
            else if(e instanceof StudentException)
                throw new StudentException(e.getMessage(),e);
            else
                throw new Exception("Fatal Error",e);
        }
        finally {

            if (connection != null) {
                connection.disconnect(); //GOOD :)
            }
        }
    }

}
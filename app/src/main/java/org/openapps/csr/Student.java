package org.openapps.csr;


import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class Student   {
    private String name;
    private String USN;
    private LinkedHashMap<String,String> marksMap;
    private LinkedHashMap<String, String> subjectMap;

    public Student(String USN)  throws StudentException {
        marksMap = new LinkedHashMap<>();
        subjectMap = new LinkedHashMap<>();
        this.USN=USN;
        if(USN.length()!=10 || ((!USN.substring(0,3).equals("4JC"))&&(!USN.substring(0,3).equals("4jc")))) //Use String::equals()
            throw new StudentException("Invalid USN number");
    }

    public void addMarksInASubject(String subCode, String grade) {
        //Tuple<String, String> t = new Tuple<>(subName, grade);
        marksMap.put(subCode,grade);
    }

    public void addSubject(String subCode, String subName)  {
        subjectMap.put(subCode,subName);
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getUSN()  {
        return USN;
    }

    /*public String getJsonOfStudent()  throws org.opensolutions.StudentException  {
        String json;
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter;
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        try {
            json =objectWriter.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new org.opensolutions.StudentException("JsonProcessingException");
        }
        return json;
    }*/

    public String getName() {
        return this.name;
    }

    public float getSgpa()  {
        float sgpa= (float) 0.0,totalCredits=0;
        Set s = marksMap.entrySet();
        Iterator iterator = s.iterator();
        while (iterator.hasNext())  { //Use foreach
            Map.Entry mapEntry = (Map.Entry)iterator.next();
            sgpa+=getCredits(mapEntry.getKey().toString())*getGradePoint(mapEntry.getValue().toString());
            totalCredits+=getCredits(mapEntry.getKey().toString());
        }
        return sgpa/totalCredits;
    }

    private float getCredits(String subCode)    {
        if(subCode.contains("HU"))
            return 0;
        else if(subCode.contains("L"))
            return (float) 1.5;
        else
            return 4;
    }

    private int getGradePoint(String grade) {
        switch (grade)  {
            case "S" : return 10;
            case "A" : return 9;
            case "B" : return 8;
            case "C" : return 7;
            case "D" : return 5;
            case "E" : return 4;
            default:   return 0;
        }
    }

    @Override
    public String toString()    {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Name : %s\n", name));
        sb.append(String.format("USN : %s\n", USN));
        sb.append(String.format("Marks : \n"));
        //sb.append("Marks : \n");
        Set s = marksMap.entrySet();
        Iterator iterator = s.iterator();
        while (iterator.hasNext()) { //foreach please
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            sb.append(mapEntry.getKey() + "("+subjectMap.get(mapEntry.getKey())+") : " + mapEntry.getValue());
            sb.append(String.format("\n"));
        }
        sb.append("SGPA : "+this.getSgpa());
        return sb.toString();
    }

    public String getMarks()   {
        StringBuilder sb = new StringBuilder();
        Set s = marksMap.entrySet();
        Iterator iterator = s.iterator();
        while (iterator.hasNext()) { //foreach please
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            sb.append(mapEntry.getKey() + "|" +subjectMap.get(mapEntry.getKey())+ "|" + mapEntry.getValue());
            sb.append("\n");
        }
        sb.append("SGPA : "+this.getSgpa());
        return sb.toString();
    }
/*
    public void displayAll()    {
        //No System.out.println()
        System.out.println("Name : "+name);
        System.out.println("USN : "+USN);
        System.out.println("Marks : ");
        Set s = marksMap.entrySet();
        Iterator iterator = s.iterator();
        while (iterator.hasNext())  { //foreach
            Map.Entry mapEntry = (Map.Entry)iterator.next();
            System.out.println(mapEntry.getKey() + " : "+mapEntry.getValue());
        }
    }*/

}
package org.usfirst.frc.team3452.robot.subsystems;
import java.util.ArrayList;

public class To_Add{

     public static void main(String []args){
        ArrayList<ArrayList<String>> subsystem = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        
        temp.add("Encoder");
        temp.add("ERROR");
        
        subsystem.add(temp);
        
        temp = new ArrayList<>();
        
        temp.add("Current");
        temp.add("WARNING");
        
        subsystem.add(temp);

        System.out.println(subsystem);
         
     }
}

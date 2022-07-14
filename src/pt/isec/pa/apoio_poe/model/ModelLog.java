package pt.isec.pa.apoio_poe.model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class ModelLog {
    private static ModelLog instance;
    private ArrayList<String> msg;

    private ModelLog(){
        msg = new ArrayList<>();
    }

    public static ModelLog getInstance(){
        if(instance == null){
            instance = new ModelLog();
        }
        return instance;
    }

    public void add(String e){
        msg.add(e);
    }

    public void reset(){
        msg.clear();
    }

    public ArrayList<String> getLog() {
        return msg;
    }

    public void exportLog() throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd_HH.mm.ss").withZone(ZoneId.systemDefault());
        Date date=new Date();
        Instant inst = date.toInstant();
        String nome_fich = "logfile" + dtf.format(inst) + ".txt";
        dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").withZone(ZoneId.systemDefault());
        StringBuilder sb = new StringBuilder("Registo - " + dtf.format(inst) + "\n\n");
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nome_fich), StandardCharsets.UTF_8));
        sb.append("Detalhes das operações: \n\n");
        for(String s : msg){
            sb.append(s + " \n");
        }
        writer.write(sb.toString());
        writer.close();
    }

}

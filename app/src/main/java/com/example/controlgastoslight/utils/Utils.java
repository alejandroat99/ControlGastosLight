package com.example.controlgastoslight.utils;

import android.content.Context;

import com.example.controlgastoslight.db.actions.RegistroActions;
import com.example.controlgastoslight.db.model.Registro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Utils {
    /**
     * Devuelve un mapa con claves booleanas que representan cada tipo de registro
     * en la aplicacion, el valor guardado es una lista con los rgistros que pertenecen
     * a dicho tipo
     * @param input
     * @return
     */
    public static Map<Boolean, List<Registro>> groupByType(List<Registro> input){
        Map<Boolean, List<Registro>> res = new HashMap<>();
        res.put(true, new ArrayList<>());
        res.put(false, new ArrayList<>());

        for (Registro r : input) {
            List<Registro> regs = res.get(r.isGasto());
            regs.add(r);
            res.put(r.isGasto(), regs);
        }
        return res;
    }

    /**
     * Usa la lista de registros pasada por parametro para organizar dichos registros
     * por su fecha. La clave de las entradas es la fecha escrita en String con el formato
     * "dd/mm/YYYY" y el valor es la lista de registros.
     * @param input
     * @return
     */
    public static Map<String, List<Registro>> groupByDate(List<Registro> input){
        Map<String, List<Registro>> res = new HashMap<>();
        for(Registro r : input){
            List<Registro> regs;
            if(res.containsKey(r.getFecha())){
                regs = res.get(r.getFecha());
            }else{
                regs = new ArrayList<>();
            }
            regs.add(r);
            res.put(r.getFecha(), regs);
        }
        return res;
    }

    public static float sum(List<Registro> input){
        float res = 0;
        for(Registro r : input){
            res += r.getValue();
        }
        return res;
    }

    public static List<Registro> getRegistrosToday(Context context){
        RegistroActions ra = new RegistroActions(context);
        try {
            List<Registro> allRegistros = ra.getAllRegistro();
            Map<String, List<Registro>> filter_date = groupByDate(allRegistros);
            String today = getDate();
            return filter_date.get(today);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Registro> getRegistrosMonth(Context context){
        RegistroActions ra = new RegistroActions(context);
        try {
            List<Registro> allRegistros = ra.getAllRegistro();
            Map<String, List<Registro>> filter_date = groupByDate(allRegistros);

            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            List<String> dates = new ArrayList<>();
            for(String date : filter_date.keySet()){
                if(date.contains(month+"/"+year)){
                    dates.add(date);
                }
            }

            List<Registro> res = new ArrayList<>();
            for (String month_date : dates){
                res.addAll(filter_date.get(month_date));
            }

            return res;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Registro> getRegistrosYear(Context context){
        RegistroActions ra = new RegistroActions(context);
        try {
            List<Registro> allRegistros = ra.getAllRegistro();
            Map<String, List<Registro>> filter_date = groupByDate(allRegistros);

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            List<String> dates = new ArrayList<>();
            for(String date : filter_date.keySet()){
                if(date.contains("/"+year)){
                    dates.add(date);
                }
            }

            List<Registro> res = new ArrayList<>();
            for (String year_date : dates){
                res.addAll(filter_date.get(year_date));
            }

            return res;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Registro> getRegistrosWeek(Context context){
        RegistroActions ra = new RegistroActions(context);
        try {
            List<Registro> allRegistros = ra.getAllRegistro();
            Map<String, List<Registro>> filter_date = groupByDate(allRegistros);

            Calendar calendar = Calendar.getInstance();
            int week = calendar.get(Calendar.WEEK_OF_YEAR);
            int year = calendar.get(Calendar.YEAR);
            List<String> dates = new ArrayList<>();
            for(String date : filter_date.keySet()){
                Date aux_date = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                Calendar aux_calendar = Calendar.getInstance();
                aux_calendar.setTime(aux_date);
                if( week == aux_calendar.get(Calendar.WEEK_OF_YEAR) && date.contains("/"+year)){
                    dates.add(date);
                }
            }

            List<Registro> res = new ArrayList<>();
            for (String week_date : dates){
                res.addAll(filter_date.get(week_date));
            }

            return res;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDate(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return String.format("%d/%d/%d", day, month, year);
    }
}

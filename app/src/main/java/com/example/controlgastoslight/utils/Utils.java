package com.example.controlgastoslight.utils;

import com.example.controlgastoslight.db.model.Registro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
